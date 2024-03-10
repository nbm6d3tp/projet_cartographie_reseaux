package fr.internetcartographier.model.geolocationservice;

import java.util.Objects;
import java.util.Optional;

/**
 * The {@code GeolocationData} class represents geographical coordinates and
 * location information,
 * including latitude, longitude, country, and city. It provides methods to
 * access and manipulate
 * this information, as well as utilities for creating typed geolocation data
 * objects.
 */
public class GeolocationData {
	/**
	 * The latitude of the geographical location.
	 */
	private double latitude;

	/**
	 * The longitude of the geographical location.
	 */
	private double longitude;

	/**
	 * The country associated with the geographical location.
	 */
	private String country;

	/**
	 * The city associated with the geographical location.
	 */
	private String city;

	/**
	 * A nested class representing typed geolocation data, including the service
	 * generating the data
	 * and an optional {@code GeolocationData} object. Serving to know which
	 * geolocation service has
	 * generated the geo data.
	 */
	public static class TypedGeolocationData {

		/**
		 * The class of the geolocation service generating the data.
		 */
		private Class<? extends GeolocationService> serviceGenerating;

		/**
		 * Optional geolocation data associated with the service.
		 */
		private Optional<GeolocationData> geolocationData;

		/**
		 * Constructs a {@code TypedGeolocationData} object with the specified service
		 * class and
		 * optional geolocation data.
		 *
		 * @param serviceGenerating The class of the geolocation service generating the
		 *                          data.
		 * @param geolocationData   Optional geolocation data associated with the
		 *                          service.
		 */
		public TypedGeolocationData(Class<? extends GeolocationService> serviceGenerating,
				Optional<GeolocationData> geolocationData) {
			super();
			this.serviceGenerating = serviceGenerating;
			this.geolocationData = geolocationData;
		}

		/**
		 * Gets the class of the geolocation service generating the data.
		 *
		 * @return The class of the geolocation service.
		 */
		public Class<? extends GeolocationService> getServiceGenerating() {
			return serviceGenerating;
		}

		/**
		 * Gets the optional geolocation data associated with the service.
		 *
		 * @return Optional geolocation data.
		 */
		public Optional<GeolocationData> getGeolocationData() {
			return geolocationData;
		}
	}

	/**
	 * Constructs a {@code GeolocationData} object with the specified latitude,
	 * longitude, country, and city.
	 * Validates the coordinates and ensures that country and city are not null.
	 *
	 * @param  latitude                 The latitude of the geographical location.
	 * @param  longitude                The longitude of the geographical location.
	 * @param  country                  The country associated with the geographical
	 *                                  location.
	 * @param  city                     The city associated with the geographical
	 *                                  location.
	 * @throws IllegalArgumentException If the coordinates are invalid or
	 *                                  country/city is null.
	 */
	public GeolocationData(double latitude, double longitude, String country, String city) {
		if (!isCoordinateValid(latitude, longitude) || country == null || city == null) {
			throw new IllegalArgumentException("Arguments passed in GeolocationData's constructor not valid");
		}
		this.longitude = longitude;
		this.latitude = latitude;
		this.country = country;
		this.city = city;
	}

	/**
	 * Constructs a {@code GeolocationData} object by copying another
	 * {@code GeolocationData} object.
	 *
	 * @param geolocationData The GeolocationData object to copy.
	 */
	public GeolocationData(GeolocationData geolocationData) {
		longitude = geolocationData.longitude;
		latitude = geolocationData.latitude;
		country = geolocationData.country;
		city = geolocationData.city;
	}

	/**
	 * Checks if the given coordinates are valid.
	 *
	 * @param  lat The latitude to check.
	 * @param  lon The longitude to check.
	 * @return     {@code true} if the coordinates are valid, {@code false}
	 *             otherwise.
	 */
	private boolean isCoordinateValid(double lat, double lon) {
		return lon >= -180.0 && lon <= 180.0 && lat >= -180.0 && lat <= 180.0;
	}

	public double getLatitude() {
		return latitude;
	}

	/**
	 * Gets the longitude of the geographical location.
	 *
	 * @return The longitude.
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Gets the country associated with the geographical location.
	 *
	 * @return The country.
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Gets the city associated with the geographical location.
	 *
	 * @return The city.
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Gets a string representation of the geographical coordinates in the format
	 * "latitude, longitude".
	 *
	 * @return The string representation of the coordinates.
	 */
	public String getGPSString() {
		return latitude + "," + longitude;
	}

	/**
	 * Computes the hash code for this object based on its fields.
	 *
	 * @return The hash code.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(city, country, latitude, longitude);
	}

	/**
	 * Checks if this object is equal to another object based on its fields.
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
		GeolocationData other = (GeolocationData) obj;
		return Double.compare(other.latitude, latitude) == 0 && Double.compare(other.longitude, longitude) == 0
				&& Objects.equals(city, other.city) && Objects.equals(country, other.country);
	}

	/**
	 * Returns a string representation of this object.
	 *
	 * @return The string representation of the object.
	 */
	@Override
	public String toString() {
		return "GeolocationData [latitude=" + latitude + ", longitude=" + longitude + ", country=" + country + ", city="
				+ city + "]";
	}
}