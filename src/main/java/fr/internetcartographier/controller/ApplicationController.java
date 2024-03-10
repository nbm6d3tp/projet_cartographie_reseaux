package fr.internetcartographier.controller;

import fr.internetcartographier.controller.globe.GlobeController;
import fr.internetcartographier.model.InternetCartographier;
import fr.internetcartographier.model.tracerouteresults.TracerouteResult;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ApplicationController extends Controller {

    private final InternetCartographier internetCartographier;

    public ApplicationController(InternetCartographier internetCartographier) {
        this.internetCartographier = internetCartographier;
    }

    @FXML
    private void initialize() {
        ControllerManager controllerManager = ControllerManager.getInstance();
        controllerManager.registerController("ApplicationController", this);
    }

    public synchronized void update(TracerouteResult tracerouteResult) {
        internetCartographier.update(tracerouteResult);

        ControllerManager controllerManager = ControllerManager.getInstance();
        GlobeController globeController = (GlobeController) controllerManager.getController("GlobeController");

        globeController.update(tracerouteResult);
    }

    public void quit() {
        ControllerManager controllerManager = ControllerManager.getInstance();
        SignInController signInController = (SignInController) controllerManager.getController("SignInController");
        signInController.logout();
        System.exit(0);
    }

}
