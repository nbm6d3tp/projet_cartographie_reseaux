package fr.internetcartographier.model.tracerouteresults;

import fr.internetcartographier.model.geolocationservice.IPAddress;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TracerouteRow {

    private static final String REGEX = "(?:(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,})\\s+)?(?:\\((\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\)\\s+)?(\\d+\\.\\d+|\\d+)\\s+ms";

    private final static Pattern PATTERN = Pattern.compile(REGEX);

    private final Map<IPAddress, Double> responseTimes;

    private final IPAddress ipAddress;

    private final double responseTime;

    private final boolean reachable;

    public TracerouteRow(String row) {
        responseTimes = new HashMap<>();
        parse(row);

        Optional<Map.Entry<IPAddress, Double>> minEntry = responseTimes.entrySet().stream()
                .min(Map.Entry.comparingByValue());

        if (minEntry.isPresent()) {
            ipAddress = minEntry.get().getKey();
            responseTime = minEntry.get().getValue();
            reachable = true;
        } else {
            ipAddress = null;
            responseTime = -1.0;
            reachable = false;
        }
    }

    public double getResponseTime() {
        return responseTime;
    }

    public IPAddress getIpAddress() {
        return ipAddress;
    }

    public boolean isReachable() {
        return reachable;
    }

    private void parse(String row) {
        Matcher matcher = PATTERN.matcher(row);
        IPAddress lastIPAddress = null;

        while (matcher.find()) {
            String name = matcher.group(1);
            String ip = matcher.group(2);
            String responseTime = matcher.group(3);

            if (name == null) {
                // [Response time] ms
                Double lastResponseTime = responseTimes.get(lastIPAddress);
                Double responseTimeDouble = Double.parseDouble(responseTime);

                if (lastResponseTime != null && responseTimeDouble < lastResponseTime && lastIPAddress != null) {
                    responseTimes.put(lastIPAddress, responseTimeDouble);
                }
            } else if (ip == null) {
                // [IP] [Response time] ms
                if (IPAddress.isValidIPAddress(name)) {
                    lastIPAddress = new IPAddress(name);

                    responseTimes.put(lastIPAddress, Double.parseDouble(responseTime));
                }
            } else {
                // [IP or Name] ([IP]) [Response time] ms
                if (IPAddress.isValidIPAddress(ip)) {
                    if (!IPAddress.isValidIPAddress(name)) {
                        lastIPAddress = new IPAddress(ip, name);
                    } else {
                        lastIPAddress = new IPAddress(ip);
                    }
                }

                responseTimes.put(lastIPAddress, Double.parseDouble(responseTime));
            }
        }
    }

}
