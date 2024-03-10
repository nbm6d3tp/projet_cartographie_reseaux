package fr.internetcartographier.controller.traceroutemanager;

import javafx.beans.property.*;

public class TracerouteParameters {

    private final BooleanProperty hostNameResolution;

    private final IntegerProperty maxHops;

    private final DoubleProperty timeout;

    public TracerouteParameters(boolean hostNameResolution, int maxHops, double timeout) {
        this.hostNameResolution = new SimpleBooleanProperty(hostNameResolution);
        this.maxHops = new SimpleIntegerProperty(maxHops);
        this.timeout = new SimpleDoubleProperty(timeout);
    }

    public TracerouteParameters(TracerouteParameters tracerouteParameters) {
        hostNameResolution = new SimpleBooleanProperty(tracerouteParameters.hostNameResolution.getValue());
        maxHops = new SimpleIntegerProperty(tracerouteParameters.maxHops.getValue());
        timeout = new SimpleDoubleProperty(tracerouteParameters.timeout.getValue());
    }

    public BooleanProperty getHostNameResolutionProperty() {
        return hostNameResolution;
    }

    public IntegerProperty getMaxHopsProperty() {
        return maxHops;
    }

    public DoubleProperty getTimeoutProperty() {
        return timeout;
    }

    public boolean getHostNameResolution() {
        return hostNameResolution.getValue();
    }

    public int getMaxHops() {
        return maxHops.getValue();
    }

    public double getTimeout() {
        return timeout.getValue();
    }

}
