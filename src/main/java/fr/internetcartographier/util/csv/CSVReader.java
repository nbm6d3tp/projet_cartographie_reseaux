package fr.internetcartographier.util.csv;

import fr.internetcartographier.model.geolocationservice.GeoIP2;
import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.geolocationservice.HostIP;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import fr.internetcartographier.model.internetgraph.Edge;
import fr.internetcartographier.model.internetgraph.InternetGraph;
import fr.internetcartographier.model.internetgraph.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

// https://mkyong.com/java/how-to-read-and-parse-csv-file-in-java/

public class CSVReader {

	private static final char DOUBLE_QUOTES = '"';
	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE_CHAR = '"';
	private static final char NEW_LINE = '\n';

	private boolean isMultiLine = false;
	private String pendingField = "";
	private String[] pendingFieldLine = new String[] {};


	public static InternetGraph importGraphFromCSVFile(File nodesFile, File edgesFile) throws IOException {
		CSVReader reader = new CSVReader();

		InternetGraph internetGraph = new InternetGraph();

		List<String[]> nodesResult = reader.readFile(nodesFile, 1);
		List<String[]> edgesResult = reader.readFile(edgesFile, 1);

		List<GeolocationData.TypedGeolocationData> geolocationDatas = new ArrayList<>();

		for (String[] nodeRecords : nodesResult) {
			IPAddress ipAddress = new IPAddress(nodeRecords[0]);

			if (nodeRecords[8].equals("true")) {
				Optional<GeolocationData> geoDataHostIP = parseGeolocationData(nodeRecords[5], nodeRecords[6],
						nodeRecords[7]);
				geolocationDatas.add(new GeolocationData.TypedGeolocationData(HostIP.class, geoDataHostIP));
			}

			if (nodeRecords[12].equals("true")) {
				Optional<GeolocationData> geoDataGeoIP2 = parseGeolocationData(nodeRecords[9], nodeRecords[10],
						nodeRecords[11]);
				geolocationDatas.add(new GeolocationData.TypedGeolocationData(GeoIP2.class, geoDataGeoIP2));
			}

			Node node = new Node(ipAddress, geolocationDatas.toArray(new GeolocationData.TypedGeolocationData[0]));
			internetGraph.addNode(node);
		}

		for (String[] edgeRecords : edgesResult) {
			Optional<Node> nodeA = internetGraph.getNode(edgeRecords[0]);
			Optional<Node> nodeB = internetGraph.getNode(edgeRecords[1]);

			if (nodeA.isEmpty() || nodeB.isEmpty()) {
				continue;
			}

			double responseTime;

			try {
				responseTime = Double.parseDouble(edgeRecords[5]);
			} catch (Exception e) {
				responseTime = 0.0;
			}

			Edge edge = new Edge(nodeA.get(), nodeB.get(), true, responseTime);
			internetGraph.addEdge(edge);
		}

		return internetGraph;
	}

	public static InternetGraph importGraphFromCSVFiles(String pathToNodesFile, String pathToEdgesFile) throws IOException {
		return importGraphFromCSVFile(new File(pathToNodesFile), new File(pathToEdgesFile));
	}

	// TODO Keeping but in fact targets entry is not a CSV it's a txt
	public static Set<String> importTargetsFromCSVFile(String pathToTargetsFile) throws Exception {
		return importTargetsFromCSVFile(new File(pathToTargetsFile));
	}

	public static Set<String> importTargetsFromCSVFile(File targetsFile) throws Exception {
		CSVReader reader = new CSVReader();

		List<String[]> nodesResult = reader.readFile(targetsFile, 1);
		Set<String> nodes = new HashSet<>();

		for (String[] nodeRecords : nodesResult) {
			nodes.add(nodeRecords[0]);
		}

		return nodes;
	}

	private static Optional<GeolocationData> parseGeolocationData(String coordinates, String country, String city) {
		if (coordinates.isEmpty()) {
			return Optional.empty();
		}

		String[] parts = coordinates.split(",");

		if (parts.length == 2) {
			try {
				double latitude = Double.parseDouble(parts[0].trim());
				double longitude = Double.parseDouble(parts[1].trim());

				return Optional.of(new GeolocationData(latitude, longitude, country, city));
			} catch (NumberFormatException e) {
				return Optional.empty();
			}
		}

		return Optional.empty();
	}

	private List<String[]> readFile(File csvFile, int skipLine) throws IOException {
		List<String[]> result = new ArrayList<>();

		int indexLine = 1;

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			String line;

			while ((line = br.readLine()) != null) {

				if (indexLine++ <= skipLine) {
					continue;
				}

				String[] csvLineInArray = parseLine(line);

				if (isMultiLine) {
					pendingFieldLine = joinArrays(pendingFieldLine, csvLineInArray);
				} else {
					if (pendingFieldLine != null && pendingFieldLine.length > 0) {
						// joins all fields and add to list
						result.add(joinArrays(pendingFieldLine, csvLineInArray));

						pendingFieldLine = new String[0];
					} else {
						// if dun want to support multiline, only this line is required.
						result.add(csvLineInArray);
					}

				}

			}
		}

		return result;
	}

	private String[] parseLine(String line) {
		return parseLine(line, DEFAULT_SEPARATOR);
	}

	private String[] parseLine(String line, char separator) {
		return parse(line, separator, DEFAULT_QUOTE_CHAR).toArray(String[]::new);
	}

	private List<String> parse(String line, char separator, char quote) {
		List<String> result = new ArrayList<>();

		boolean inQuotes = false;
		boolean isFieldWithEmbeddedDoubleQuotes = false;

		StringBuilder field = new StringBuilder();

		for (char c : line.toCharArray()) {

			if (c == DOUBLE_QUOTES) { // handle embedded double quotes ""
				if (isFieldWithEmbeddedDoubleQuotes) {

					if (!field.isEmpty()) { // handle for empty field like "",""
						field.append(DOUBLE_QUOTES);
						isFieldWithEmbeddedDoubleQuotes = false;
					}

				} else {
					isFieldWithEmbeddedDoubleQuotes = true;
				}
			} else {
				isFieldWithEmbeddedDoubleQuotes = false;
			}

			if (isMultiLine) { // multiline, add pending from the previous field
				field.append(pendingField).append(NEW_LINE);
				pendingField = "";
				inQuotes = true;
				isMultiLine = false;
			}

			if (c == quote) {
				inQuotes = !inQuotes;
			} else {
				if (c == separator && !inQuotes) { // if find separator and not in quotes, add field to the list
					result.add(field.toString());
					field.setLength(0); // empty the field and ready for the next
				} else {
					field.append(c); // else append the char into a field
				}
			}

		}

		// line done, what to do next?
		if (inQuotes) {
			pendingField = field.toString(); // multiline
			isMultiLine = true;
		} else {
			result.add(field.toString()); // this is the last field
		}

		return result;
	}

	// TODO Can be extracted to a Util class with all generics helper methods
	private static String[] joinArrays(String[] array1, String[] array2) {
		return Stream.concat(Arrays.stream(array1), Arrays.stream(array2)).toArray(String[]::new);
	}

}
