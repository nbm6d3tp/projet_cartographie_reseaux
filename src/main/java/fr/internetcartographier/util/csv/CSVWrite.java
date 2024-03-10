package fr.internetcartographier.util.csv;

import fr.internetcartographier.model.InternetCartographier;
import fr.internetcartographier.model.WeightMetric;
import fr.internetcartographier.model.geolocationservice.GeoIP2;
import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.geolocationservice.HostIP;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import fr.internetcartographier.model.internetgraph.Edge;
import fr.internetcartographier.model.internetgraph.InternetGraph;
import fr.internetcartographier.model.internetgraph.Node;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// https://mkyong.com/java/how-to-export-data-to-csv-file-java/

public class CSVWrite {

	// TODO can be integrated in the config.properties
	private static final char COMMA = ',';
	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DOUBLE_QUOTES = '\"';
	private static final String EMBEDDED_DOUBLE_QUOTES = "\"\"";
	private static final char NEW_LINE_UNIX = '\n';
	private static final String NEW_LINE_WINDOWS = "\r\n";

	public static void exportCsvNodesFile(InternetCartographier internetCartographier, String pathToFileNodes,
			String pathToFileEdges) throws IOException {
		InternetGraph internetGraph = internetCartographier.getInternetGraph();
		Set<Node> nodes = internetGraph.getNodes();
		Set<Edge> edges = internetGraph.getEdges();

		IPAddress originIpAddress = internetCartographier.getOrigin().getIpAddress();
		List<String[]> listNodeRecords = new ArrayList<>();
		List<String[]> listEdgeRecords = new ArrayList<>();

		String[] nodesHeader = { "Node", "Distance to source (constant)", "Distance to source (real distance, HostIP)",
				"Distance to source (real distance, GeoIP2)", "Distance to source (Response Time)", "GPS(HostIP)",
				"Country(HostIP)", "City(HostIP)", "Is HostIP geo data computed already?", "GPS(GeoIP2)",
				"Country(GeoIP2)", "City(GeoIP2)", "Is GeoIP2 geo data computed already?" };

		String[] edgesHeader = { "Node A", "Node B", "Is direct", "Weight in real distance(HostIP)",
				"Weight in real distance(GeoIP2)", "Weight in response time" };

		listNodeRecords.add(nodesHeader);
		listEdgeRecords.add(edgesHeader);

		for (Node node : nodes) {
			IPAddress ipAddress = node.getIpAddress();
			String[] nodeRecord = new String[13];

			nodeRecord[0] = node.getStringIpAddress();
			nodeRecord[1] = internetGraph
					.minimumDistance(ipAddress, originIpAddress, WeightMetric.CONSTANT, GeolocationService.class)
					.getCost() + "";
			nodeRecord[2] = internetGraph
					.minimumDistance(ipAddress, originIpAddress, WeightMetric.DISTANCE, HostIP.class).getCost() + "";
			nodeRecord[3] = internetGraph
					.minimumDistance(ipAddress, originIpAddress, WeightMetric.DISTANCE, GeoIP2.class).getCost() + "";
			nodeRecord[4] = internetGraph
					.minimumDistance(ipAddress, originIpAddress, WeightMetric.RESPONSE_TIME, GeolocationService.class)
					.getCost() + "";

			Optional<GeolocationData> geolocationDataHostIP = node.getGeolocationData(HostIP.class);
			boolean isNodeContainsAlreadyHostIPData = node.isNodeContainsAlreadyGeolocationData(HostIP.class);

			Optional<GeolocationData> geolocationDataGeoIP2 = node.getGeolocationData(GeoIP2.class);
			boolean isNodeContainsAlreadyGeoIP2Data = node.isNodeContainsAlreadyGeolocationData(GeoIP2.class);

			nodeRecord[5] = geolocationDataHostIP.isPresent() ? geolocationDataHostIP.get().getGPSString() : "";
			nodeRecord[6] = geolocationDataHostIP.isPresent() ? geolocationDataHostIP.get().getCountry() : "";
			nodeRecord[7] = geolocationDataHostIP.isPresent() ? geolocationDataHostIP.get().getCity() : "";
			nodeRecord[8] = isNodeContainsAlreadyHostIPData + "";

			nodeRecord[9] = geolocationDataGeoIP2.isPresent() ? geolocationDataGeoIP2.get().getGPSString() : "";
			nodeRecord[10] = geolocationDataGeoIP2.isPresent() ? geolocationDataGeoIP2.get().getCountry() : "";
			nodeRecord[11] = geolocationDataGeoIP2.isPresent() ? geolocationDataGeoIP2.get().getCity() : "";
			nodeRecord[12] = isNodeContainsAlreadyGeoIP2Data + "";

			listNodeRecords.add(nodeRecord);
		}

		for (Edge edge : edges) {
			String[] edgeRecord = new String[6];

			edgeRecord[0] = edge.getNodeA().getStringIpAddress();
			edgeRecord[1] = edge.getNodeB().getStringIpAddress();
			edgeRecord[2] = edge.isDirect() + "";
			edgeRecord[3] = edge.getWeight(WeightMetric.DISTANCE, HostIP.class) + "";
			edgeRecord[4] = edge.getWeight(WeightMetric.DISTANCE, GeoIP2.class) + "";
			edgeRecord[5] = edge.getWeight(WeightMetric.RESPONSE_TIME, null) + "";

			listEdgeRecords.add(edgeRecord);
		}

		CSVWrite writer = new CSVWrite();

		writer.writeToCsvFile(listNodeRecords, new File(pathToFileNodes));
		writer.writeToCsvFile(listEdgeRecords, new File(pathToFileEdges));
	}

	private String convertToCsvFormat(final String[] line) {
		return Stream.of(line) // convert String[] to stream
				.map(l -> formatCsvField(l, true)) // format CSV field
				.collect(Collectors.joining(String.valueOf(DEFAULT_SEPARATOR))); // join with a separator
	}

	// put your extra login here
	private String formatCsvField(final String field, final boolean quote) {
		String result = field;

		if (result.contains(String.valueOf(COMMA)) || result.contains(String.valueOf(DOUBLE_QUOTES)) || result.contains(String.valueOf(NEW_LINE_UNIX))
				|| result.contains(NEW_LINE_WINDOWS)) {

			// if field contains double quotes, replace it with two double quotes \"\"
			result = result.replace(String.valueOf(DOUBLE_QUOTES), EMBEDDED_DOUBLE_QUOTES);

			// must wrap by or enclosed with double quotes
			result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;

		} else {
			// should all fields enclosed in double quotes
			if (quote) {
				result = DOUBLE_QUOTES + result + DOUBLE_QUOTES;
			}
		}

		return result;

	}

	// a standard FileWriter, CSV is a normal text file
	private void writeToCsvFile(List<String[]> list, File file) throws IOException {
		List<String> collect = list.stream().map(this::convertToCsvFormat).toList();

		// CSV is a normal text file, need a writer
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			for (String line : collect) {
				bw.write(line);
				bw.newLine();
			}
		}
	}

}
