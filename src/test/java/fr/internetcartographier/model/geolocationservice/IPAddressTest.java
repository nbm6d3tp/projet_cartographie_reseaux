package fr.internetcartographier.model.geolocationservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class IPAddressTest {

	@Test
	public void testValidIPAddress() {
		String[] invalidIPAddresses = { "0.0.0.0", // Reserved for Default Route
				"10.0.0.0", // Private network
				"127.0.0.1", // Loopback address
				"169.254.0.0", // Link-local address
				"172.16.0.0", // Private network
				"192.0.2.0", // Reserved for documentation and example code
				"192.88.99.0", // 6to4 relay anycast
				"192.168.0.0", // Private network
				"198.18.0.0", // Benchmark testing
				"198.51.100.0", // Reserved for documentation and example code
				"203.0.113.0", // Reserved for documentation and example code
				"224.0.0.0", // Multicast
				"240.0.0.0", // Reserved for future use
				"256.256.256.256", // Invalid IP address
				"invalidIpAddress" // Invalid IP address format
		};

		for (String ipAddress : invalidIPAddresses) {
			assertFalse(IPAddress.isValidIPAddress(ipAddress));
		}
	}

	@Test
	public void testGenerateRandomIPAddress() {
		IPAddress randomIPAddress = IPAddress.generateRandomIPAddress();
		assertTrue(IPAddress.isValidIPAddress(randomIPAddress.getStringIpAddress()));
	}

	@Test
	public void testGenerateIPAddressesInARange() {
		IPAddress from = new IPAddress("18.45.75.65");
		IPAddress to = new IPAddress("18.45.75.69");

		List<IPAddress> ipAddressList = IPAddress.generateIPAddressesInARange(from, to);

		assertEquals(5, ipAddressList.size());
		assertEquals("18.45.75.65", ipAddressList.get(0).getStringIpAddress());
		assertEquals("18.45.75.66", ipAddressList.get(1).getStringIpAddress());
		assertEquals("18.45.75.67", ipAddressList.get(2).getStringIpAddress());
		assertEquals("18.45.75.68", ipAddressList.get(3).getStringIpAddress());
		assertEquals("18.45.75.69", ipAddressList.get(4).getStringIpAddress());
	}

	@Test
	public void testEqualsAndHashCode() {
		IPAddress ipAddress1 = new IPAddress("18.45.75.65");
		IPAddress ipAddress2 = new IPAddress("18.45.75.65");
		IPAddress ipAddress3 = new IPAddress("18.45.75.65", "google.com");
		IPAddress ipAddress4 = new IPAddress("18.0.0.1");

		// Test equals method
		assertTrue(ipAddress1.equals(ipAddress2));
		assertTrue(ipAddress1.equals(ipAddress3));
		assertFalse(ipAddress1.equals(ipAddress4));

		// Test hashCode
		assertEquals(ipAddress1.hashCode(), ipAddress2.hashCode());
		assertEquals(ipAddress1.hashCode(), ipAddress3.hashCode());
		assertNotEquals(ipAddress1.hashCode(), ipAddress4.hashCode());
	}

	@Test
	public void testToString() {
		IPAddress ipAddress = new IPAddress("18.45.75.65");
		assertEquals("IPAddress [ipAddress=18.45.75.65, domainName=Optional.empty]", ipAddress.toString());
	}

	@Test
	public void testInvalidIPAddressConstructor() {
		assertThrows(IllegalArgumentException.class, () -> new IPAddress("invalidIpAddress"));
		assertThrows(IllegalArgumentException.class, () -> new IPAddress("256.256.256.256"));
	}

	@Test
	public void testCopyConstructor() {
		IPAddress original = new IPAddress("18.45.75.65", "google.com");
		IPAddress copy = new IPAddress(original);

		assertEquals(original, copy);
		assertNotSame(original, copy);
	}

}
