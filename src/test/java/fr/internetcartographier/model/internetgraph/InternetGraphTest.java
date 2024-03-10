package fr.internetcartographier.model.internetgraph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.internetcartographier.model.WeightMetric;
import fr.internetcartographier.model.geolocationservice.GeoIP2;
import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.geolocationservice.HostIP;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import fr.internetcartographier.util.path.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InternetGraphTest {
	private InternetGraph internetGraph;
	private GeolocationService geoIP2;
	private GeolocationService hostIP;

	@BeforeEach
	void setUp() throws Exception {
		internetGraph = new InternetGraph();
		geoIP2 = GeoIP2.getInstance();
		hostIP = HostIP.getInstance();
	}

	@Test
	void testAddNode() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));

		assertFalse(internetGraph.getNodes().contains(nodeA));
		assertFalse(internetGraph.getCache().containsNode(nodeA.getStringIpAddress()));
		assertFalse(internetGraph.getAdjacencyList().keySet().contains(nodeA.getStringIpAddress()));

		internetGraph.addNode(nodeA);

		assertTrue(internetGraph.getNodes().contains(nodeA));
		assertTrue(internetGraph.getCache().containsNode(nodeA.getStringIpAddress()));
		assertTrue(internetGraph.getAdjacencyList().keySet().contains(nodeA.getStringIpAddress()));
	}

	@Test
	void testRemoveNode() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		internetGraph.addNode(nodeA);

		internetGraph.removeNode(nodeA.getIpAddress());
		assertFalse(internetGraph.getNodes().contains(nodeA));
		assertFalse(internetGraph.getCache().containsNode(nodeA.getStringIpAddress()));
		assertFalse(internetGraph.getAdjacencyList().keySet().contains(nodeA.getStringIpAddress()));
	}

	@Test
	void testAddEdge() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		Node nodeB = new Node(new IPAddress("180.68.175.165"));
		Edge edge = new Edge(nodeA, nodeB, true, 10);
		assertThrows(IllegalArgumentException.class, () -> internetGraph.addEdge(edge));

		internetGraph.addNode(nodeA);
		internetGraph.addNode(nodeB);
		internetGraph.addEdge(edge);

		assertTrue(internetGraph.getAdjacencyList().get(nodeA.getStringIpAddress()).contains(edge));
		assertTrue(internetGraph.getAdjacencyList().get(nodeB.getStringIpAddress()).contains(edge));
	}

	@Test
	void testRemoveEdge() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		Node nodeB = new Node(new IPAddress("180.68.175.165"));
		Edge edge = new Edge(nodeA, nodeB, true, 10);

		internetGraph.addNode(nodeA);
		internetGraph.addNode(nodeB);
		internetGraph.addEdge(edge);

		internetGraph.removeEdge(nodeA.getIpAddress(), nodeB.getIpAddress());

		assertFalse(internetGraph.getAdjacencyList().get(nodeA.getStringIpAddress()).contains(edge));
		assertFalse(internetGraph.getAdjacencyList().get(nodeB.getStringIpAddress()).contains(edge));
	}

	@Test
	void testGetNode() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		assertTrue(internetGraph.getNode(nodeA.getIpAddress()).isEmpty());

		internetGraph.addNode(nodeA);
		Optional<Node> retrievedNode = internetGraph.getNode(nodeA.getIpAddress());

		assertTrue(retrievedNode.isPresent());
		assertEquals(nodeA, retrievedNode.get());

		retrievedNode = internetGraph.getNode(nodeA.getIpAddress().getStringIpAddress());
		assertTrue(retrievedNode.isPresent());
		assertEquals(nodeA, retrievedNode.get());
	}

	@Test
	void testGetEdge() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		Node nodeB = new Node(new IPAddress("180.68.175.165"));
		Edge edge = new Edge(nodeA, nodeB, true, 10);

		assertThrows(IllegalArgumentException.class,
				() -> internetGraph.getEdge(nodeA.getIpAddress(), nodeB.getIpAddress()));
		assertThrows(IllegalArgumentException.class,
				() -> internetGraph.getEdge(nodeB.getIpAddress(), nodeA.getIpAddress()));

		internetGraph.addNode(nodeA);
		internetGraph.addNode(nodeB);
		internetGraph.addEdge(edge);

		Optional<Edge> retrievedEdge = internetGraph.getEdge(nodeA.getIpAddress(), nodeB.getIpAddress());
		assertTrue(retrievedEdge.isPresent());
		assertEquals(edge, retrievedEdge.get());

		retrievedEdge = internetGraph.getEdge(nodeB.getIpAddress(), nodeA.getIpAddress());
		assertTrue(retrievedEdge.isPresent());
		assertEquals(edge, retrievedEdge.get());
	}

	@Test
	void testGetEdges() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		Node nodeB = new Node(new IPAddress("180.68.175.165"));
		Node nodeC = new Node(new IPAddress("129.69.1.170"));

		Edge edgeAB = new Edge(nodeA, nodeB, true, 10);
		Edge edgeBC = new Edge(nodeB, nodeC, true, 15);
		Edge edgeBA = new Edge(nodeB, nodeA, true, 10);
		Edge edgeCB = new Edge(nodeC, nodeB, true, 15);

		internetGraph.addNode(nodeA);
		internetGraph.addNode(nodeB);
		internetGraph.addNode(nodeC);
		internetGraph.addEdge(edgeAB);
		internetGraph.addEdge(edgeBC);

		Set<Edge> retrievedEdges = internetGraph.getEdges();

		assertEquals(2, retrievedEdges.size());
		assertTrue(retrievedEdges.contains(edgeAB));
		assertTrue(retrievedEdges.contains(edgeBC));
		assertTrue(retrievedEdges.contains(edgeBA));
		assertTrue(retrievedEdges.contains(edgeCB));
	}

	@Test
	void testGetEdgesByNode() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		Node nodeB = new Node(new IPAddress("180.68.175.165"));
		Node nodeC = new Node(new IPAddress("129.69.1.170"));

		Edge edgeAB = new Edge(nodeA, nodeB, true, 10);
		Edge edgeBC = new Edge(nodeB, nodeC, true, 15);

		internetGraph.addNode(nodeA);
		internetGraph.addNode(nodeB);
		internetGraph.addNode(nodeC);
		internetGraph.addEdge(edgeAB);
		internetGraph.addEdge(edgeBC);

		Set<Edge> retrievedEdges = internetGraph.getEdges(nodeA);
		assertEquals(1, retrievedEdges.size());
		assertTrue(retrievedEdges.contains(edgeAB));

		retrievedEdges = internetGraph.getEdges(nodeB);
		assertEquals(2, retrievedEdges.size());
		assertTrue(retrievedEdges.contains(edgeAB));
		assertTrue(retrievedEdges.contains(edgeBC));
	}

	@Test
	void testGetNeighbors() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		Node nodeB = new Node(new IPAddress("180.68.175.165"));
		Node nodeC = new Node(new IPAddress("129.69.1.170"));

		Edge edgeAB = new Edge(nodeA, nodeB, true, 10);
		Edge edgeBC = new Edge(nodeB, nodeC, true, 15);

		internetGraph.addNode(nodeA);
		internetGraph.addNode(nodeB);
		internetGraph.addNode(nodeC);
		internetGraph.addEdge(edgeAB);
		internetGraph.addEdge(edgeBC);

		Set<Node> neighbors = internetGraph.getNeighbors(nodeB);
		assertEquals(2, neighbors.size());
		assertTrue(neighbors.contains(nodeA));
		assertTrue(neighbors.contains(nodeC));

		neighbors = internetGraph.getNeighbors(nodeA);
		assertEquals(1, neighbors.size());
		assertTrue(neighbors.contains(nodeB));
	}

	@Test
	void testMinimumDistance() {
		IPAddress ipAddress0 = new IPAddress("18.45.75.65");
		Optional<GeolocationData> geoData0 = geoIP2.getGeolocationData(ipAddress0);
		Node node0 = new Node(ipAddress0, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData0));
		internetGraph.addNode(node0);

		IPAddress ipAddress1 = new IPAddress("180.68.175.165");
		Optional<GeolocationData> geoData1 = geoIP2.getGeolocationData(ipAddress1);
		Node node1 = new Node(ipAddress1, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData1));
		internetGraph.addNode(node1);

		IPAddress ipAddress2 = new IPAddress("129.69.1.170");
		Optional<GeolocationData> geoData2 = geoIP2.getGeolocationData(ipAddress2);
		Node node2 = new Node(ipAddress2, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData2));
		internetGraph.addNode(node2);

		IPAddress ipAddress3 = new IPAddress("134.69.1.170");
		Optional<GeolocationData> geoData3 = geoIP2.getGeolocationData(ipAddress3);
		Node node3 = new Node(ipAddress3, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData3));
		internetGraph.addNode(node3);

		IPAddress ipAddress4 = new IPAddress("137.69.1.170");
		Optional<GeolocationData> geoData4 = geoIP2.getGeolocationData(ipAddress4);
		Node node4 = new Node(ipAddress4, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData4));
		internetGraph.addNode(node4);

		IPAddress ipAddress5 = new IPAddress("141.69.1.170");
		Optional<GeolocationData> geoData5 = geoIP2.getGeolocationData(ipAddress5);
		Node node5 = new Node(ipAddress5, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData5));
		internetGraph.addNode(node5);

		IPAddress ipAddress6 = new IPAddress("145.69.1.170");
		Optional<GeolocationData> geoData6 = geoIP2.getGeolocationData(ipAddress6);
		Node node6 = new Node(ipAddress6, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData6));
		internetGraph.addNode(node6);

		IPAddress ipAddress7 = new IPAddress("160.69.1.170");
		Optional<GeolocationData> geoData7 = geoIP2.getGeolocationData(ipAddress7);
		Node node7 = new Node(ipAddress7, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData7));
		internetGraph.addNode(node7);

		IPAddress ipAddress8 = new IPAddress("165.69.1.170");
		Optional<GeolocationData> geoData8 = geoIP2.getGeolocationData(ipAddress8);
		Node node8 = new Node(ipAddress8, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData8));
		internetGraph.addNode(node8);

		IPAddress ipAddress9 = new IPAddress("165.69.1.171");
		Optional<GeolocationData> geoData9 = geoIP2.getGeolocationData(ipAddress8);
		Node node9 = new Node(ipAddress9, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData9));
		internetGraph.addNode(node9);

		internetGraph.addEdge(new Edge(node1, node0, true, 3));
		internetGraph.addEdge(new Edge(node1, node2, true, 7));
		internetGraph.addEdge(new Edge(node1, node7, true, 10));
		internetGraph.addEdge(new Edge(node1, node8, true, 4));

		internetGraph.addEdge(new Edge(node2, node1, true, 7));
		internetGraph.addEdge(new Edge(node2, node3, true, 6));
		internetGraph.addEdge(new Edge(node2, node5, true, 2));
		internetGraph.addEdge(new Edge(node2, node8, true, 1));

		internetGraph.addEdge(new Edge(node3, node2, true, 6));
		internetGraph.addEdge(new Edge(node3, node4, true, 8));
		internetGraph.addEdge(new Edge(node3, node5, true, 13));
		internetGraph.addEdge(new Edge(node3, node8, true, 3));

		internetGraph.addEdge(new Edge(node4, node3, true, 8));
		internetGraph.addEdge(new Edge(node4, node5, true, 9));

		internetGraph.addEdge(new Edge(node5, node2, true, 2));
		internetGraph.addEdge(new Edge(node5, node3, true, 13));
		internetGraph.addEdge(new Edge(node5, node4, true, 9));
		internetGraph.addEdge(new Edge(node5, node6, true, 4));
		internetGraph.addEdge(new Edge(node5, node8, true, 5));

		internetGraph.addEdge(new Edge(node6, node5, true, 4));
		internetGraph.addEdge(new Edge(node6, node7, true, 2));
		internetGraph.addEdge(new Edge(node6, node8, true, 5));

		internetGraph.addEdge(new Edge(node7, node0, true, 7));
		internetGraph.addEdge(new Edge(node7, node1, true, 10));
		internetGraph.addEdge(new Edge(node7, node6, true, 2));

		internetGraph.addEdge(new Edge(node8, node1, true, 4));
		internetGraph.addEdge(new Edge(node8, node2, true, 1));
		internetGraph.addEdge(new Edge(node8, node3, true, 3));
		internetGraph.addEdge(new Edge(node8, node5, true, 5));
		internetGraph.addEdge(new Edge(node8, node6, true, 5));
		internetGraph.addEdge(new Edge(node8, node7, true, 6));

		assertTrue(internetGraph.getCache().getMinPath(WeightMetric.CONSTANT, HostIP.class.getCanonicalName(),
				ipAddress0.getStringIpAddress(), ipAddress3.getStringIpAddress()).isEmpty());
		Path<String> path1 = internetGraph.minimumDistance(ipAddress0, ipAddress3, WeightMetric.CONSTANT,
				hostIP.getClass());
		assertEquals(3, path1.getCost());
		assertEquals(4, path1.getElements().size());
		Path<String> path1Cache = internetGraph.getCache().getMinPath(WeightMetric.CONSTANT,
				hostIP.getClass().getCanonicalName(), ipAddress0.getStringIpAddress(), ipAddress3.getStringIpAddress())
				.get();
		Path<String> path1ReversedCache = internetGraph.getCache().getMinPath(WeightMetric.CONSTANT,
				hostIP.getClass().getCanonicalName(), ipAddress3.getStringIpAddress(), ipAddress0.getStringIpAddress())
				.get();
		assertEquals(3, path1Cache.getCost());
		assertEquals(4, path1Cache.getElements().size());
		assertEquals(3, path1ReversedCache.getCost());
		assertEquals(4, path1ReversedCache.getElements().size());

		assertTrue(internetGraph.getCache().getMinPath(WeightMetric.CONSTANT, GeoIP2.class.getCanonicalName(),
				ipAddress0.getStringIpAddress(), ipAddress4.getStringIpAddress()).isEmpty());
		Path<String> path2 = internetGraph.minimumDistance(ipAddress0, ipAddress4, WeightMetric.CONSTANT, GeoIP2.class);
		assertEquals(4, path2.getCost());
		assertEquals(5, path2.getElements().size());
		Path<String> path2Cache = internetGraph.getCache().getMinPath(WeightMetric.CONSTANT,
				GeoIP2.class.getCanonicalName(), ipAddress0.getStringIpAddress(), ipAddress4.getStringIpAddress())
				.get();
		Path<String> path2ReversedCache = internetGraph.getCache().getMinPath(WeightMetric.CONSTANT,
				GeoIP2.class.getCanonicalName(), ipAddress4.getStringIpAddress(), ipAddress0.getStringIpAddress())
				.get();
		assertEquals(4, path2Cache.getCost());
		assertEquals(5, path2Cache.getElements().size());
		assertEquals(4, path2ReversedCache.getCost());
		assertEquals(5, path2ReversedCache.getElements().size());

		assertTrue(internetGraph.getCache().getMinPath(WeightMetric.RESPONSE_TIME, GeoIP2.class.getCanonicalName(),
				ipAddress0.getStringIpAddress(), ipAddress4.getStringIpAddress()).isEmpty());
		Path<String> path3 = internetGraph.minimumDistance(ipAddress0, ipAddress4, WeightMetric.RESPONSE_TIME,
				GeoIP2.class);
		Path<String> testPath3 = new Path<>(Arrays.asList(ipAddress0.getStringIpAddress(),
				ipAddress1.getStringIpAddress(), ipAddress8.getStringIpAddress(), ipAddress3.getStringIpAddress(),
				ipAddress4.getStringIpAddress()), 18.0);
		Path<String> testPath3Reversed = new Path<>(Arrays.asList(ipAddress4.getStringIpAddress(),
				ipAddress3.getStringIpAddress(), ipAddress8.getStringIpAddress(), ipAddress1.getStringIpAddress(),
				ipAddress0.getStringIpAddress()), 18.0);
		assertEquals(testPath3, path3);
		Path<String> path3Cache = internetGraph.getCache().getMinPath(WeightMetric.RESPONSE_TIME,
				GeoIP2.class.getCanonicalName(), ipAddress0.getStringIpAddress(), ipAddress4.getStringIpAddress())
				.get();
		Path<String> path3ReversedCache = internetGraph.getCache().getMinPath(WeightMetric.RESPONSE_TIME,
				GeoIP2.class.getCanonicalName(), ipAddress4.getStringIpAddress(), ipAddress0.getStringIpAddress())
				.get();
		Path<String> pathCache01 = internetGraph.getCache().getMinPath(WeightMetric.RESPONSE_TIME,
				GeoIP2.class.getCanonicalName(), ipAddress0.getStringIpAddress(), ipAddress1.getStringIpAddress())
				.get();
		Path<String> pathCache10 = internetGraph.getCache().getMinPath(WeightMetric.RESPONSE_TIME,
				GeoIP2.class.getCanonicalName(), ipAddress1.getStringIpAddress(), ipAddress0.getStringIpAddress())
				.get();
		assertEquals(testPath3, path3Cache);
		assertEquals(testPath3Reversed, path3ReversedCache);
		assertEquals(new Path<>(Arrays.asList(ipAddress0.getStringIpAddress(), ipAddress1.getStringIpAddress()), 3.0),
				pathCache01);
		assertEquals(new Path<>(Arrays.asList(ipAddress1.getStringIpAddress(), ipAddress0.getStringIpAddress()), 3.0),
				pathCache10);

		Path<String> path5 = internetGraph.minimumDistance(ipAddress0, ipAddress9, WeightMetric.RESPONSE_TIME,
				GeoIP2.class);
		assertEquals(new Path<>(Arrays.asList(ipAddress0.getStringIpAddress(), ipAddress9.getStringIpAddress()),
				Double.MAX_VALUE), path5);
		Path<String> path5Cache = internetGraph.getCache().getMinPath(WeightMetric.RESPONSE_TIME,
				GeoIP2.class.getCanonicalName(), ipAddress0.getStringIpAddress(), ipAddress9.getStringIpAddress())
				.get();
		Path<String> path5ReversedCache = internetGraph.getCache().getMinPath(WeightMetric.RESPONSE_TIME,
				GeoIP2.class.getCanonicalName(), ipAddress9.getStringIpAddress(), ipAddress0.getStringIpAddress())
				.get();
		assertEquals(new Path<>(Arrays.asList(ipAddress0.getStringIpAddress(), ipAddress9.getStringIpAddress()),
				Double.MAX_VALUE), path5Cache);
		assertEquals(new Path<>(Arrays.asList(ipAddress9.getStringIpAddress(), ipAddress0.getStringIpAddress()),
				Double.MAX_VALUE), path5ReversedCache);

		Path<String> path6 = internetGraph.minimumDistance(ipAddress0, ipAddress0, WeightMetric.RESPONSE_TIME,
				GeoIP2.class);
		Path<String> path6Cache = internetGraph.getCache().getMinPath(WeightMetric.RESPONSE_TIME,
				GeoIP2.class.getCanonicalName(), ipAddress0.getStringIpAddress(), ipAddress0.getStringIpAddress())
				.get();
		assertEquals(new Path<>(Arrays.asList(ipAddress0.getStringIpAddress()), 0), path6);
		assertEquals(new Path<>(Arrays.asList(ipAddress0.getStringIpAddress()), 0), path6Cache);

		Path<String> path7 = internetGraph.minimumDistance(ipAddress0, ipAddress0, WeightMetric.CONSTANT, GeoIP2.class);
		Path<String> path7Cache = internetGraph.getCache().getMinPath(WeightMetric.RESPONSE_TIME,
				GeoIP2.class.getCanonicalName(), ipAddress0.getStringIpAddress(), ipAddress0.getStringIpAddress())
				.get();
		assertEquals(new Path<>(Arrays.asList(ipAddress0.getStringIpAddress()), 0), path7);
		assertEquals(new Path<>(Arrays.asList(ipAddress0.getStringIpAddress()), 0), path7Cache);

		internetGraph.addEdge(new Edge(node0, node8, true, 5.0));
		assertTrue(internetGraph.getCache().getMinPath(WeightMetric.RESPONSE_TIME, GeoIP2.class.getCanonicalName(),
				ipAddress0.getStringIpAddress(), ipAddress1.getStringIpAddress()).isEmpty());
		assertTrue(internetGraph.getCache().getMinPath(WeightMetric.CONSTANT, GeoIP2.class.getCanonicalName(),
				ipAddress0.getStringIpAddress(), ipAddress4.getStringIpAddress()).isEmpty());

	}

	@Test
	void testUpdateNodesGeolocationDatas() {
		IPAddress ipAddress0 = new IPAddress("18.45.75.65");
		Optional<GeolocationData> geoData0 = geoIP2.getGeolocationData(ipAddress0);
		Node node0 = new Node(ipAddress0, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData0));

		internetGraph.addNode(node0);

		IPAddress ipAddress1 = new IPAddress("180.68.175.165");
		Optional<GeolocationData> geoData1 = geoIP2.getGeolocationData(ipAddress1);
		Node node1 = new Node(ipAddress1, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData1));
		internetGraph.addNode(node1);

		IPAddress ipAddress2 = new IPAddress("129.69.1.170");
		Optional<GeolocationData> geoData2 = geoIP2.getGeolocationData(ipAddress2);
		Node node2 = new Node(ipAddress2, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData2));
		internetGraph.addNode(node2);

		Edge edge1 = new Edge(node0, node1, true, 1.0);
		Edge edge2 = new Edge(node1, node2, true, 1.0);
		double weightEdge1GeoIP2 = edge1.getWeight(WeightMetric.DISTANCE, geoIP2.getClass());
		double weightEdge2GeoIP2 = edge2.getWeight(WeightMetric.DISTANCE, geoIP2.getClass());
		double weightEdge1HostIP = edge1.getWeight(WeightMetric.DISTANCE, hostIP.getClass());
		double weightEdge2HostIP = edge2.getWeight(WeightMetric.DISTANCE, hostIP.getClass());
		assertNotEquals(weightEdge1GeoIP2, Double.MAX_VALUE);
		assertNotEquals(weightEdge2GeoIP2, Double.MAX_VALUE);
		assertEquals(weightEdge1HostIP, Double.MAX_VALUE);
		assertEquals(weightEdge2HostIP, Double.MAX_VALUE);

		internetGraph.addEdge(edge1);
		internetGraph.addEdge(edge2);

		assertTrue(node0.getGeolocationData(geoIP2.getClass()).isPresent());
		assertTrue(node1.getGeolocationData(geoIP2.getClass()).isPresent());
		assertTrue(node2.getGeolocationData(geoIP2.getClass()).isPresent());
		assertTrue(node0.getGeolocationData(hostIP.getClass()).isEmpty());
		assertTrue(node1.getGeolocationData(hostIP.getClass()).isEmpty());
		assertTrue(node2.getGeolocationData(hostIP.getClass()).isEmpty());

		assertEquals(
				internetGraph.getEdge(ipAddress0, ipAddress1).get().getWeight(WeightMetric.DISTANCE, geoIP2.getClass()),
				weightEdge1GeoIP2);
		assertEquals(
				internetGraph.getEdge(ipAddress1, ipAddress2).get().getWeight(WeightMetric.DISTANCE, geoIP2.getClass()),
				weightEdge2GeoIP2);
		assertEquals(
				internetGraph.getEdge(ipAddress0, ipAddress1).get().getWeight(WeightMetric.DISTANCE, hostIP.getClass()),
				weightEdge1HostIP);
		assertEquals(
				internetGraph.getEdge(ipAddress1, ipAddress2).get().getWeight(WeightMetric.DISTANCE, hostIP.getClass()),
				weightEdge2HostIP);

		assertEquals(internetGraph.getNode(ipAddress0).get().getGeolocationData(geoIP2.getClass()), geoData0);
		assertEquals(internetGraph.getNode(ipAddress1).get().getGeolocationData(geoIP2.getClass()), geoData1);
		assertEquals(internetGraph.getNode(ipAddress2).get().getGeolocationData(geoIP2.getClass()), geoData2);
		assertTrue(internetGraph.getNode(ipAddress0).get().getGeolocationData(hostIP.getClass()).isEmpty());
		assertTrue(internetGraph.getNode(ipAddress1).get().getGeolocationData(hostIP.getClass()).isEmpty());
		assertTrue(internetGraph.getNode(ipAddress2).get().getGeolocationData(hostIP.getClass()).isEmpty());

		assertTrue(internetGraph.getCache().getMinPath(WeightMetric.DISTANCE, geoIP2.getClass().getCanonicalName(),
				ipAddress1.getStringIpAddress(), ipAddress2.getStringIpAddress()).isEmpty());
		internetGraph.minimumDistance(ipAddress1, ipAddress2, WeightMetric.DISTANCE, geoIP2.getClass());
		assertTrue(internetGraph.getCache().getMinPath(WeightMetric.DISTANCE, geoIP2.getClass().getCanonicalName(),
				ipAddress1.getStringIpAddress(), ipAddress2.getStringIpAddress()).isPresent());

		internetGraph.updateNodesGeolocationDatas(hostIP);
		assertEquals(
				internetGraph.getEdge(ipAddress0, ipAddress1).get().getWeight(WeightMetric.DISTANCE, geoIP2.getClass()),
				weightEdge1GeoIP2);
		assertEquals(
				internetGraph.getEdge(ipAddress1, ipAddress2).get().getWeight(WeightMetric.DISTANCE, geoIP2.getClass()),
				weightEdge2GeoIP2);
		assertNotEquals(
				internetGraph.getEdge(ipAddress0, ipAddress1).get().getWeight(WeightMetric.DISTANCE, hostIP.getClass()),
				weightEdge1HostIP);
		assertNotEquals(
				internetGraph.getEdge(ipAddress1, ipAddress2).get().getWeight(WeightMetric.DISTANCE, hostIP.getClass()),
				weightEdge2HostIP);

		assertEquals(internetGraph.getNode(ipAddress0).get().getGeolocationData(geoIP2.getClass()), geoData0);
		assertEquals(internetGraph.getNode(ipAddress1).get().getGeolocationData(geoIP2.getClass()), geoData1);
		assertEquals(internetGraph.getNode(ipAddress2).get().getGeolocationData(geoIP2.getClass()), geoData2);
		assertTrue(internetGraph.getNode(ipAddress0).get().getGeolocationData(hostIP.getClass()).isPresent());
		assertTrue(internetGraph.getNode(ipAddress1).get().getGeolocationData(hostIP.getClass()).isPresent());
		assertTrue(internetGraph.getNode(ipAddress2).get().getGeolocationData(hostIP.getClass()).isPresent());

		assertTrue(internetGraph.getCache().getMinPath(WeightMetric.DISTANCE, geoIP2.getClass().getCanonicalName(),
				ipAddress1.getStringIpAddress(), ipAddress2.getStringIpAddress()).isEmpty());
	}
}
