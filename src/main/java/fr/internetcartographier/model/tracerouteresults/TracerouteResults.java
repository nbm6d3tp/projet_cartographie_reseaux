package fr.internetcartographier.model.tracerouteresults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TracerouteResults {

	Map<Long, TracerouteResult> tracerouteResults;

	public TracerouteResults() {
		tracerouteResults = new HashMap<>();
	}

	public void addTracerouteResult(TracerouteResult tracerouteResult) {
		tracerouteResults.put(tracerouteResult.getId(), tracerouteResult);
	}

	public TracerouteResult getTracerouteResult(long id) {
		return tracerouteResults.get(id);
	}

	public List<Long> getIPAddressTracerouteResults(String ipAddress) {
		List<Long> results = new ArrayList<>();

		for (Map.Entry<Long, TracerouteResult> tracerouteResultEntry : tracerouteResults.entrySet()) {
			if (tracerouteResultEntry.getValue().contains(ipAddress)) {
				results.add(tracerouteResultEntry.getKey());
			}
		}

		return results;
	}

}