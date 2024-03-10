package fr.internetcartographier.controller.traceroutemanager;

import fr.internetcartographier.controller.ApplicationController;
import fr.internetcartographier.controller.ControllerManager;
import fr.internetcartographier.controller.hud.HUDController;
import fr.internetcartographier.controller.hud.StatusUpdatePopupController;
import fr.internetcartographier.model.tracerouteresults.TracerouteResult;
import fr.internetcartographier.util.statistics.NumericStatistic;
import fr.internetcartographier.util.statistics.Statistic;
import fr.internetcartographier.util.statistics.Statisticable;
import fr.internetcartographier.util.statistics.Statistics;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TracerouteManager implements Statisticable {

    private final static String OS = System.getProperty("os.name");

    private final TracerouteParameters tracerouteParameters;

    private final IntegerProperty simultaneousTraceroute;

    private final IntegerProperty traceroutePerMinute;

    private final Queue<String> targets;

    private ScheduledExecutorService tracerouteExecutor;

    private ScheduledExecutorService queuePollExecutor;

    public TracerouteManager(TracerouteParameters tracerouteParameters, int simultaneousTraceroute, int traceroutePerMinute) {
        this.tracerouteParameters = tracerouteParameters;
        this.simultaneousTraceroute = new SimpleIntegerProperty(simultaneousTraceroute);
        this.simultaneousTraceroute.addListener((obs, oldValue, newValue) -> tracerouteExecutor = new ScheduledThreadPoolExecutor(newValue.intValue()));

        this.traceroutePerMinute = new SimpleIntegerProperty(traceroutePerMinute);
        this.traceroutePerMinute.addListener((observable, oldValue, newValue) -> {
            queuePollExecutor.shutdown();
            queuePollExecutor = getQueuePollExecutor();
        });

        targets = new LinkedList<>();
        tracerouteExecutor = getTracerouteExecutor();
        queuePollExecutor = getQueuePollExecutor();
    }

    public IntegerProperty getSimultaneousTracerouteProperty() {
        return simultaneousTraceroute;
    }

    public int getSimultaneousTraceroute() {
        return simultaneousTraceroute.get();
    }

    public IntegerProperty getTraceroutePerMinuteProperty() {
        return traceroutePerMinute;
    }

    public int getTraceroutePerMinute() {
        return traceroutePerMinute.get();
    }

    public void addTarget(String target) {
        targets.add(target);
    }

    public void addTargets(List<String> targets) {
        this.targets.addAll(targets);
    }

    private ScheduledExecutorService getTracerouteExecutor() {
        return new ScheduledThreadPoolExecutor(simultaneousTraceroute.getValue());
    }

    private ScheduledExecutorService getQueuePollExecutor() {
        ScheduledExecutorService queuePollRoutine = Executors.newSingleThreadScheduledExecutor();
        long delay = (long) ((60.0 / traceroutePerMinute.getValue()) * 1000);

        queuePollRoutine.scheduleAtFixedRate(() -> {
            if (!targets.isEmpty()) {
                String target = targets.poll();
                tracerouteExecutor.submit(() -> runTraceroute(target));
            }
        }, 0, delay, TimeUnit.MILLISECONDS);

        return queuePollRoutine;
    }

    private void runTraceroute(String target) {
        ControllerManager controllerManager = ControllerManager.getInstance();
        HUDController hudController = (HUDController) controllerManager.getController("HUDController");
        Platform.runLater(() -> hudController.setExecutingTracerouteTarget(target));
        executing.add(target);

        ProcessBuilder tracerouteProcess = getTracerouteProcess(target);

        try {
            Process process = tracerouteProcess.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = reader.readLine();
            StringBuilder tracerouteResultString = new StringBuilder();

            while (line != null) {
                tracerouteResultString.append(line).append("\n");
                line = reader.readLine();
            }

            process.waitFor();

            controllerManager = ControllerManager.getInstance();
            ApplicationController applicationController = (ApplicationController) controllerManager.getController("ApplicationController");

            TracerouteResult tracerouteResult = new TracerouteResult(new TracerouteParameters(tracerouteParameters), target, tracerouteResultString.toString());
            applicationController.update(tracerouteResult);

            executed.add(target);
        } catch (IOException | InterruptedException e) {
            controllerManager = ControllerManager.getInstance();
            StatusUpdatePopupController statusUpdatePopupController = (StatusUpdatePopupController) controllerManager.getController("StatusUpdatePopupController");

            statusUpdatePopupController.displayTemporaryMessage("Traceroute to " + target + " failed at execution!", StatusUpdatePopupController.PopupType.ERROR, 5);
            failed.add(target);
        } finally {
            executing.remove(target);
        }
    }

    private ProcessBuilder getTracerouteProcess(String target) {
        ProcessBuilder tracerouteProcess;

        if (Objects.equals(OS, "Linux") || Objects.equals(OS, "linux") || Objects.equals(OS, "macOS") || Objects.equals(OS, "Mac OS X")) {
            tracerouteProcess = new ProcessBuilder("traceroute");
        } else if (Objects.equals(OS, "Windows") || Objects.equals(OS, "windows")) {
            tracerouteProcess = new ProcessBuilder("tracert");
        } else {
            throw new RuntimeException("Error: Invalid OS (" + OS + ")!");
        }

        if (!tracerouteParameters.getHostNameResolution()) {
            tracerouteProcess.command().add("-n");
        }

        tracerouteProcess.command().add("-m");
        tracerouteProcess.command().add(Integer.toString(tracerouteParameters.getMaxHops()));

        tracerouteProcess.command().add("-w");
        tracerouteProcess.command().add(Double.toString(tracerouteParameters.getTimeout()));

        tracerouteProcess.command().add(target);

        return tracerouteProcess;
    }

    public List<String> getPending() {
        return new ArrayList<>(targets);
    }

    private final List<String> executing = new ArrayList<>();
    private final List<String> executed = new ArrayList<>();
    private final List<String> failed = new ArrayList<>();

    public List<String> getExecuting() {
        return executing;
    }

    public List<String> getExecuted() {
        return executed;
    }

    public List<String> getFailed() {
        return failed;
    }

    @Override
    public Statistics getStatistics() {
        Statistics statistics = new Statistics();

        statistics.addStatistic(getNumberPendingStatistic());
        statistics.addStatistic(getNumberExecutingStatistic());
        statistics.addStatistic(getNumberExecutedStatistic());
        statistics.addStatistic(getNumberFailedStatistic());

        return statistics;
    }

    private Statistic<Integer> getNumberPendingStatistic() {
        return new NumericStatistic<>("Number of pending traceroute", targets.size(), "");
    }

    private Statistic<Integer> getNumberExecutingStatistic() {
        return new NumericStatistic<>("Number of executing traceroute", executing.size(), "");
    }

    private Statistic<Integer> getNumberExecutedStatistic() {
        return new NumericStatistic<>("Number of executed traceroute", executed.size(), "");
    }

    private Statistic<Integer> getNumberFailedStatistic() {
        return new NumericStatistic<>("Number of failed traceroute", failed.size(), "");
    }

}
