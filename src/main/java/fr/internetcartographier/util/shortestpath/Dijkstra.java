package fr.internetcartographier.util.shortestpath;

import fr.internetcartographier.model.WeightMetric;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import fr.internetcartographier.model.internetgraph.InternetGraph;
import java.util.HashMap;
import java.util.Map;

/**
 * Dijkstra's algorithm for finding the minimum path and distances from a source
 * node to all other nodes in an Internet graph.
 * The class provides a static method, `getMinPath`, which calculates the
 * minimum path and distances using Dijkstra's algorithm.
 * The resulting distances and predecessors are encapsulated in a
 * `DijkstraResult` object.
 */
public class Dijkstra {

	/**
	 * Result of Dijkstra's algorithm, containing distances and predecessors.
	 */
	// TODO Could be a record, also don't understand why we work with an inner static class (or why we dont also do that for BSF)
	public static class DijkstraResult {
		
		private final Map<String, Double> distances;
		private final Map<String, String> predecessors;

		/**
		 * Constructs a DijkstraResult with the provided distances and predecessors.
		 * Serve to profit the result of Dijkstra algorithme to feed the cache.
		 *
		 * @param distances    Map of node IP addresses to their distances from the
		 *                     source.
		 * @param predecessors Map of node IP addresses to their predecessors in the
		 *                     minimum path.
		 */
		public DijkstraResult(Map<String, Double> distances, Map<String, String> predecessors) {
			this.distances = distances;
			this.predecessors = predecessors;
		}

		/**
		 * Gets the map of node IP addresses to their distances from the source.
		 *
		 * @return Map of distances.
		 */
		public Map<String, Double> getDistances() {
			return distances;
		}

		/**
		 * Gets the map of node IP addresses to their predecessors in the minimum path.
		 *
		 * @return Map of predecessors.
		 */
		public Map<String, String> getPredecessors() {
			return predecessors;
		}
		
	}

	/**
	 * Calculates the minimum path and distances from source to all other nodes of
	 * the graph, using Dijkstra's algorithm.
	 */
	private static String minimumDistance(Map<String, Double> distances, Map<String, Boolean> spSet) {
		double m = Double.MAX_VALUE;
		String m_key = null;

		for (String ipAddress : distances.keySet()) {
			if (!spSet.get(ipAddress) && distances.get(ipAddress) <= m) {
				m = distances.get(ipAddress);
				m_key = ipAddress;
			}
		}
		
		return m_key;
	}

	public static DijkstraResult getMinPath(String source, InternetGraph graph, WeightMetric weightMetric,
			Class<? extends GeolocationService> geolocationService) {
		Map<String, Double> distances = new HashMap<>();
		Map<String, String> predecessors = new HashMap<>();
		Map<String, Boolean> spSet = new HashMap<>();

		for (String ipAddress : graph.getAdjacencyList().keySet()) {
			distances.put(ipAddress, Double.MAX_VALUE);
			predecessors.put(ipAddress, "");
			spSet.put(ipAddress, false);
		}

		distances.replace(source, 0.0);

		for (int cnt = 0; cnt < graph.getAdjacencyList().keySet().size() - 1; cnt++) {
			String ux = minimumDistance(distances, spSet);
			spSet.replace(ux, true);
			
			for (String vx : graph.getAdjacencyList().keySet()) {
				// TODO Split this in small boolean with meaningful names, its unreadable
				if (!spSet.get(vx) && graph.getEdge(new IPAddress(ux), new IPAddress(vx)).isPresent()
						&& distances.get(ux) != Double.MAX_VALUE
						&& distances.get(ux) + graph.getEdge(new IPAddress(ux), new IPAddress(vx)).get()
								.getWeight(weightMetric, geolocationService) < distances.get(vx)) {
					distances.replace(vx, distances.get(ux) + graph.getEdge(new IPAddress(ux), new IPAddress(vx)).get()
							.getWeight(weightMetric, geolocationService));
					predecessors.replace(vx, ux);
				}
			}
		}
		
		return new DijkstraResult(distances, predecessors);
	}
	
}
