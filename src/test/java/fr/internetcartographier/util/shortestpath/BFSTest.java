package fr.internetcartographier.util.shortestpath;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fr.internetcartographier.model.geolocationservice.GeoIP2;
import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import fr.internetcartographier.model.internetgraph.Edge;
import fr.internetcartographier.model.internetgraph.InternetGraph;
import fr.internetcartographier.model.internetgraph.Node;
import fr.internetcartographier.util.path.Path;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BFSTest {
	private InternetGraph internetGraph;
	private GeolocationService geoIP2;

	@BeforeEach
	void setUp() throws Exception {
		geoIP2 = GeoIP2.getInstance();
		internetGraph = new InternetGraph();
	}

	@Test
	void testBFS() {
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

		assertEquals(new Path<>(Arrays.asList(ipAddress0.getStringIpAddress()), 0),
				BreadthFirstSearch.getMinPath(ipAddress0.getStringIpAddress(), ipAddress0.getStringIpAddress(), internetGraph));

		assertEquals(new Path<>(Arrays.asList(ipAddress0.getStringIpAddress(), ipAddress1.getStringIpAddress()), 1),
				BreadthFirstSearch.getMinPath(ipAddress0.getStringIpAddress(), ipAddress1.getStringIpAddress(), internetGraph));

		Path<String> path1 = BreadthFirstSearch.getMinPath(ipAddress0.getStringIpAddress(), ipAddress3.getStringIpAddress(),
				internetGraph);
		assertEquals(3, path1.getCost());
		assertEquals(4, path1.getElements().size());

		Path<String> path2 = BreadthFirstSearch.getMinPath(ipAddress0.getStringIpAddress(), ipAddress4.getStringIpAddress(),
				internetGraph);
		assertEquals(4, path2.getCost());
		assertEquals(5, path2.getElements().size());

		Path<String> path3 = BreadthFirstSearch.getMinPath(ipAddress8.getStringIpAddress(), ipAddress4.getStringIpAddress(),
				internetGraph);
		assertEquals(2, path3.getCost());
		assertEquals(3, path3.getElements().size());

		Path<String> path4 = BreadthFirstSearch.getMinPath(ipAddress2.getStringIpAddress(), ipAddress5.getStringIpAddress(),
				internetGraph);
		assertEquals(1, path4.getCost());
		assertEquals(2, path4.getElements().size());

		Path<String> path5 = BreadthFirstSearch.getMinPath(ipAddress2.getStringIpAddress(), ipAddress9.getStringIpAddress(),
				internetGraph);
		assertEquals(Double.MAX_VALUE, path5.getCost());
		assertEquals(2, path5.getElements().size());

		Path<String> path6 = BreadthFirstSearch.getMinPath(ipAddress2.getStringIpAddress(), ipAddress2.getStringIpAddress(),
				internetGraph);
		assertEquals(0, path6.getCost());
		assertEquals(1, path6.getElements().size());
	}
}
