package fr.internetcartographier.controller.leftmenubar.tab;

import fr.internetcartographier.controller.Controller;
import fr.internetcartographier.controller.ControllerManager;
import fr.internetcartographier.controller.traceroutemanager.TracerouteManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;

public class QueueTabController extends Controller {

    @FXML
    private VBox pendingTraceroute;

    @FXML
    private VBox executingTraceroute;

    @FXML
    private VBox executedTraceroute;

    @FXML
    private VBox failedTraceroute;

    //private Timeline updateRoutine;

    private final static double UPDATE_ROUTINE_DELAY = 1;

    private final TracerouteManager tracerouteManager;

    public QueueTabController(TracerouteManager tracerouteManager) {
        this.tracerouteManager = tracerouteManager;
    }

    @FXML
    private void initialize() {
        ControllerManager controllerManager = ControllerManager.getInstance();
        controllerManager.registerController("QueueTabController", this);

        //updateRoutine = new Timeline(new KeyFrame(Duration.seconds(UPDATE_ROUTINE_DELAY), event -> update()));
        //updateRoutine.setCycleCount(Timeline.INDEFINITE);

        update();
    }

    public void update() {
        List<String> pending = tracerouteManager.getPending();
        List<String> executing = tracerouteManager.getExecuting();
        List<String> executed = tracerouteManager.getExecuted();
        List<String> failed = tracerouteManager.getFailed();

        pendingTraceroute.getChildren().clear();
        executingTraceroute.getChildren().clear();
        executedTraceroute.getChildren().clear();
        failedTraceroute.getChildren().clear();

        for (String target : pending) {
            pendingTraceroute.getChildren().add(new Label(target));
        }

        for (String target : executing) {
            executingTraceroute.getChildren().add(new Label(target));
        }

        for (String target : executed) {
            executedTraceroute.getChildren().add(new Label(target));
        }

        for (String target : failed) {
            failedTraceroute.getChildren().add(new Label(target));
        }
    }

}
