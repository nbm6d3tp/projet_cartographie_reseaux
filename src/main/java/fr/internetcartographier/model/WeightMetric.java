package fr.internetcartographier.model;

import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.internetgraph.Edge;

/**
 * Enumeration representing different weight metrics for edges in a network
 * graph.
 * Each constant in this enum defines a specific way to calculate the weight of
 * an edge.
 * The enum includes:
 * - CONSTANT: Returns a constant weight value of 1 for all edges.
 * - DISTANCE: Calculates the distance between two nodes based on their
 * geographical coordinates.
 * - RESPONSE_TIME: Uses the response time as the weight for an edge.
 */
public enum WeightMetric {

	/**
	 * Represents a constant weight of 1 for all edges.
	 */
	CONSTANT {

		/**
		 * Returns a constant weight value of 1 for the given edge and geolocation
		 * service.
		 *
		 * @param  edge               The edge for which to calculate the weight.
		 * @param  geolocationService The geolocation service to use in the calculation.
		 * @return                    The constant weight value of 1.
		 */
		@Override
		public double getWeight(Edge edge, Class<? extends GeolocationService> geolocationService) {
			return 1;
		}

		/**
		 * Returns the string representation of this weight metric.
		 *
		 * @return The string "Constant".
		 */
		@Override
		public String toString() {
			return "Constant";
		}
	},
	/**
	 * Represents the weight based on the real distance between two nodes.
	 */
	DISTANCE {

		/**
		 * Calculates the distance-based weight for the given edge and geolocation
		 * service.
		 * Uses the Haversine formula to calculate the great-circle distance between two
		 * points on the Earth's surface.
		 *
		 * @param  edge               The edge for which to calculate the weight.
		 * @param  geolocationService The geolocation service to use in the calculation.
		 * @return                    The calculated distance-based weight.
		 */
		@Override
		public double getWeight(Edge edge, Class<? extends GeolocationService> geolocationService) {
			if (edge.getNodeA().getGeolocationData(geolocationService).isEmpty()
					|| edge.getNodeB().getGeolocationData(geolocationService).isEmpty()) {
				return Double.MAX_VALUE;
			}
			// return distance between the 2 nodes of an edge
			GeolocationData geoDataNodeA = edge.getNodeA().getGeolocationData(geolocationService).get();
			GeolocationData geoDataNodeB = edge.getNodeB().getGeolocationData(geolocationService).get();

			double lat1Rad = Math.toRadians(geoDataNodeA.getLatitude());
			double lat2Rad = Math.toRadians(geoDataNodeB.getLatitude());
			double lon1Rad = Math.toRadians(geoDataNodeA.getLongitude());
			double lon2Rad = Math.toRadians(geoDataNodeB.getLongitude());

			double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
			double y = lat2Rad - lat1Rad;
			double distance = Math.sqrt(x * x + y * y) * EARTH_RADIUS;
			return Math.round(distance * 100.0) / 100.0;
		}

		/**
		 * Returns the string representation of this weight metric.
		 *
		 * @return The string "Real distance".
		 */
		@Override
		public String toString() {
			return "Real distance";
		}
	},

	/**
	 * Represents the weight based on the response time of an edge.
	 */
	RESPONSE_TIME {

		/**
		 * Returns the response time as the weight for the given edge and geolocation
		 * service.
		 *
		 * @param  edge               The edge for which to calculate the weight.
		 * @param  geolocationService The geolocation service to use in the calculation.
		 * @return                    The response time as the weight.
		 */
		@Override
		public double getWeight(Edge edge, Class<? extends GeolocationService> geolocationService) {
			// TODO Auto-generated method stub
			return edge.getResponseTime();
		}

		/**
		 * Returns the string representation of this weight metric.
		 *
		 * @return The string "Response time".
		 */
		@Override
		public String toString() {
			return "Response time";
		}
	};

	private final static int EARTH_RADIUS = 6371;

	/**
	 * Abstract method to be implemented by each constant for calculating the weight
	 * of an edge.
	 *
	 * @param  edge               The edge for which to calculate the weight.
	 * @param  geolocationService The geolocation service to use in the calculation.
	 * @return                    The calculated weight for the given edge.
	 */
	public abstract double getWeight(Edge edge, Class<? extends GeolocationService> geolocationService);
}
