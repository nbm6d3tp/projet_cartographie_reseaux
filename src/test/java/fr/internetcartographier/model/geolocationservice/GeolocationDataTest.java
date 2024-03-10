package fr.internetcartographier.model.geolocationservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class GeolocationDataTest {

	@Test
	public void testConstructorWithValidCoordinates() {
		GeolocationData geolocationData = new GeolocationData(37.7749, -122.4194, "USA", "San Francisco");

		assertEquals(37.7749, geolocationData.getLatitude(), 0.0001);
		assertEquals(-122.4194, geolocationData.getLongitude(), 0.0001);
		assertEquals("USA", geolocationData.getCountry());
		assertEquals("San Francisco", geolocationData.getCity());
	}

	@Test
	public void testConstructorWithInvalidArguments() {
		assertThrows(IllegalArgumentException.class, () -> new GeolocationData(200, 100, "USA", "San Francisco"));
		assertThrows(IllegalArgumentException.class,
				() -> new GeolocationData(37.7749, -122.4194, null, "San Francisco"));
		assertThrows(IllegalArgumentException.class, () -> new GeolocationData(37.7749, -122.4194, "USA", null));
	}

	@Test
	public void testCopyConstructor() {
		GeolocationData originalData = new GeolocationData(37.7749, -122.4194, "USA", "San Francisco");
		GeolocationData copiedData = new GeolocationData(originalData);

		assertEquals(originalData, copiedData);
		assertNotEquals(System.identityHashCode(originalData), System.identityHashCode(copiedData));
	}

	@Test
	public void testEqualsAndHashCode() {
		GeolocationData data1 = new GeolocationData(37.7749, -122.4194, "USA", "San Francisco");
		GeolocationData data2 = new GeolocationData(37.7749, -122.4194, "USA", "San Francisco");
		GeolocationData data3 = new GeolocationData(40.7128, -74.0060, "USA", "New York");

		// Test equals method
		assertTrue(data1.equals(data2));
		assertFalse(data1.equals(data3));

		// Test hashCode
		assertEquals(data1.hashCode(), data2.hashCode());
		assertNotEquals(data1.hashCode(), data3.hashCode());
	}

	@Test
	public void testToString() {
		GeolocationData geolocationData = new GeolocationData(37.7749, -122.4194, "USA", "San Francisco");
		assertNotNull(geolocationData.toString());
	}

}
