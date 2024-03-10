package fr.internetcartographier.controller.leftmenubar.tab;

import fr.internetcartographier.controller.Controller;
import fr.internetcartographier.controller.ControllerManager;
import fr.internetcartographier.controller.traceroutemanager.TracerouteManager;
import fr.internetcartographier.model.InternetCartographier;
import fr.internetcartographier.model.internetgraph.InternetGraph;
import fr.internetcartographier.util.statistics.Statistic;
import fr.internetcartographier.util.statistics.Statistics;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class StatisticsTabController extends Controller {

    @FXML
    private VBox applicationStatisticsContainer;

    @FXML
    private VBox graphStatisticsContainer;

    @FXML
    private VBox tracerouteStatisticsContainer;

    private final InternetCartographier internetCartographier;

    private final InternetGraph internetGraph;

    private final TracerouteManager tracerouteManager;

    public StatisticsTabController(InternetCartographier internetCartographier, TracerouteManager tracerouteManager) {
        this.internetCartographier = internetCartographier;
        internetGraph = internetCartographier.getInternetGraph();
        this.tracerouteManager = tracerouteManager;
    }

    @FXML
    private void initialize() {
        ControllerManager controllerManager = ControllerManager.getInstance();
        controllerManager.registerController("StatisticsTabController", this);

        update();
    }

    public void update() {
        updateApplicationStatistics();
        updateGraphStatistics();
        updateTracerouteStatistics();
    }

    private void updateApplicationStatistics() {
        Statistics applicationStatistics = internetCartographier.getStatistics();

        applicationStatisticsContainer.getChildren().clear();

        for (Statistic<?> statistic : applicationStatistics) {
            applicationStatisticsContainer.getChildren().add(statistic.getUIComponent());
        }
    }

    private void updateGraphStatistics() {
        Statistics applicationStatistics = internetGraph.getStatistics();

        graphStatisticsContainer.getChildren().clear();

        for (Statistic<?> statistic : applicationStatistics) {
            graphStatisticsContainer.getChildren().add(statistic.getUIComponent());
        }
    }

    private void updateTracerouteStatistics() {
        Statistics applicationStatistics = tracerouteManager.getStatistics();

        tracerouteStatisticsContainer.getChildren().clear();

        for (Statistic<?> statistic : applicationStatistics) {
            tracerouteStatisticsContainer.getChildren().add(statistic.getUIComponent());
        }
    }

}
