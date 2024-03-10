package fr.internetcartographier.controller.topmenubar.contextmenu;

import fr.internetcartographier.controller.Controller;
import fr.internetcartographier.controller.ControllerManager;
import fr.internetcartographier.controller.globe.GlobeController;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class SearchContextMenuController extends Controller {

    @FXML
    private ContextMenu searchContextMenu;

    @FXML
    private VBox searchContextMenuContent;

    @FXML
    private Label searchResultsNumber;

    @FXML
    private ScrollPane searchResults;

    @FXML
    private VBox searchResultsContainer;

    @FXML
    private void initialize() {
        ControllerManager controllerManager = ControllerManager.getInstance();
        controllerManager.registerController("SearchContextMenuController", this);
    }

    public void setContextMenuSize(double width) {
        searchContextMenuContent.setPrefWidth(width - 2);
    }

    public void updateSearchResult(List<IPAddress> ipAddresses) {
        searchResultsContainer.getChildren().clear();

        if (ipAddresses.isEmpty()) {
            searchResultsNumber.setText("No result found");
            searchResults.setManaged(false);
        } else {
            searchResultsNumber.setText(ipAddresses.size() + " result" + (ipAddresses.size() > 1 ? "s" : "") + " found");
            searchResults.setManaged(true);
        }

        for (IPAddress ipAddress : ipAddresses) {
            searchResultsContainer.getChildren().add(getSearchResultComponent(ipAddress));
        }
    }

    private Label getSearchResultComponent(IPAddress ipAddress) {
        Label label = new Label();

        label.setMaxWidth(Double.MAX_VALUE);

        StringBuilder searchString = new StringBuilder(ipAddress.getStringIpAddress());
        Optional<String> domainName = ipAddress.getDomainName();

        if (domainName.isPresent()) {
            searchString.append("(").append(domainName.get()).append(") - Node");
        } else {
            searchString.append(" - Node");
        }

        label.setText(searchString.toString());

        label.setCursor(Cursor.HAND);

        label.getStyleClass().addAll("text", "search-result");

        VBox.setMargin(label, new Insets(0));
        label.setPadding(new Insets(2.0, 4.0, 2.0, 4.0));

        ControllerManager controllerManager = ControllerManager.getInstance();
        GlobeController globeController = (GlobeController) controllerManager.getController("GlobeController");

        label.setOnMouseClicked(event -> globeController.moveViewOn(ipAddress.getStringIpAddress()));

        return label;
    }

}
