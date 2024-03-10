package fr.internetcartographier.model.geolocationservice;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import fr.internetcartographier.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

/**
 * The {@code GeoIP2} class implements the {@code GeolocationService} interface
 * to provide geolocation data based on IP addresses using the GeoIP2 database.
 * It follows the singleton pattern to ensure a single instance of the service.
 */
public class GeoIP2 implements GeolocationService {

	private static GeolocationService instance = null;
	// GeoIP2 database reader
	private static DatabaseReader database;

	/**
	 * Retrieves the singleton instance of the GeoIP2 service. If an instance
	 * does not exist, a new one is created and returned.
	 *
	 * @return           The singleton instance of the GeoIP2 service.
	 * @throws Exception If there is an error initializing the GeoIP2 service.
	 */
	public static GeolocationService getInstance() throws URISyntaxException, IOException {
		if (instance == null) {
			instance = new GeoIP2();
		}

		return instance;
	}

	/**
	 * Returns the name of the GeoIP2 service.
	 *
	 * @return The name of the GeoIP2 service.
	 */
	@Override
	public String getName() {
		return "GeoIP2";
	}

	/**
	 * Private constructor for the GeoIP2 service. Initializes the GeoIP2
	 * database reader using the URL provided in the properties file.
	 *
	 * @throws Exception If there is an error initializing the GeoIP2 service.
	 */
	private GeoIP2() throws URISyntaxException, IOException {
		Configuration p = Configuration.getInstance();
		URL urlDatabase = this.getClass().getClassLoader().getResource(p.getProperty("geoip2database"));
		File fileDatabase = new File(Objects.requireNonNull(urlDatabase).toURI());

		database = new DatabaseReader.Builder(fileDatabase).build();
	}

	/**
	 * Retrieves geolocation data for the given IP address using the GeoIP2
	 * database.
	 *
	 * @param  ipAddress The IP address for which geolocation data is requested.
	 * @return           An optional containing the geolocation data, or empty if an
	 *                   error occurs during retrieval.
	 */
	@Override
	public Optional<GeolocationData> getGeolocationData(IPAddress ipAddress) {
		try {
			CityResponse response = database.city(InetAddress.getByName(ipAddress.getStringIpAddress()));
			String country = response.getCountry().getName();
			String city = response.getCity().getName();
			Location location = response.getLocation();

			return Optional.of(new GeolocationData(location.getLatitude(), location.getLongitude(),
					country == null ? "" : country, city == null ? "" : city));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	@Override
	public GeolocationServiceStatus getStatus() {
		return GeolocationServiceStatus.UP;
	}

}