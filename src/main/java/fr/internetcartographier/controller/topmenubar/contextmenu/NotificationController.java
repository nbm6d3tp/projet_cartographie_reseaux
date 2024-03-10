package fr.internetcartographier.controller.topmenubar.contextmenu;

import fr.internetcartographier.controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotificationController extends Controller {

    @FXML
    private BorderPane notification;

    @FXML
    private ImageView notificationIcon;

    @FXML
    private Label notificationText;

    @FXML
    private Label notificationDate;

    @FXML
    private ImageView notificationDelete;

    private Runnable onClose;

    private Notification notificationModel;

    public static BorderPane createNotification(Notification notificationModel, NotificationsContextMenuController notificationsContextMenuController) {
        try {
            FXMLLoader loader = new FXMLLoader(NotificationController.class.getResource("../../../../../resources/fxml/top_menu_bar/context_menu/notification.fxml"));

            BorderPane notificationComponent = loader.load();
            NotificationController controller = loader.getController();

            notificationComponent.getProperties().put("CONTROLLER", controller);
            notificationComponent.getProperties().put("NOTIFICATION_ID", notificationModel.getId());
            notificationComponent.getProperties().put("NOTIFICATION_READ_PROPERTY", notificationModel.getReadProperty());

            controller.notificationModel = notificationModel;
            controller.notificationIcon.setImage(notificationModel.getType().getIcon());
            controller.notificationText.setText(notificationModel.getText());
            controller.onClose = () -> notificationsContextMenuController.deleteNotification(notificationComponent, true);
            controller.updateElapsed();

            return notificationComponent;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String convertElapsedToText(long timeDifferenceMillis) {
        if (timeDifferenceMillis < 10000) {
            return "Now";
        } else if (timeDifferenceMillis < 60000) {
            return (timeDifferenceMillis / 1000) + "s ago";
        } else if (timeDifferenceMillis < 3600000) {
            return (timeDifferenceMillis / 60000) + "m ago";
        } else if (timeDifferenceMillis < 86400000) {
            return (timeDifferenceMillis / 3600000) + "h ago";
        } else {
            Date date = new Date(timeDifferenceMillis);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

            return sdf.format(date);
        }
    }

    @FXML
    private void initialize() {
        notification.setOnMouseEntered(event -> notificationDelete.setVisible(true));
        notification.setOnMouseExited(event -> notificationDelete.setVisible(false));

        notification.setOnMouseClicked(event -> {
            // TODO Open it in the main notifications view.
            //application.openNotification(notificationModel);
            //application.openNotification(notificationModel.getId());
            markAsRead();
        });

        notificationDelete.setOnMouseClicked(event -> delete());
    }

    @FXML
    private void delete() {
        onClose.run();
    }

    public void updateElapsed() {
        long elapsed = System.currentTimeMillis() - notificationModel.getDate();

        notificationDate.setText(convertElapsedToText(elapsed));
    }

    public void markAsRead() {
        notification.getStyleClass().remove("unread-notification");
        notificationModel.read();
    }

}
