package fr.internetcartographier.model.geolocationservice;

import javafx.scene.paint.Color;

import java.util.Optional;

/**
 * The {@code GeolocationService} interface defines the contract for a service
 * that provides
 * geolocation information based on IP addresses.
 */
public interface GeolocationService {

	/**
	 * Gets the name of the geolocation service.
	 *
	 * @return The name of the geolocation service.
	 */
	String getName();

	/**
	 * Retrieves geolocation data for the given IP address.
	 *
	 * @param  ipAddress The IP address for which geolocation data is requested.
	 * @return           An optional containing the geolocation data, or empty if
	 *                   the data is not available.
	 */
	Optional<GeolocationData> getGeolocationData(IPAddress ipAddress);

	GeolocationServiceStatus getStatus();

	enum GeolocationServiceStatus {

		UP(Color.rgb(22, 154, 36)),
		DEGRADED(Color.rgb(238, 154, 28)),
		DOWN(Color.rgb(238, 28, 48));

		private final Color associatedColor;

		GeolocationServiceStatus(Color associatedColor) {
			this.associatedColor = associatedColor;
		}

		public Color getAssociatedColor() {
			return associatedColor;
		}

	}

}
