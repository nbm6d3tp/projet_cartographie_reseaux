package fr.internetcartographier.controller.topmenubar;

import fr.internetcartographier.controller.Controller;
import fr.internetcartographier.controller.topmenubar.option.OptionController;
import fr.internetcartographier.controller.topmenubar.option.SearchController;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class TopMenuBarController extends Controller {

    @FXML
    private HBox optionsContainer;
    @FXML
    private VBox search;
    @FXML
    private SearchController searchController;
    @FXML
    private VBox profile;
    @FXML
    private OptionController profileController;
    @FXML
    private VBox notifications;
    @FXML
    private OptionController notificationsController;
    @FXML
    private VBox settings;
    @FXML
    private OptionController settingsController;

    @FXML
    private void initialize() {
        profile.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> optionsContainer.requestFocus());
        notifications.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> optionsContainer.requestFocus());
        settings.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> optionsContainer.requestFocus());
    }

}
