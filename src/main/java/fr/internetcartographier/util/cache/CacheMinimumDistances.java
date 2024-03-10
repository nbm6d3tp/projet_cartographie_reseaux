package fr.internetcartographier.util.cache;

import fr.internetcartographier.model.WeightMetric;
import fr.internetcartographier.model.geolocationservice.GeoIP2;
import fr.internetcartographier.model.geolocationservice.HostIP;
import fr.internetcartographier.util.path.Path;

import java.util.*;

/**
 * A cache for storing and retrieving minimum distances between nodes in an
 * Internet graph.
 * The cache is used to store minimum paths computed using various weight
 * metrics and geolocation services.
 */
public class CacheMinimumDistances {
	
	private final Map<String, Map<String, Map<String, Path<String>>>> cache;

	/**
	 * Constructs a new CacheMinDistances instance by copying the content of another
	 * instance.
	 *
	 * @param cacheMinDistance Another CacheMinDistances instance to copy.
	 */
	// TODO You are not copying anything except the CacheMinDistances is this normal ? previous.cache == new.cache at this point (shared instance)
	public CacheMinimumDistances(CacheMinimumDistances cacheMinDistance) {
		cache = cacheMinDistance.cache;
	}

	/**
	 * Constructs a new CacheMinDistances instance.
	 */
	public CacheMinimumDistances() {
		cache = new HashMap<>();
	}

	/**
	 * Checks if the cache contains information for the specified node.
	 *
	 * @param  node The IP address of the node.
	 * @return      True if the cache contains information for the node, false
	 *              otherwise.
	 */
	public boolean containsNode(String node) {
		return cache.containsKey(node);
	}

	/**
	 * Add a node to cache.
	 * 
	 * @param  node The IP address of the node.
	 */
	public void addNode(String node) {
		cache.put(node, new HashMap<>());
	}

	/**
	 * Removes a node and its associated information from the cache.
	 *
	 * @param node The IP address of the node to remove.
	 */
	public void removeNode(String node) {
		cache.remove(node);
		resetCache();
	}

	/**
	 * Resets the entire cache, removing all nodes and their associated information.
	 */
	public void resetCache() {
		for (String node : cache.keySet()) {
			cache.replace(node, new HashMap<>());
		}
	}

	/**
	 * Resets the cache for a specific weight metric, removing all stored paths.
	 */
	// TODO Need explanation
	public void resetCacheMetricDistance() {
		String caseHostIP = getKeyName(WeightMetric.DISTANCE, HostIP.class.getCanonicalName());
		String caseGeoIP2 = getKeyName(WeightMetric.DISTANCE, GeoIP2.class.getCanonicalName());

		for (Map<String, Map<String, Path<String>>> cacheOfEachNode : cache.values()) {
			if (cacheOfEachNode.containsKey(caseHostIP)) {
				cacheOfEachNode.replace(caseHostIP, new HashMap<>());
			}
			if (cacheOfEachNode.containsKey(caseGeoIP2)) {
				cacheOfEachNode.replace(caseGeoIP2, new HashMap<>());
			}
		}
	}

	// Helper method to construct a key name for the cache.
	private String getKeyName(WeightMetric weightMetric, String geolocationService) {
		return weightMetric.equals(WeightMetric.DISTANCE) ? weightMetric.name() + geolocationService
				: weightMetric.name();
	}

	/**
	 * Updates the cache with a new minimum path for a specific weight metric and
	 * geolocation service. Add the reversed path to the node of destination too.
	 *
	 * @param weightMetric       The weight metric used in the path calculation.
	 * @param geolocationService The geolocation service used in the path
	 *                           calculation.
	 * @param path               The minimum path to update the cache with.
	 */
	// TODO Refactorable there is some code redundancy (a function to initialize cache for a node, and call it for source and destination)
	public void updateCache(WeightMetric weightMetric, String geolocationService, Path<String> path) {
		String caseName = getKeyName(weightMetric, geolocationService);
		String source = path.getSource();
		String destination = path.getDestination();

		if (!cache.containsKey(source) || !cache.containsKey(destination)) {
            throw new IllegalArgumentException("Error: Source (" + source + ") or destination (" + destination + ") are not cached!");
		}

		for (String node : path) {
			if (!cache.containsKey(node)) {
				throw new IllegalArgumentException("Error: Node (" + node + ") from path (" + path + ") is not cached!");
			}
		}

		Map<String, Map<String, Path<String>>> cacheOfSource = cache.get(source);
		Map<String, Map<String, Path<String>>> cacheOfDestination = cache.get(destination);

		if (!cacheOfSource.containsKey(caseName)) {
			cacheOfSource.put(caseName, new HashMap<>());
		}

		if (!cacheOfDestination.containsKey(caseName)) {
			cacheOfDestination.put(caseName, new HashMap<>());
		}

		Map<String, Path<String>> cacheOfSourceByCaseName = cacheOfSource.get(caseName);
		Map<String, Path<String>> cacheOfDestinationByCaseName = cacheOfDestination.get(caseName);

		if (cacheOfSourceByCaseName.containsKey(destination)) {
			cacheOfSourceByCaseName.replace(destination, path);
		} else {
			cacheOfSourceByCaseName.put(destination, path);
		}

		if (cacheOfDestinationByCaseName.containsKey(source)) {
			cacheOfDestinationByCaseName.replace(source, path.getReversedOrder());
		} else {
			cacheOfDestinationByCaseName.put(source, path.getReversedOrder());
		}
	}

	/**
	 * Retrieves the minimum path for a specific weight metric and geolocation
	 * service between two nodes.
	 *
	 * @param  weightMetric       The weight metric used in the path calculation.
	 * @param  geolocationService The geolocation service used in the path
	 *                            calculation.
	 * @param  source             The source node's IP address.
	 * @param  destination        The destination node's IP address.
	 * @return                    An Optional containing the minimum path if found,
	 *                            or empty if not.
	 */
	// TODO You use a lot of Optional in your code, I don't always understand why (here for example). Is this a way for you to handle nullable stuff ?
	public Optional<Path<String>> getMinPath(WeightMetric weightMetric, String geolocationService, String source,
			String destination) {
		String caseName = getKeyName(weightMetric, geolocationService);

		if (!cache.containsKey(source) || !cache.containsKey(destination)) {
			throw new IllegalArgumentException();
		}

		if (source.equals(destination)) {
			return Optional.of(new Path<>(List.of(source), 0));
		}

		Map<String, Map<String, Path<String>>> cacheOfSource = cache.get(source);

		if (!cacheOfSource.containsKey(caseName)) {
			Map<String, Map<String, Path<String>>> cacheOfDestination = cache.get(destination);

			if (!cacheOfDestination.containsKey(caseName)) {
				return Optional.empty();
			}

			Map<String, Path<String>> cacheOfDestinationByCaseName = cacheOfDestination.get(caseName);

			return Optional.ofNullable(cacheOfDestinationByCaseName.get(source));
		}

		Map<String, Path<String>> cacheOfSourceByCaseName = cacheOfSource.get(caseName);

		return Optional.ofNullable(cacheOfSourceByCaseName.get(destination));
	}

}
