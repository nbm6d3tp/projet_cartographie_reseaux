package fr.internetcartographier.controller.topmenubar.option;

import fr.internetcartographier.controller.topmenubar.contextmenu.NotificationsContextMenuController;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

public class NotificationsController extends OptionController {

    @FXML
    private Circle notificationsIndicator;

    @Override
    @FXML
    protected void initialize() {
        super.initialize();

        ((NotificationsContextMenuController) contextMenuController).bindHasNotificationIndicator(notificationsIndicator);
    }

}
