package fr.internetcartographier.model;

import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import fr.internetcartographier.model.internetgraph.Edge;
import fr.internetcartographier.model.internetgraph.InternetGraph;
import fr.internetcartographier.model.internetgraph.Node;
import fr.internetcartographier.model.tracerouteresults.TracerouteResult;
import fr.internetcartographier.model.tracerouteresults.TracerouteResults;
import fr.internetcartographier.model.tracerouteresults.TracerouteRow;
import fr.internetcartographier.util.path.Path;
import fr.internetcartographier.Configuration;
import fr.internetcartographier.util.statistics.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The {@code InternetCartographier} class represents a tool for mapping the
 * Internet graph
 * based on traceroute results, geolocation services, and specified weight
 * metrics. It maintains
 * an Internet graph, traceroute results, geolocation service, origin node, and
 * weight metric.
 * This class provides methods for updating the graph, setting the geolocation
 * service,
 * updating geolocation data, and importing a complete graph. It also includes
 * methods for
 * retrieving various components of the cartographer, such as the Internet
 * graph, traceroute
 * results, geolocation service, origin node, weight metric, and statistics.
 */
public class InternetCartographier implements Statisticable {

	/**
	 * The Internet graph representing nodes and edges in the network.
	 */
	private final InternetGraph internetGraph;

	/**
	 * Traceroute results from traceroute command.
	 */
	private final TracerouteResults tracerouteResults;

	/**
	 * Geolocation service for obtaining geolocation data.
	 */
	private GeolocationService geolocationService;

	/**
	 * The origin node from which the mapping starts.
	 */
	private final Node origin;

	/**
	 * The weight metric used for calculating distances between nodes.
	 */
	private WeightMetric weightMetric;

	/**
	 * Constructs an {@code InternetCartographier} object with the specified
	 * geolocation service
	 * and weight metric. Initializes the origin node and adds it to the Internet
	 * graph.
	 *
	 * @param  geolocationService The geolocation service to use.
	 * @param  weightMetric       The weight metric for calculating distances.
	 * @throws Exception          If an error occurs during initialization.
	 */
	public InternetCartographier(GeolocationService geolocationService, WeightMetric weightMetric) {
		this.geolocationService = geolocationService;
		this.weightMetric = weightMetric;

		origin = initializeOrigin();
		internetGraph = new InternetGraph();
		tracerouteResults = new TracerouteResults();

		internetGraph.addNode(origin);
	}

	/**
	 * Constructs an {@code InternetCartographier} object with the specified
	 * Internet graph,
	 * geolocation service, and weight metric. Initializes the origin node and adds
	 * it to the Internet graph.
	 *
	 * @param  internetGraph      The existing Internet graph.
	 * @param  geolocationService The geolocation service to use.
	 * @param  weightMetric       The weight metric for calculating distances.
	 * @throws Exception          If an error occurs during initialization.
	 */
	public InternetCartographier(InternetGraph internetGraph, GeolocationService geolocationService,
			WeightMetric weightMetric) throws Exception {
		this.internetGraph = internetGraph;
		tracerouteResults = new TracerouteResults();
		this.geolocationService = geolocationService;
		this.weightMetric = weightMetric;
		origin = initializeOrigin();
		this.internetGraph.addNode(origin);
	}

	/**
	 * Initializes the origin node (the ip address of the host launching
	 * traceroute),
	 * using the current geolocation service to generate its geo data.
	 *
	 * @return           The initialized origin node.
	 * @throws Exception If an error occurs during initialization.
	 */
	private Node initializeOrigin() {
		String ip;
		try {
			Configuration properties = Configuration.getInstance();
			URL checkipURL = new URL(properties.getProperty("checkipendpoint"));

			BufferedReader in = new BufferedReader(new InputStreamReader(checkipURL.openStream()));
			ip = in.readLine();
		} catch (IOException e) {
			throw new RuntimeException("Error: Could not retrieve origin!");
		}

		IPAddress ipAddress = new IPAddress(ip);

		Optional<GeolocationData> geoData = geolocationService.getGeolocationData(ipAddress);

		return new Node(ipAddress, new GeolocationData.TypedGeolocationData(geolocationService.getClass(), geoData));
	}

	/**
	 * Gets the current weight metric used for calculating distances.
	 *
	 * @return The weight metric.
	 */
	public WeightMetric getWeightMetric() {
		return weightMetric;
	}

	/**
	 * Sets the weight metric to the specified value.
	 *
	 * @param weightMetric The new weight metric.
	 */
	private void setWeightMetric(WeightMetric weightMetric) {
		this.weightMetric = weightMetric;
	}

	// TODO need to do same as update geolocation service btw (re calculate edge
	// cost ?)
	public void updateWeightMetric(WeightMetric weightMetric) {
		setWeightMetric(weightMetric);
		internetGraph.updateEdgesWeights();
	}

	/**
	 * Gets a copy of the Internet graph.
	 *
	 * @return A copy of the Internet graph.
	 */
	public InternetGraph getInternetGraph() {
		return internetGraph;
	}

	/**
	 * Gets a copy of the traceroute results.
	 *
	 * @return A copy of the traceroute results.
	 */
	public TracerouteResults getTracerouteResults() {
		return tracerouteResults;
	}

	/**
	 * Gets the current geolocation service.
	 *
	 * @return The geolocation service.
	 */
	public GeolocationService getGeolocationService() {
		return geolocationService;
	}

	/**
	 * Gets a copy of the origin node.
	 *
	 * @return A copy of the origin node.
	 */
	public Node getOrigin() {
		return new Node(origin);
	}

	/**
	 * Sets the geolocation service to the specified value.
	 *
	 * @param service The new geolocation service.
	 */
	private void setGeolocationService(GeolocationService service) {
		geolocationService = service;
	}

	/**
	 * Updates the geolocation service and geolocation data for nodes in the
	 * Internet graph.
	 *
	 * @param service The new geolocation service.
	 */
	public void updateGeolocationService(GeolocationService service) {
		setGeolocationService(service);
		internetGraph.updateNodesGeolocationDatas(service);
	}

	/**
	 * Updates (add nodes, add edges) the graph using the traceroute results
	 * received from
	 * traceroute
	 *
	 * @param service The new geolocation service.
	 */
	public void update(TracerouteResult tracerouteResult) {
		tracerouteResults.addTracerouteResult(tracerouteResult);

		Node previousNode = origin;
		boolean direct = true;

		for (TracerouteRow row : tracerouteResult) {
			if (row.isReachable()) {
				Optional<GeolocationData> geolocationData = geolocationService.getGeolocationData(row.getIpAddress());
				GeolocationData.TypedGeolocationData typedGeolocationData = new GeolocationData.TypedGeolocationData(
						geolocationService.getClass(), geolocationData);

				Node node = new Node(row.getIpAddress(), typedGeolocationData);
				internetGraph.addNode(node);

				if (previousNode != null && !previousNode.equals(node)) {
					Edge edge = new Edge(node, previousNode, direct, row.getResponseTime());
					internetGraph.addEdge(edge);
				}

				previousNode = node;
				direct = true;
			} else {
				direct = false;
			}
		}
	}

	// Use just for testing, simultating update(TracerouteResults tracerouteResults)
	public void update(Set<String> targets) throws Exception {
		for (String ipAddressString : targets) {
			IPAddress ipAddress = new IPAddress(ipAddressString);
			Optional<GeolocationData> geoData = geolocationService.getGeolocationData(ipAddress);
			Node node = new Node(ipAddress,
					new GeolocationData.TypedGeolocationData(geolocationService.getClass(), geoData));
			internetGraph.addNode(node);
		}

		// Simulate results of TracerouteResults
		Random random = new Random();
		List<Node> nodes = new ArrayList<>(internetGraph.getNodes());
		for (Node node : nodes) {
			int numEdges = random.nextInt(5);
			for (int i = 0; i < numEdges; i++) {
				Node targetNode = nodes.get(random.nextInt(nodes.size()));
				while (node.equals(targetNode)) {
					targetNode = nodes.get(random.nextInt(nodes.size()));
				}
				boolean directed = random.nextBoolean();
				int weight = random.nextInt(10) + 1;
				internetGraph.addEdge(new Edge(node, targetNode, directed, weight));
			}
		}
	}

	/**
	 * Imports a complete graph (from csv files nodes and edges) into the Internet
	 * graph.
	 *
	 * @param importedInternetGraph The complete graph to import.
	 */
	public void importCompleteGraph(InternetGraph importedInternetGraph) {
		for (Node node : importedInternetGraph.getNodes()) {
			internetGraph.addNode(node);
		}

		for (Edge edge : importedInternetGraph.getEdges()) {
			internetGraph.addEdge(edge);
		}

		internetGraph.updateNodesGeolocationDatas(geolocationService);
	}

	public List<IPAddress> searchIPAddresses(String searchTerm) {
		return internetGraph.getNodes().stream().map(Node::getIpAddress) // Extract IPAddress from Node
				.filter(ip -> ip.getMatchScore(searchTerm) > 0) // Exclude entries with match score 0
				.sorted((ip1, ip2) -> Integer.compare(ip2.getMatchScore(searchTerm), ip1.getMatchScore(searchTerm)))
				.collect(Collectors.toList());
	}

	private final static int RANK_HEAD = 5;

	public Statistics getStatistics() {
		Statistics statistics = new Statistics();

		statistics.addStatistic(getRankingByMinimumDistanceToOriginStatistic());
		statistics.addStatistic(getNumberNodesWithGeolocationData());

		return statistics;
	}

	private MapStatistic<Double, String> getRankingByMinimumDistanceToOriginStatistic() {
		List<Path<String>> paths = new ArrayList<>();

		for (Node node : internetGraph.getNodes()) {
			paths.add(internetGraph.minimumDistance(origin.getIpAddress(), node.getIpAddress(), weightMetric, geolocationService.getClass()));
		}

		TreeMap<Double, String> rankingByMinimumDistanceToOrigin = new TreeMap<>();

		int max_rank = Math.min(paths.size(), RANK_HEAD);

		for (int rank = 0; rank < max_rank; ++rank) {
			Path<String> path = paths.get(rank);

			rankingByMinimumDistanceToOrigin.put(path.getCost(), path.getDestination());
		}

		return new MapStatistic<>("Ranking by minimum distance to origin", rankingByMinimumDistanceToOrigin);
	}

	private Statistic<Integer> getNumberNodesWithGeolocationData() {
		int numberNodesWithGeolocationData = 0;

		for (Node node : internetGraph.getNodes()) {
			if (node.getGeolocationData(geolocationService.getClass()).isPresent()) {
				++numberNodesWithGeolocationData;
			}
		}

		return new NumericStatistic<>("Number of nodes with geolocation data", numberNodesWithGeolocationData, "");
	}

}
