package fr.internetcartographier.model.internetgraph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.internetcartographier.model.geolocationservice.GeoIP2;
import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.geolocationservice.HostIP;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NodeTest {
	private GeolocationService hostIP;
	private GeolocationService geoIP2;

	@BeforeEach
	public void setUp() throws Exception {
		hostIP = HostIP.getInstance();
		geoIP2 = GeoIP2.getInstance();
	}

	@Test
	public void testNodeCreation() {
		IPAddress ipAddress = new IPAddress("18.45.75.65");
		Optional<GeolocationData> geoDataGeoIP2 = geoIP2.getGeolocationData(ipAddress);
		Node node = new Node(ipAddress, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoDataGeoIP2));

		assertEquals(ipAddress, node.getIpAddress());
		assertTrue(node.getGeolocationData(GeoIP2.class).isPresent());
		assertFalse(node.getGeolocationData(HostIP.class).isPresent());
		assertEquals(geoDataGeoIP2.get(), node.getGeolocationData(GeoIP2.class).get());

		Node node1 = new Node(node);
		assertEquals(ipAddress, node1.getIpAddress());
		assertTrue(node1.getGeolocationData(GeoIP2.class).isPresent());
		assertFalse(node1.getGeolocationData(HostIP.class).isPresent());
		assertEquals(geoDataGeoIP2.get(), node1.getGeolocationData(GeoIP2.class).get());

		Optional<GeolocationData> geoDataHostIP = hostIP.getGeolocationData(ipAddress);
		Node node2 = new Node(ipAddress, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoDataGeoIP2),
				new GeolocationData.TypedGeolocationData(hostIP.getClass(), geoDataHostIP));

		assertTrue(node2.getGeolocationData(GeoIP2.class).isPresent());
		assertTrue(node2.getGeolocationData(HostIP.class).isPresent());
		assertEquals(geoDataGeoIP2.get(), node2.getGeolocationData(GeoIP2.class).get());
		assertEquals(geoDataHostIP.get(), node2.getGeolocationData(HostIP.class).get());
	}

	@Test
	public void testUpdateGeolocationData() {
		IPAddress ipAddress = new IPAddress("18.45.75.65");
		Optional<GeolocationData> geoDataGeoIP2 = geoIP2.getGeolocationData(ipAddress);
		Optional<GeolocationData> geoDataHostIP = hostIP.getGeolocationData(ipAddress);

		Node node = new Node(ipAddress, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoDataGeoIP2));

		assertTrue(node.getGeolocationData(GeoIP2.class).isPresent());
		assertFalse(node.getGeolocationData(HostIP.class).isPresent());
		assertEquals(geoDataGeoIP2.get(), node.getGeolocationData(GeoIP2.class).get());

		node.updateGeolocationData(new GeolocationData.TypedGeolocationData(hostIP.getClass(), geoDataHostIP));

		assertTrue(node.getGeolocationData(GeoIP2.class).isPresent());
		assertTrue(node.getGeolocationData(HostIP.class).isPresent());
		assertEquals(geoDataGeoIP2.get(), node.getGeolocationData(GeoIP2.class).get());
		assertEquals(geoDataHostIP.get(), node.getGeolocationData(HostIP.class).get());
	}

	@Test
	public void testEquality() {
		IPAddress ipAddress1 = new IPAddress("18.45.75.65");
		IPAddress ipAddress2 = new IPAddress("122.215.42.19");

		Node node1 = new Node(ipAddress1);
		Node node2 = new Node(ipAddress2);
		Node node3 = new Node(ipAddress1);

		assertEquals(node1, node3);
		assertNotEquals(node1, node2);
	}

	@Test
	public void testToString() {
		IPAddress ipAddress = new IPAddress("18.45.75.65");
		Optional<GeolocationData> geoData = geoIP2.getGeolocationData(ipAddress);
		Node node = new Node(ipAddress, new GeolocationData.TypedGeolocationData(geoIP2.getClass(), geoData));
		assertNotNull(node.toString());
	}
}
