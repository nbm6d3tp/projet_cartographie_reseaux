package fr.internetcartographier.model.internetgraph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.internetcartographier.model.geolocationservice.GeoIP2;
import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.geolocationservice.HostIP;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EdgeTest {
	private GeolocationService hostIP;
	private GeolocationService geoIP2;

	@BeforeEach
	public void setUp() throws Exception {
		hostIP = HostIP.getInstance();
		geoIP2 = GeoIP2.getInstance();
	}

	@Test
	public void testEdgeCreation() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		Node nodeB = new Node(new IPAddress("122.215.42.19"));

		Edge edge = new Edge(nodeA, nodeB, true, 10.0);

		assertEquals(nodeA, edge.getNodeA());
		assertEquals(nodeB, edge.getNodeB());
		assertTrue(edge.isDirect());
		assertEquals(10.0, edge.getResponseTime(), 0.001);

		Edge edge1 = new Edge(edge);
		assertEquals(nodeA, edge1.getNodeA());
		assertEquals(nodeB, edge1.getNodeB());
		assertTrue(edge1.isDirect());
		assertEquals(10.0, edge1.getResponseTime(), 0.001);
	}

	@Test
	public void testEdgeCreationWithNullNodes() {
		assertThrows(IllegalArgumentException.class, () -> new Edge(null, null, true, 10.0));
	}

	@Test
	public void testEdgeCreationWithSameNodes() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		Node nodeB = new Node(new IPAddress("18.45.75.65"));
		assertThrows(IllegalArgumentException.class, () -> new Edge(nodeA, nodeB, true, 10.0));
	}

	@Test
	public void testUpdateGeolocationData() {
		IPAddress ipAddressA = new IPAddress("18.45.75.65");
		IPAddress ipAddressB = new IPAddress("122.215.42.19");

		Optional<GeolocationData> geolocationDataGeoIP2A = geoIP2.getGeolocationData(ipAddressA);
		Optional<GeolocationData> geolocationDataGeoIP2B = geoIP2.getGeolocationData(ipAddressB);

		Node nodeA = new Node(ipAddressA,
				new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geolocationDataGeoIP2A));
		Node nodeB = new Node(ipAddressB,
				new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geolocationDataGeoIP2B));
		Edge edge = new Edge(nodeA, nodeB, true, 10.0);

		assertEquals(edge.getNodeA().getGeolocationData(GeoIP2.class), geolocationDataGeoIP2A);
		assertEquals(edge.getNodeB().getGeolocationData(GeoIP2.class), geolocationDataGeoIP2B);
		assertEquals(edge.getNodeA().getGeolocationData(HostIP.class), Optional.empty());
		assertEquals(edge.getNodeB().getGeolocationData(HostIP.class), Optional.empty());

		Optional<GeolocationData> geolocationDataHostIPA = hostIP.getGeolocationData(ipAddressA);
		Optional<GeolocationData> geolocationDataHostIPB = hostIP.getGeolocationData(ipAddressB);

		edge.updateGeolocationDataNodeA(
				new GeolocationData.TypedGeolocationData(hostIP.getClass(), geolocationDataHostIPA));
		edge.updateGeolocationDataNodeB(
				new GeolocationData.TypedGeolocationData(hostIP.getClass(), geolocationDataHostIPB));

		assertEquals(edge.getNodeA().getGeolocationData(HostIP.class), geolocationDataHostIPA);
		assertEquals(edge.getNodeB().getGeolocationData(HostIP.class), geolocationDataHostIPB);
		assertEquals(edge.getNodeA().getGeolocationData(GeoIP2.class), geolocationDataGeoIP2A);
		assertEquals(edge.getNodeB().getGeolocationData(GeoIP2.class), geolocationDataGeoIP2B);
	}

	@Test
	public void testIsContainNode() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		Node nodeB = new Node(new IPAddress("122.215.42.19"));
		Node nodeC = new Node(new IPAddress("141.69.1.170"));
		Edge edge = new Edge(nodeA, nodeB, true, 10.0);

		assertTrue(edge.isContainNode(nodeA));
		assertTrue(edge.isContainNode(nodeB));
		assertFalse(edge.isContainNode(nodeC));
	}

	@Test
	public void testIsContainIPAddress() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		Node nodeB = new Node(new IPAddress("122.215.42.19"));
		Edge edge = new Edge(nodeA, nodeB, true, 10.0);

		assertTrue(edge.isContainNode(new IPAddress("18.45.75.65")));
		assertTrue(edge.isContainNode(new IPAddress("122.215.42.19")));

		assertFalse(edge.isContainNode(new IPAddress("141.69.1.170")));
	}

	@Test
	public void testEqualsAndHashCode() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		Node nodeB = new Node(new IPAddress("122.215.42.19"));
		Edge edgeAB = new Edge(nodeA, nodeB, true, 10.0);
		Edge edgeBA = new Edge(nodeB, nodeA, true, 10.0);

		Edge edgeABnotDirect = new Edge(nodeA, nodeB, false, 10.0);
		Edge edgeAB20 = new Edge(nodeA, nodeB, false, 20.0);

		Node nodeC = new Node(new IPAddress("141.69.1.170"));
		Edge edgeAC = new Edge(nodeA, nodeC, true, 10.0);

		assertEquals(edgeAB, edgeBA);
		assertEquals(edgeAB, edgeABnotDirect);
		assertEquals(edgeAB, edgeAB20);
		assertNotEquals(edgeAB, edgeAC);

		assertEquals(edgeAB.hashCode(), edgeBA.hashCode());
		assertEquals(edgeAB.hashCode(), edgeABnotDirect.hashCode());
		assertEquals(edgeAB.hashCode(), edgeAB20.hashCode());
		assertNotEquals(edgeAB.hashCode(), edgeAC.hashCode());
	}

	@Test
	public void testToString() {
		Node nodeA = new Node(new IPAddress("18.45.75.65"));
		Node nodeB = new Node(new IPAddress("122.215.42.19"));
		Edge edge = new Edge(nodeA, nodeB, true, 10.0);

		assertNotNull(edge.toString());
	}

}
