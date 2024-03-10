package fr.internetcartographier.model.internetgraph;

import fr.internetcartographier.model.WeightMetric;
import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import fr.internetcartographier.util.shortestpath.BreadthFirstSearch;
import fr.internetcartographier.util.shortestpath.Dijkstra;
import fr.internetcartographier.util.cache.CacheMinimumDistances;
import fr.internetcartographier.util.path.Path;
import fr.internetcartographier.util.statistics.*;

import java.util.*;
import java.util.function.Predicate;

/**
 * The {@code InternetGraph} class represents a graph of nodes and edges,
 * modeling
 * the structure of an internet network. It includes methods for adding and
 * removing
 * nodes and edges, as well as finding minimum distances between nodes using
 * various
 * weight metrics.
 */
public class InternetGraph implements Statisticable {

	/**
	 * The set of nodes in the graph.
	 */
	private Set<Node> nodes;

	/**
	 * The adjacency list representing edges between nodes.
	 */
	private Map<String, Set<Edge>> adjacencyList;

	/**
	 * The cache for storing minimum distances between nodes.
	 */
	private final CacheMinimumDistances cache;

	/**
	 * Constructs an {@code InternetGraph} object by copying another
	 * {@code InternetGraph} object.
	 *
	 * @param internetGraph The InternetGraph object to copy.
	 */
	// TODO Not a deep copy by the way
	public InternetGraph(InternetGraph internetGraph) {
		nodes = internetGraph.nodes;
		adjacencyList = internetGraph.adjacencyList;
		cache = internetGraph.cache;
	}

	/**
	 * Constructs an empty {@code InternetGraph} object.
	 */
	public InternetGraph() {
		nodes = new HashSet<>();
		adjacencyList = new HashMap<>();
		cache = new CacheMinimumDistances();
	}

	/**
	 * Gets the set of nodes in the graph.
	 *
	 * @return The set of nodes.
	 */
	public Set<Node> getNodes() {
		return nodes;
	}

	/**
	 * Gets the cache for storing minimum distances between nodes.
	 *
	 * @return The cache for minimum distances.
	 */
	public CacheMinimumDistances getCache() {
		return cache;
	}

	/**
	 * Gets a node from the graph based on its IP address.
	 *
	 * @param  ipAddress The IP address of the node to retrieve.
	 * @return           An optional containing the node if found, or empty
	 *                   otherwise.
	 */
	public Optional<Node> getNode(IPAddress ipAddress) {
		return getNode(ipAddress.getStringIpAddress());
	}

	/**
	 * Gets a node from the graph based on its IP address.
	 *
	 * @param  ipAddress The IP address of the node to retrieve.
	 * @return           An optional containing the node if found, or empty
	 *                   otherwise.
	 */
	public Optional<Node> getNode(String ipAddress) {
		for (Node node : nodes) {
			if (node.getStringIpAddress().equals(ipAddress)) {
				return Optional.of(node);
			}
		}
		return Optional.empty();
	}

	/**
	 * Gets an edge between two nodes based on their IP addresses.
	 *
	 * @param  ipAddressA The IP address of the first node.
	 * @param  ipAddressB The IP address of the second node.
	 * @return            An optional containing the edge if found, or empty
	 *                    otherwise.
	 */
	public Optional<Edge> getEdge(IPAddress ipAddressA, IPAddress ipAddressB) {
		if (!adjacencyList.keySet().contains(ipAddressA.getStringIpAddress())
				|| !adjacencyList.keySet().contains(ipAddressB.getStringIpAddress())) {
			throw new IllegalArgumentException();
		}
		for (Edge edge : adjacencyList.get(ipAddressA.getStringIpAddress())) {
			if (edge.isContainNode(ipAddressB)) {
				return Optional.of(edge);
			}
		}
		return Optional.empty();
	}

	/**
	 * Gets a set of all edges in the graph.
	 *
	 * @return The set of edges.
	 */
	public Set<Edge> getEdges() {
		Set<Edge> edges = new HashSet<>();
		for (Map.Entry<String, Set<Edge>> entry : adjacencyList.entrySet()) {
			edges.addAll(entry.getValue());
		}
		return edges;
	}

	/**
	 * Gets a set of edges connected to a specific node.
	 *
	 * @param  node The node for which to retrieve connected edges.
	 * @return      The set of edges connected to the node.
	 */
	public Set<Edge> getEdges(Node node) {
		return adjacencyList.get(node.getStringIpAddress());
	}

	/**
	 * Gets a set of edges connected to a node based on its IP address.
	 *
	 * @param  ipAddress The IP address of the node for which to retrieve connected
	 *                   edges.
	 * @return           The set of edges connected to the node.
	 */
	public Set<Edge> getEdges(IPAddress ipAddress) {
		return adjacencyList.get(ipAddress.getStringIpAddress());
	}

	/**
	 * Gets a set of edges connected to a node based on its IP address.
	 *
	 * @param  ipAddress The IP address of the node for which to retrieve connected
	 *                   edges.
	 * @return           The set of edges connected to the node.
	 */
	public Set<Edge> getEdges(String ipAddress) {
		if (!adjacencyList.containsKey(ipAddress)) {
			throw new IllegalArgumentException();
		}
		return adjacencyList.get(ipAddress);
	}

	/**
	 * Gets a set of neighboring nodes for a given node.
	 *
	 * @param  node The node for which to retrieve neighbors.
	 * @return      The set of neighboring nodes.
	 */
	public Set<Node> getNeighbors(Node node) {
		return getNeighbors(node.getIpAddress());
	}

	/**
	 * Gets a set of neighboring nodes for a node based on its IP address.
	 *
	 * @param  ipAddress The IP address of the node for which to retrieve neighbors.
	 * @return           The set of neighboring nodes.
	 */
	public Set<Node> getNeighbors(IPAddress ipAddress) {
		return getNeighbors(ipAddress.getStringIpAddress());
	}

	/**
	 * Gets a set of neighboring nodes for a node based on its IP address.
	 *
	 * @param  ipAddress The IP address of the node for which to retrieve neighbors.
	 * @return           The set of neighboring nodes.
	 */
	public Set<Node> getNeighbors(String ipAddress) {
		Set<Node> neighbors = new HashSet<>();
		Set<Edge> edges = getEdges(ipAddress);
		for (Edge edge : edges) {
			neighbors.add(edge.getAnotherNode(ipAddress));
		}
		return neighbors;
	}

	/**
	 * Gets the adjacency list representing edges between nodes.
	 *
	 * @return The adjacency list.
	 */
	public Map<String, Set<Edge>> getAdjacencyList() {
		return adjacencyList;
	}

	/**
	 * Adds a node to the graph.
	 *
	 * @param node The node to add.
	 */
	public void addNode(Node node) {
		nodes.add(node);
		cache.addNode(node.getStringIpAddress());
		adjacencyList.put(node.getStringIpAddress(), new HashSet<>());
	}

	/**
	 * Removes a node from the graph.
	 *
	 * @param ipAddress The IP address of the node to remove.
	 */
	public void removeNode(IPAddress ipAddress) {
		nodes.removeIf(node -> node.getIpAddress().equals(ipAddress));
		adjacencyList.remove(ipAddress.getStringIpAddress());
		cache.removeNode(ipAddress.getStringIpAddress());
		for (Set<Edge> edges : adjacencyList.values()) {
			edges.removeIf(edge -> edge.isContainNode(ipAddress));
		}
	}

	/**
	 * Adds an edge to the graph.
	 *
	 * @param edge The edge to add.
	 */
	public void addEdge(Edge edge) {
		if (!adjacencyList.containsKey(edge.getNodeA().getStringIpAddress())
				|| !adjacencyList.containsKey(edge.getNodeB().getStringIpAddress())
				|| !nodes.contains(edge.getNodeA()) || !nodes.contains(edge.getNodeB())) {
			throw new IllegalArgumentException();
		}

		adjacencyList.get(edge.getNodeA().getStringIpAddress()).add(edge);
		adjacencyList.get(edge.getNodeB().getStringIpAddress()).add(edge);
		cache.resetCache();
	}

	/**
	 * Removes an edge between two nodes based on their IP addresses.
	 *
	 * @param ipAddressA The IP address of the first node.
	 * @param ipAddressB The IP address of the second node.
	 */
	public void removeEdge(IPAddress ipAddressA, IPAddress ipAddressB) {
		if (!adjacencyList.containsKey(ipAddressA.getStringIpAddress())
				|| !adjacencyList.containsKey(ipAddressB.getStringIpAddress())) {
			throw new IllegalArgumentException();
		}

		adjacencyList.get(ipAddressA.getStringIpAddress()).removeIf(edge -> edge.isContainNode(ipAddressB));
		adjacencyList.get(ipAddressB.getStringIpAddress()).removeIf(edge -> edge.isContainNode(ipAddressA));

		cache.resetCache();
	}

	/**
	 * Gets an iterator for the adjacency list.
	 *
	 * @return The iterator for the adjacency list.
	 */
	public Iterator<Map.Entry<String, Set<Edge>>> getAdjacencyListIterator() {
		return adjacencyList.entrySet().iterator();
	}

	/**
	 * Finds the minimum distance path between two nodes in the graph.
	 * If the source equals the destination => return a path of cost 0 immediately.
	 * If the cherched min path is already in the cache, so return it immediately.
	 * Else, calculate it using the two algorithmes BFS and Dijkstra. If the metric
	 * is CONSTANT
	 * or TIME_RESPONSE, so use BFS. Else Dijkstra.
	 * Stock all the gotten results in the cache for using after.
	 *
	 * @param  source             The source node's IP address.
	 * @param  destination        The destination node's IP address.
	 * @param  weightMetric       The weight metric for calculating distances.
	 * @param  geolocationService The geolocation service class.
	 * @return                    The minimum distance path between the source and
	 *                            destination nodes.
	 */
	// TODO Should return a Path<IPAddress> so view can access more data when fetching path
	public Path<String> minimumDistance(IPAddress source, IPAddress destination, WeightMetric weightMetric,
			Class<? extends GeolocationService> geolocationService) {
		if (source.equals(destination)) {
			return new Path<>(Collections.singletonList(source.getStringIpAddress()), 0);
		}
		Optional<Path<String>> minPathCache = cache.getMinPath(weightMetric, geolocationService.getCanonicalName(),
				source.getStringIpAddress(), destination.getStringIpAddress());
		if (minPathCache.isPresent()) {
			return minPathCache.get();
		}
		Optional<Node> nodeSource = getNode(source);
		Optional<Node> nodeDestination = getNode(destination);
		if (nodeSource.isEmpty() || nodeDestination.isEmpty()) {
			throw new IllegalArgumentException();
		}
		if (weightMetric == WeightMetric.CONSTANT) {
			Path<String> pathBFS = BreadthFirstSearch.getMinPath(source.getStringIpAddress(), destination.getStringIpAddress(), this);
			cache.updateCache(weightMetric, geolocationService.getCanonicalName(), pathBFS);
			return pathBFS;
		}
		Dijkstra.DijkstraResult result = Dijkstra.getMinPath(source.getStringIpAddress(), this, weightMetric,
				geolocationService);

		Map<String, String> predecessors = result.getPredecessors();
		Map<String, Double> distances = result.getDistances();

		Path<String> minPath = null;
		for (String node : distances.keySet()) {
			List<String> listPath = new LinkedList<>();
			String crawl = node;
			listPath.add(crawl);

			if (predecessors.get(crawl).isEmpty()) {
				listPath.add(source.getStringIpAddress());
			} else {
				while (!predecessors.get(crawl).isEmpty()) {
					listPath.add(predecessors.get(crawl));
					crawl = predecessors.get(crawl);
				}
			}
			Collections.reverse(listPath);
			Path<String> path = new Path<>(listPath, distances.get(node));
			cache.updateCache(weightMetric, geolocationService.getCanonicalName(), path);
			if (node.equals(destination.getStringIpAddress())) {
				minPath = path;
			}
		}
		if (minPath == null) {
			throw new IllegalStateException();
		}

		return minPath;
	}

	/**
	 * Updates the geolocation data for nodes and edges in the graph. Use chosen
	 * geolocation service
	 * If the geo data generated by the chosen geolocation service is already in the
	 * cache of the nodes
	 * => Use it immediately.
	 * Else use the chosen geolocation service to generate new geo data for the
	 * nodes.
	 * After that, reset the cache of min distances relating to the chosen
	 * geolocation service, because
	 * the geo data of the nodes have changed => their related min paths are changed
	 * too.
	 *
	 * @param geolocationService The geolocation service to use for updating data.
	 */
	public void updateNodesGeolocationDatas(GeolocationService geolocationService) {
		for (Node node : nodes) {
			if (!node.isNodeContainsAlreadyGeolocationData(geolocationService.getClass())) {
				node.updateGeolocationData(new GeolocationData.TypedGeolocationData(geolocationService.getClass(),
						geolocationService.getGeolocationData(node.getIpAddress())));
			}
		}

		for (Set<Edge> edges : adjacencyList.values()) {
			for (Edge edge : edges) {
				Node nodeA = edge.getNodeA();
				Node nodeB = edge.getNodeB();
				if (!nodeA.isNodeContainsAlreadyGeolocationData(geolocationService.getClass())) {
					Optional<GeolocationData> newGeolocationDataNodeA = geolocationService
							.getGeolocationData(nodeA.getIpAddress());
					edge.updateGeolocationDataNodeA(new GeolocationData.TypedGeolocationData(
							geolocationService.getClass(), newGeolocationDataNodeA));
				}
				if (!nodeB.isNodeContainsAlreadyGeolocationData(geolocationService.getClass())) {
					Optional<GeolocationData> newGeolocationDataNodeB = geolocationService
							.getGeolocationData(nodeB.getIpAddress());
					edge.updateGeolocationDataNodeA(new GeolocationData.TypedGeolocationData(
							geolocationService.getClass(), newGeolocationDataNodeB));
				}
			}
		}

		cache.resetCacheMetricDistance();
	}

	public void updateEdgesWeights() {
		// TODO What should I do ?
	}

	public Statistics getStatistics() {
		Statistics statistics = new Statistics();

		statistics.addStatistic(getNumberNodesStatistic());
		statistics.addStatistic(getNumberEdgesStatistic());

		return statistics;
	}

	private Statistic<Integer> getNumberNodesStatistic() {
		return new NumericStatistic<>("Number of nodes", nodes.size(), "");
	}

	private Statistic<Integer> getNumberEdgesStatistic() {
		return new NumericStatistic<>("Number of edges", getEdges().size(), "");
	}

}
