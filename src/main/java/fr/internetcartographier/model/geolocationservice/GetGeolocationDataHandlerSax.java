package fr.internetcartographier.model.geolocationservice;

import java.util.Optional;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * The {@code GetGeolocationDataHandlerSax} class is a SAX (Simple API for XML)
 * handler
 * used to parse XML data and extract geolocation information such as country
 * name, city name,
 * and coordinates.
 */
public class GetGeolocationDataHandlerSax extends DefaultHandler {

	/**
	 * StringBuilder to store the current element's value during parsing.
	 */
	private StringBuilder currentValue = new StringBuilder();

	/**
	 * The extracted country name from the XML data.
	 */
	private String countryName = "";

	/**
	 * The extracted city name from the XML data.
	 */
	private String cityName = "";

	/**
	 * Optional containing the extracted coordinates (if available) from the XML
	 * data.
	 */
	private Optional<String> coordinates;

	/**
	 * Gets the extracted country name.
	 *
	 * @return The country name.
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * Gets the extracted coordinates as an optional string.
	 *
	 * @return Optional containing the coordinates, or empty if not available.
	 */
	public Optional<String> getCoordinates() {
		return coordinates;
	}

	/**
	 * Gets the extracted city name.
	 *
	 * @return The city name.
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * Resets the current value and coordinates when starting a new XML element.
	 *
	 * @param uri        The Namespace URI.
	 * @param localName  The local name (without prefix).
	 * @param qName      The qualified name (with prefix).
	 * @param attributes The attributes attached to the element.
	 */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) {
		currentValue.setLength(0);
		coordinates = Optional.empty();
	}

	/**
	 * Processes the end of an XML element, extracting relevant data based on the
	 * element name.
	 *
	 * @param uri       The Namespace URI.
	 * @param localName The local name (without prefix).
	 * @param qName     The qualified name (with prefix).
	 */
	@Override
	public void endElement(String uri, String localName, String qName) {
		if (qName.equalsIgnoreCase("countryName")) {
			if (currentValue.toString() != null) {
				countryName = currentValue.toString();
			}
		}

		if (qName.equalsIgnoreCase("gml:coordinates")) {
			coordinates = Optional.of(currentValue.toString());
		}

		if (qName.equalsIgnoreCase("gml:name")) {
			cityName = currentValue.toString();
		}
	}

	/**
	 * Appends character data to the current value during parsing.
	 *
	 * @param ch     The characters.
	 * @param start  The start position in the character array.
	 * @param length The number of characters to use from the character array.
	 */
	@Override
	public void characters(char ch[], int start, int length) {
		currentValue.append(ch, start, length);
	}
}
