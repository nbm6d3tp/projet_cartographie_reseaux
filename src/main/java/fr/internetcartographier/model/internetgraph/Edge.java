package fr.internetcartographier.model.internetgraph;

import fr.internetcartographier.model.WeightMetric;
import fr.internetcartographier.model.geolocationservice.GeoIP2;
import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.geolocationservice.HostIP;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import java.util.Objects;

/**
 * The {@code Edge} class represents a connection between two nodes in a graph,
 * with properties such as direction, response time, and geolocation data.
 */
public class Edge {

	/**
	 * The starting node of the edge.
	 */
	private Node nodeA;

	/**
	 * The ending node of the edge.
	 */
	private Node nodeB;

	/**
	 * Indicates whether the edge is directed.
	 */
	private boolean isDirect;

	/**
	 * The response time associated with the edge.
	 */
	private double responseTime;

	/**
	 * Constructs an {@code Edge} object by copying another {@code Edge} object.
	 *
	 * @param edge The Edge object to copy.
	 */
	public Edge(Edge edge) {
		nodeA = edge.nodeA;
		nodeB = edge.nodeB;
		isDirect = edge.isDirect;
		responseTime = edge.responseTime;
	}

	/**
	 * Constructs an {@code Edge} object with specified nodes, direction, and
	 * response time.
	 *
	 * @param  nodeA                    The starting node of the edge.
	 * @param  nodeB                    The ending node of the edge.
	 * @param  isDirect                 Indicates whether the edge is directed.
	 * @param  responseTime             The response time associated with the edge.
	 * @throws IllegalArgumentException If either node is null or both nodes are the
	 *                                  same.
	 */
	public Edge(Node nodeA, Node nodeB, boolean isDirect, double responseTime) {
		if (nodeA == null || nodeB == null) {
			throw new IllegalArgumentException("2 extremities of an edge can't be null");
		}

		if (nodeA.equals(nodeB)) {
			throw new IllegalArgumentException("2 extremities of an edge have to be different");
		}

		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.isDirect = isDirect;
		this.responseTime = responseTime;
	}

	/**
	 * Gets a copy of the starting node of the edge.
	 *
	 * @return A copy of the starting node.
	 */
	public Node getNodeA() {
		return new Node(nodeA);
	}

	/**
	 * Gets a copy of the ending node of the edge.
	 *
	 * @return A copy of the ending node.
	 */
	public Node getNodeB() {
		return new Node(nodeB);
	}

	/**
	 * Updates the geolocation data of the starting node of the edge.
	 *
	 * @param geolocationData The geolocation data to update.
	 */
	public void updateGeolocationDataNodeA(GeolocationData.TypedGeolocationData geolocationData) {
		nodeA.updateGeolocationData(geolocationData);
	}

	/**
	 * Updates the geolocation data of the ending node of the edge.
	 *
	 * @param geolocationData The geolocation data to update.
	 */
	public void updateGeolocationDataNodeB(GeolocationData.TypedGeolocationData geolocationData) {
		nodeB.updateGeolocationData(geolocationData);
	}

	/**
	 * Checks if the edge is directed.
	 *
	 * @return {@code true} if the edge is directed, {@code false} otherwise.
	 */
	public boolean isDirect() {
		return isDirect;
	}

	/**
	 * Gets the response time associated with the edge.
	 *
	 * @return The response time.
	 */
	public double getResponseTime() {
		return responseTime;
	}

	/**
	 * Gets the weight of the edge based on the specified weight metric and
	 * geolocation service.
	 *
	 * @param  weightMetric       The weight metric to use.
	 * @param  geolocationService The geolocation service class.
	 * @return                    The weight of the edge.
	 */
	public double getWeight(WeightMetric weightMetric, Class<? extends GeolocationService> geolocationService) {
		return weightMetric.getWeight(this, geolocationService);
	}

	/**
	 * Sets the direction of the edge.
	 *
	 * @param isDirect {@code true} if the edge is directed, {@code false}
	 *                 otherwise.
	 */
	public void setDirect(boolean isDirect) {
		this.isDirect = isDirect;
	}

	/**
	 * Checks if the edge contains the specified node.
	 *
	 * @param  node The node to check.
	 * @return      {@code true} if the edge contains the node, {@code false}
	 *              otherwise.
	 */
	public boolean isContainNode(Node node) {
		return isContainNode(node.getIpAddress());
	}

	/**
	 * Checks if the edge contains the node with the specified IP address.
	 *
	 * @param  ipAddress The IP address of the node to check.
	 * @return           {@code true} if the edge contains the node, {@code false}
	 *                   otherwise.
	 */
	public boolean isContainNode(IPAddress ipAddress) {
		return isContainNode(ipAddress.getStringIpAddress());
	}

	/**
	 * Checks if the edge contains the node with the specified IP address.
	 *
	 * @param  ipAddress The IP address of the node to check.
	 * @return           {@code true} if the edge contains the node, {@code false}
	 *                   otherwise.
	 */
	public boolean isContainNode(String ipAddress) {
		return nodeA.getStringIpAddress().equals(ipAddress) || nodeB.getStringIpAddress().equals(ipAddress);
	}

	/**
	 * Gets the other node connected to the edge, given one node's IP address.
	 *
	 * @param  ipAddress                The IP address of one of the nodes.
	 * @return                          The other node connected to the edge.
	 * @throws IllegalArgumentException If the specified IP address does not belong
	 *                                  to either node.
	 */
	Node getAnotherNode(String ipAddress) {
		if (!nodeA.getStringIpAddress().equals(ipAddress) && !nodeB.getStringIpAddress().equals(ipAddress)) {
			throw new IllegalArgumentException("The specified IP address does not belong to either node");
		}
		return nodeA.getStringIpAddress().equals(ipAddress) ? nodeB : nodeA;
	}

	/**
	 * Computes the hash code for this object based on its nodes.
	 *
	 * @return The hash code.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(nodeA) + Objects.hash(nodeB);
	}

	/**
	 * Checks if this object is equal to another object based on its nodes.
	 *
	 * @param  obj The object to compare.
	 * @return     {@code true} if the objects are equal, {@code false} otherwise.
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
		Edge other = (Edge) obj;
		return Objects.equals(nodeA, other.nodeA) && Objects.equals(nodeB, other.nodeB)
				|| Objects.equals(nodeA, other.nodeB) && Objects.equals(nodeB, other.nodeA);
	}

	/**
	 * Returns a string representation of this object, including weights based on
	 * different geolocation services.
	 *
	 * @return The string representation of the object.
	 */
	@Override
	public String toString() {
		return "Edge [nodeA=" + nodeA + ", nodeB=" + nodeB + ", isDirect=" + isDirect + ", weightInDistance(HostIp)="
				+ getWeight(WeightMetric.DISTANCE, HostIP.class) + ", weightInDistance(GeoIp2)="
				+ getWeight(WeightMetric.DISTANCE, GeoIP2.class) + ", weightInResponseTime="
				+ getWeight(WeightMetric.RESPONSE_TIME, null) + "]\n";
	}
}
