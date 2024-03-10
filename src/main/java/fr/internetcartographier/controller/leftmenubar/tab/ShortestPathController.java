package fr.internetcartographier.controller.leftmenubar.tab;

import fr.internetcartographier.controller.Controller;
import fr.internetcartographier.controller.ControllerManager;
import fr.internetcartographier.model.InternetCartographier;
import fr.internetcartographier.model.WeightMetric;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import fr.internetcartographier.model.internetgraph.InternetGraph;
import fr.internetcartographier.model.internetgraph.Node;
import fr.internetcartographier.util.path.Path;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.util.Set;
import java.util.stream.Collectors;

public class ShortestPathController extends Controller {

    @FXML
    private ComboBox<IPAddress> shortestPathFrom;

    @FXML
    private ComboBox<IPAddress> shortestPathTo;

    @FXML
    private VBox shortestPathResult;

    @FXML
    private Label displayMessage;

    @FXML
    private Button shortestPathButton;

    private final InternetCartographier internetCartographier;

    public ShortestPathController(InternetCartographier internetCartographier) {
        this.internetCartographier = internetCartographier;

        clearDisplayMessage = new Timeline(new KeyFrame(Duration.seconds(ERROR_MESSAGE_DISPLAY_DURATION), event -> displayMessage.setText("")));
        clearDisplayMessage.setCycleCount(1);
    }

    @FXML
    private void initialize() {
        ControllerManager controllerManager = ControllerManager.getInstance();
        controllerManager.registerController("ShortestPathController", this);

        initializeComboBoxes();

        shortestPathButton.setOnAction(event -> calculateShortestPath());

        update();
    }

    private void calculateShortestPath() {
        if (validateInputs()) {
            InternetGraph internetGraph = internetCartographier.getInternetGraph();
            WeightMetric weightMetric = internetCartographier.getWeightMetric();
            GeolocationService geolocationService = internetCartographier.getGeolocationService();

            Path<String> path = internetGraph.minimumDistance(shortestPathFrom.getValue(), shortestPathTo.getValue(), weightMetric, geolocationService.getClass());

            shortestPathResult.getChildren().clear();

            for (String node : path) {
                shortestPathResult.getChildren().add(new Label(node));
            }

            displayMessage("Shortest path successfully calculated!", false);
        }
    }

    private boolean validateInputs() {
        if (shortestPathFrom.getValue() == null) {
            displayMessage("Select a From IPAddress!", true);
            return false;
        }

        if (shortestPathTo.getValue() == null) {
            displayMessage("Select a To IPAddress!", true);
            return false;
        }

        return true;
    }

    public void update() {
        populateComboBoxes();
    }

    private void populateComboBoxes() {
        InternetGraph internetGraph = internetCartographier.getInternetGraph();
        Set<IPAddress> ipAddresses = internetGraph.getNodes().stream()
                .map(Node::getIpAddress)
                .collect(Collectors.toSet());
        ObservableList<IPAddress> ipAddressesList = FXCollections.observableArrayList(ipAddresses);

        shortestPathFrom.setItems(ipAddressesList);
        shortestPathTo.setItems(ipAddressesList);

        IPAddress originIPAddress = internetCartographier.getOrigin().getIpAddress();
        shortestPathFrom.setValue(originIPAddress);
    }

    private void initializeComboBoxes() {
        StringConverter<IPAddress> ipAddressConverter = new StringConverter<>() {
            @Override
            public String toString(IPAddress object) {
                return object == null ? null : object.getStringIpAddress();
            }

            @Override
            public IPAddress fromString(String string) {
                return null;
            }
        };

        shortestPathFrom.setConverter(ipAddressConverter);
        shortestPathTo.setConverter(ipAddressConverter);

        populateComboBoxes();
    }

    private final static double ERROR_MESSAGE_DISPLAY_DURATION = 5;

    private final Timeline clearDisplayMessage;

    private void displayMessage(String message, boolean error) {
        displayMessage.getStyleClass().removeAll("success-message", "error-message");

        if (error) {
            displayMessage.getStyleClass().add("error-message");
        } else {
            displayMessage.getStyleClass().add("success-message");
        }

        displayMessage.setText(message);

        clearDisplayMessage.stop();
        clearDisplayMessage.play();
    }

}
