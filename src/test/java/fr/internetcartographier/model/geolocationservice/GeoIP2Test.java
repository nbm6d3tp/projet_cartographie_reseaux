package fr.internetcartographier.model.geolocationservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeoIP2Test {

	private GeolocationService geoIP2;

	@BeforeEach
	public void setUp() throws Exception {
		geoIP2 = GeoIP2.getInstance();
	}

	@Test
	public void testGetName() {
		assertEquals("GeoIP2", geoIP2.getName());
	}

	@Test
	public void testGetGeolocationData() {
		String testIpAddress = "18.45.75.65";

		IPAddress ipAddress = new IPAddress(testIpAddress);
		Optional<GeolocationData> geolocationDataOptional = geoIP2.getGeolocationData(ipAddress);

		assertTrue(geolocationDataOptional.toString().equals(
				"Optional[GeolocationData [latitude=37.751, longitude=-97.822, country=United States, city=]]"));
	}
}
