package fr.internetcartographier.model.tracerouteresults;

import fr.internetcartographier.controller.traceroutemanager.TracerouteParameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class TracerouteResult implements Iterable<TracerouteRow> {

    private final static AtomicLong idGenerator = new AtomicLong(0);

    private final long id;

    private final TracerouteParameters tracerouteParameters;

    private final String target;

    private final List<TracerouteRow> rows;

    public TracerouteResult(TracerouteParameters tracerouteParameters, String target, String tracerouteResult) {
        id = idGenerator.incrementAndGet();
        this.tracerouteParameters = tracerouteParameters;
        this.target = target;

        rows = new ArrayList<>();

        String[] lines = tracerouteResult.split("\n");
        String[] rows = Arrays.copyOfRange(lines, 1, lines.length);

        for (String row : rows) {
            this.rows.add(new TracerouteRow(row));
        }
    }

    public long getId() {
        return id;
    }

    public TracerouteParameters getTracerouteParameters() {
        return tracerouteParameters;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public Iterator<TracerouteRow> iterator() {
        return new TracerouteResultsIterator();
    }

    public boolean contains(String ipAddress) {
        for (TracerouteRow row : rows) {
            if (row.getIpAddress().getStringIpAddress().equals(ipAddress)) {
                return true;
            }
        }

        return false;
    }

    public class TracerouteResultsIterator implements Iterator<TracerouteRow> {

        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < rows.size();
        }

        @Override
        public TracerouteRow next() {
            return rows.get(current++);
        }

    }

}
