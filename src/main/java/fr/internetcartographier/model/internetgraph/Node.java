package fr.internetcartographier.model.internetgraph;

import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.geolocationservice.GeolocationData.TypedGeolocationData;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The {@code Node} class represents a node in an internet graph with an IP
 * address
 * and associated geolocation data. It includes methods for updating geolocation
 * data, checking if a node contains geolocation data for a specific service,
 * and
 * providing a string representation of the node.
 */
public class Node {

	/**
	 * The IP address of the node.
	 */
	private final IPAddress ipAddress;

	/**
	 * A map to store geolocation data for various services.
	 */
	private Map<String, Optional<GeolocationData>> geolocationDatas;

	/**
	 * Constructs a {@code Node} object with the specified IP address and
	 * geolocation data.
	 *
	 * @param ipAddress        The IP address of the node.
	 * @param geolocationDatas The geolocation data associated with the node.
	 */
	public Node(IPAddress ipAddress, GeolocationData.TypedGeolocationData... geolocationDatas) {
		this.ipAddress = ipAddress;
		this.geolocationDatas = new HashMap<>();
		for (GeolocationData.TypedGeolocationData geolocationData : geolocationDatas) {
			this.geolocationDatas.put(geolocationData.getServiceGenerating().getCanonicalName(),
					geolocationData.getGeolocationData());
		}
	}

	/**
	 * Constructs a {@code Node} object by copying another {@code Node} object.
	 *
	 * @param node The Node object to copy.
	 */
	public Node(Node node) {
		ipAddress = node.ipAddress;
		geolocationDatas = new HashMap<>();
		for (Map.Entry<String, Optional<GeolocationData>> entry : node.geolocationDatas.entrySet()) {
			String key = entry.getKey();
			Optional<GeolocationData> val = entry.getValue();
			geolocationDatas.put(key, val);
		}
	}

	/**
	 * Gets the IP address of the node.
	 *
	 * @return The IP address.
	 */
	public IPAddress getIpAddress() {
		return new IPAddress(ipAddress);
	}

	/**
	 * Gets the string representation of the IP address.
	 *
	 * @return The string representation of the IP address.
	 */
	public String getStringIpAddress() {
		return ipAddress.getStringIpAddress();
	}

	/**
	 * Gets the geolocation data for a specific service.
	 *
	 * @param  geolocationService The geolocation service class.
	 * @return                    An optional containing the geolocation data if
	 *                            present, or empty otherwise.
	 */
	public Optional<GeolocationData> getGeolocationData(Class<? extends GeolocationService> geolocationService) {
		if (isNodeContainsAlreadyGeolocationData(geolocationService)) {
			return geolocationDatas.get(geolocationService.getCanonicalName());
		}

		return Optional.empty();
	}

	/**
	 * Checks if the cache of the node contains already geolocation data for a
	 * specific service.
	 *
	 * @param  geolocationService The geolocation service class.
	 * @return                    {@code true} if geolocation data is present,
	 *                            {@code false} otherwise.
	 */
	public boolean isNodeContainsAlreadyGeolocationData(Class<? extends GeolocationService> geolocationService) {
		return geolocationDatas.containsKey(geolocationService.getCanonicalName());
	}

	/**
	 * Updates the geolocation data for the node.
	 *
	 * @param geolocationData The geolocation data to update.
	 */
	public void updateGeolocationData(TypedGeolocationData geolocationData) {
		if (isNodeContainsAlreadyGeolocationData(geolocationData.getServiceGenerating())) {
			geolocationDatas.replace(geolocationData.getServiceGenerating().getCanonicalName(),
					geolocationData.getGeolocationData());
		} else {
			geolocationDatas.put(geolocationData.getServiceGenerating().getCanonicalName(),
					geolocationData.getGeolocationData());
		}
	}

	/**
	 * Computes the hash code for the node based on its IP address.
	 *
	 * @return The hash code.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(ipAddress);
	}

	/**
	 * Checks if this node is equal to another object.
	 *
	 * @param  obj The object to compare with.
	 * @return     {@code true} if the nodes are equal, {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Node other = (Node) obj;
		return Objects.equals(ipAddress, other.ipAddress);
	}

	/**
	 * Provides a string representation of the node, including its IP address and
	 * geolocation data.
	 *
	 * @return The string representation of the node.
	 */
	@Override
	public String toString() {
		String result = "ipAddress: " + ipAddress;

		for (Map.Entry<String, Optional<GeolocationData>> entry : geolocationDatas.entrySet()) {
			String key = entry.getKey();
			Optional<GeolocationData> val = entry.getValue();
			result += ", " + key + ": " + val;
		}
		result += "\n";

		return result;
	}

}
