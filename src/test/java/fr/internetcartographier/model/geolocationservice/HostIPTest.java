package fr.internetcartographier.model.geolocationservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HostIPTest {

	private GeolocationService hostIP;

	@BeforeEach
	public void setUp() throws Exception {
		hostIP = HostIP.getInstance();
	}

	@Test
	public void testGetName() {
		assertEquals("HostIP", hostIP.getName());
	}

	@Test
	public void testGetGeolocationData() {
		Optional<GeolocationData> geolocationDataOptional = hostIP.getGeolocationData(new IPAddress("18.45.75.65"));
		assertTrue(geolocationDataOptional.toString().equals(
				"Optional[GeolocationData [latitude=42.3758, longitude=-71.1187, country=UNITED STATES, city=Cambridge, MA]]"));
		geolocationDataOptional = hostIP.getGeolocationData(new IPAddress("122.215.42.19"));
		assertTrue(geolocationDataOptional.isEmpty());
	}

}
