package fr.internetcartographier.model.geolocationservice;

import fr.internetcartographier.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The {@code HostIP} class implements the {@code GeolocationService} interface
 * to provide
 * geolocation data based on host IP addresses using a SAX parser.
 */
public class HostIP implements GeolocationService {

	/**
	 * Singleton instance of the HostIP service.
	 */
	private static GeolocationService instance = null;

	/**
	 * SAX parser used for parsing XML data.
	 */
	private static SAXParser saxParser;

	/**
	 * SAX handler for extracting geolocation information from XML data.
	 */
	private static GetGeolocationDataHandlerSax handler;

	/**
	 * Gets the name of the HostIP geolocation service.
	 *
	 * @return The name of the geolocation service.
	 */
	@Override
	public String getName() {
		return "HostIP";
	}

	/**
	 * Retrieves the singleton instance of the HostIP service. If an instance does
	 * not exist,
	 * a new one is created and returned.
	 *
	 * @return           The singleton instance of the HostIP service.
	 * @throws Exception If there is an error initializing the HostIP service.
	 */
	public static GeolocationService getInstance() throws ParserConfigurationException, SAXException, MalformedURLException {
		if (instance == null) {
			instance = new HostIP();
		}

		return instance;
	}

	private GeolocationServiceStatus status;

	private final String apiEndpoint;

	/**
	 * Private constructor for the HostIP service. Initializes the SAX parser and
	 * handler.
	 *
	 * @throws ParserConfigurationException If there is an error configuring the SAX
	 *                                      parser.
	 * @throws SAXException                 If there is an error creating the SAX
	 *                                      parser.
	 */
	private HostIP() throws ParserConfigurationException, SAXException, MalformedURLException {
		SAXParserFactory factory = SAXParserFactory.newInstance();

		saxParser = factory.newSAXParser();
		handler = new GetGeolocationDataHandlerSax();

        try {
            apiEndpoint = Configuration.getInstance().getProperty("hostipendpoint");
        } catch (IOException e) {
            throw new RuntimeException("Error: HostIP API endpoint missing from 'config.properties'!");
        }

		URL url = new URL(apiEndpoint);

        try {
			url.openStream();
			status = GeolocationServiceStatus.UP;
		} catch (IOException e) {
			status = GeolocationServiceStatus.DOWN;
        }
	}

	/**
	 * Retrieves geolocation data for the given host IP address using a SAX parser.
	 *
	 * @param  ipAddress The host IP address for which geolocation data is
	 *                   requested.
	 * @return           An optional containing the geolocation data, or empty if an
	 *                   error occurs during retrieval.
	 */
	@Override
	public Optional<GeolocationData> getGeolocationData(IPAddress ipAddress) {
		try {
			URL url = new URL(apiEndpoint + "?ip=" + ipAddress.getStringIpAddress());

			saxParser.parse(new InputSource(url.openStream()), handler);
			status = GeolocationServiceStatus.UP;
		} catch (IOException | SAXException ioe ) {
			status = GeolocationServiceStatus.DOWN;

			return Optional.empty();
		}

		String countryName = handler.getCountryName() != null ? handler.getCountryName() : "";
		String cityName = handler.getCityName() != null ? handler.getCityName() : "";
		Optional<String> coordinates = handler.getCoordinates();

		if (coordinates.isEmpty()) {
			return Optional.empty();
		}

		String[] coordinatesArray = coordinates.get().split(",");

		double longitude = Double.parseDouble(coordinatesArray[0]);
		double latitude = Double.parseDouble(coordinatesArray[1]);

		return Optional.of(new GeolocationData(latitude, longitude, countryName, cityName));
	}

	@Override
	public GeolocationServiceStatus getStatus() {
		return status;
	}

}
