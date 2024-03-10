package fr.internetcartographier.model.geolocationservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code IPAddress} class represents an IP address and provides utility
 * methods for working with IP addresses.
 */
public class IPAddress {

	/**
	 * The IP address in string format.
	 */
	private final String ipAddress;

	/**
	 * Optional domain name associated with the IP address.
	 */
	private Optional<String> domainName;

	/**
	 * Constructs an {@code IPAddress} object with the specified IP address.
	 * Validates the IP address format.
	 *
	 * @param  ipAddress                The IP address in string format.
	 * @throws IllegalArgumentException If the provided IP address is not valid.
	 */
	public IPAddress(String ipAddress) {
		if (!isValidIPAddress(ipAddress)) {
			throw new IllegalArgumentException("Invalid IP address format");
		}

		this.ipAddress = ipAddress;
		domainName = Optional.empty();
	}

	/**
	 * Constructs an {@code IPAddress} object by copying another {@code IPAddress}
	 * object.
	 *
	 * @param newIpAddress The IPAddress object to copy.
	 */
	public IPAddress(IPAddress newIpAddress) {
		ipAddress = newIpAddress.ipAddress;
		domainName = newIpAddress.domainName;
	}

	/**
	 * Constructs an {@code IPAddress} object with the specified IP address and
	 * domain name.
	 *
	 * @param ipAddress  The IP address in string format.
	 * @param domainName Optional domain name associated with the IP address.
	 */
	public IPAddress(String ipAddress, String domainName) {
		this(ipAddress);
		this.domainName = Optional.of(domainName);
	}

	/**
	 * Gets the IP address in string format.
	 *
	 * @return The IP address.
	 */
	public String getStringIpAddress() {
		return ipAddress;
	}

	/**
	 * Gets the optional domain name associated with the IP address.
	 *
	 * @return Optional domain name.
	 */
	public Optional<String> getDomainName() {
		return domainName;
	}

	/**
	 * Computes the hash code for this object based on its IP address.
	 *
	 * @return The hash code.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(ipAddress);
	}

	/**
	 * Checks if this object is equal to another object based on its IP address.
	 *
	 * @param  obj The object to compare.
	 * @return     {@code true} if the objects are equal, {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		IPAddress other = (IPAddress) obj;

		return Objects.equals(ipAddress, other.ipAddress);
	}

	/**
	 * Regular expression to match a valid IPv4 address.
	 */
	private static final String IPV4_REGEX = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
			+ "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
			+ "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

	/**
	 * List of reserved IP address ranges.
	 */
	private static final String[] RESERVED_IP_RANGES = { "0.0.0.0", "10.0.0.0/8", "127.0.0.0/8", "169.254.0.0/16",
			"172.16.0.0/12", "192.0.2.0/24", "192.88.99.0/24", "192.168.0.0/16", "198.18.0.0/15", "198.51.100.0/24",
			"203.0.113.0/24", "224.0.0.0/4", "240.0.0.0/4" };

	/**
	 * Checks if the provided IP address is valid and not in a reserved range.
	 *
	 * @param  ipAddress The IP address to validate.
	 * @return           {@code true} if the IP address is valid and not reserved,
	 *                   {@code false} otherwise.
	 */
	public static boolean isValidIPAddress(String ipAddress) {
		// Check if the IP address conforms to the valid format
		Pattern pattern = Pattern.compile(IPV4_REGEX);
		Matcher matcher = pattern.matcher(ipAddress);

		if (!matcher.matches()) {
			return false; // Invalid format
		}

		// Check if the IP address is reserved
		for (String reservedRange : RESERVED_IP_RANGES) {
			if (isIPInCIDRRange(ipAddress, reservedRange)) {
				return false; // Reserved IP address
			}
		}

		return true; // Valid IP address
	}

	/**
	 * Checks if the provided IP address is in a CIDR range.
	 *
	 * @param  ipAddress The IP address to check.
	 * @param  cidrRange The CIDR range to compare against.
	 * @return           {@code true} if the IP address is in the CIDR range,
	 *                   {@code false} otherwise.
	 */
	private static boolean isIPInCIDRRange(String ipAddress, String cidrRange) {
		String[] parts = cidrRange.split("/");
		String baseAddress = parts[0];
		int subnetLength = 32; // Default to /32 if no subnet length is specified

		if (parts.length == 2) {
			subnetLength = Integer.parseInt(parts[1]);
		}

		String[] ipAddressParts = ipAddress.split("\\.");
		String[] baseAddressParts = baseAddress.split("\\.");

		for (int i = 0; i < subnetLength / 8; i++) {
			if (!ipAddressParts[i].equals(baseAddressParts[i])) {
				return false;
			}
		}

		int bitsToCheck = subnetLength % 8;
		if (bitsToCheck > 0) {
			int mask = 0xFF << 8 - bitsToCheck;
			int ipSegment = Integer.parseInt(ipAddressParts[subnetLength / 8]);
			int baseSegment = Integer.parseInt(baseAddressParts[subnetLength / 8]);
			if ((ipSegment & mask) != (baseSegment & mask)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return The string representation of the object.
	 */
	@Override
	public String toString() {
		return "IPAddress [ipAddress=" + ipAddress + ", domainName=" + domainName + "]";
	}

	/**
	 * Generates a random valid IP address.
	 *
	 * @return A randomly generated IP address.
	 */
	public static IPAddress generateRandomIPAddress() {
		Random random = new Random();
		StringBuilder ipAddressBuilder = new StringBuilder();

		for (int i = 0; i < 4; i++) {
			ipAddressBuilder.append(random.nextInt(256));
			if (i < 3) {
				ipAddressBuilder.append(".");
			}
		}

		String randomIPAddress = ipAddressBuilder.toString();
		if (isValidIPAddress(randomIPAddress)) {
			return new IPAddress(randomIPAddress);
		}
		return generateRandomIPAddress();
	}

	/**
	 * Generates a list of IP addresses within a specified range.
	 *
	 * @param  from The starting IP address.
	 * @param  to   The ending IP address.
	 * @return      A list of IP addresses within the specified range.
	 */
	public static List<IPAddress> generateIPAddressesInARange(IPAddress from, IPAddress to) {
		List<IPAddress> ipAddressList = new ArrayList<>();

		// Assuming from and to are valid IP addresses
		long fromInt = ipToLong(from.getStringIpAddress());
		long toInt = ipToLong(to.getStringIpAddress());

		// Generate IP addresses in the specified range
		for (long i = fromInt; i <= toInt; i++) {
			ipAddressList.add(new IPAddress(longToIP(i)));
		}

		return ipAddressList;
	}

	/**
	 * Converts an IP address in string format to a long integer.
	 *
	 * @param  ipAddress The IP address in string format.
	 * @return           The long integer representation of the IP address.
	 */	
	public static long ipToLong(String ipAddress) {
		String[] ipAddressParts = ipAddress.split("\\.");
		long result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) + Long.parseLong(ipAddressParts[i]);
		}
		return result;
	}

	/**
	 * Converts a long integer to an IP address in string format.
	 *
	 * @param  ip The long integer representation of the IP address.
	 * @return    The IP address in string format.
	 */
	public static String longToIP(long ip) {
		StringBuilder ipAddressBuilder = new StringBuilder();
		for (int i = 3; i >= 0; i--) {
			ipAddressBuilder.append(ip >> 8 * i & 0xFF);
			if (i > 0) {
				ipAddressBuilder.append(".");
			}
		}
		return ipAddressBuilder.toString();
	}

	public int getMatchScore(String searchTerm) {
		int ipAddressMatch = calculatePartialMatch(ipAddress, searchTerm);
		int domainNameMatch = domainName.map(dn -> calculatePartialMatch(dn, searchTerm)).orElse(0);
		return ipAddressMatch + domainNameMatch;
	}

	private int calculatePartialMatch(String original, String searchTerm) {
		int matchScore = 0;
		int searchLength = searchTerm.length();
		int originalLength = original.length();

		if (original.contains(searchTerm)) {
			matchScore += originalLength * originalLength / (originalLength - searchLength + 1);
		}

		return matchScore;
	}


}
