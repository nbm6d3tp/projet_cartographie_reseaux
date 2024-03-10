package fr.internetcartographier.model.firebase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class StatisticDatabase {

	private Map<String, String> statisticsMap;

	public StatisticDatabase() {
		statisticsMap = new HashMap<>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		statisticsMap.put("date", dtf.format(now));
	}

	public void putEntry(String key, String value) {
		statisticsMap.put(key, value);
	}

	public Map<String, String> getMap() {
		return new HashMap<>(statisticsMap);
	}

	public String toString() {
		StringBuilder string = new StringBuilder();

		for (Map.Entry<String, String> entry : statisticsMap.entrySet()) {
			String escapedJumpLine = entry.getValue().replace("\\n", "\n");

			string.append(entry.getKey()).append(" ").append(escapedJumpLine).append("\n");
		}

		return string.toString();
	}

}
