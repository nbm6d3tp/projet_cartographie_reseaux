package fr.internetcartographier.controller.topmenubar.contextmenu;

import fr.internetcartographier.controller.Controller;
import fr.internetcartographier.controller.ControllerManager;
import javafx.animation.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class NotificationsContextMenuController extends Controller {

    @FXML
    private ContextMenu notificationsContextMenu;

    @FXML
    private Label markAllAsReadOption;

    @FXML
    private Label noNotificationIndicator;

    @FXML
    private VBox notificationsContainer;

    @FXML
    private Label viewAllNotificationsOption;

    @FXML
    private void initialize() {
        ControllerManager controllerManager = ControllerManager.getInstance();
        controllerManager.registerController("NotificationsContextMenuController", this);

        notificationsContextMenu.getProperties().put("CONTROLLER", this);

        markAllAsReadOption.setOnMouseClicked(event -> markAllAsReadOption());
        noNotificationIndicator.visibleProperty().bind(noNotification);
        startUpdateElapsedRoutine();
    }

    private void startUpdateElapsedRoutine() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(ELAPSED_ROUTINE_DELAY), event -> updateAllNotificationsElapsed()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private static final double ELAPSED_ROUTINE_DELAY = 5;

    private final Queue<BorderPane> notificationsQueue = new LinkedList<>();

    private final static int MAX_NOTIFICATIONS = 5;

    private final static double SHIFTING_NOTIFICATION_DURATION = 0.1;

    private final static double NOTIFICATION_ADD_DURATION = 0.4;

    private final static double NOTIFICATION_DELETE_DURATION = 0.25;

    private final static int NOTIFICATION_ADD_TRANSLATE_X = 150;

    private ParallelTransition getShiftingNotificationsTransition(Notification notificationModel) {
        Duration duration = Duration.seconds(SHIFTING_NOTIFICATION_DURATION);
        ParallelTransition parallel = new ParallelTransition();

        for (BorderPane notification : notificationsQueue) {
            TranslateTransition translate = new TranslateTransition(duration, notification);
            translate.setByY(notification.getHeight());

            parallel.getChildren().add(translate);
        }

        parallel.setOnFinished(event -> {
            deleteOldestNotification();
            appendNotification(notificationModel);

            for (BorderPane notification : notificationsQueue) {
                notification.setTranslateY(0);
            }

            updateIndicators();
        });


        return parallel;
    }

    private TranslateTransition getTranslateNotificationAdd(BorderPane notification) {
        notification.setTranslateX(NOTIFICATION_ADD_TRANSLATE_X);

        TranslateTransition translate = new TranslateTransition(Duration.seconds(NOTIFICATION_ADD_DURATION), notification);
        translate.setFromX(NOTIFICATION_ADD_TRANSLATE_X);
        translate.setToX(0);

        return translate;
    }

    private FadeTransition getFadeTransition(BorderPane notification) {
        FadeTransition fade = new FadeTransition(Duration.seconds(NOTIFICATION_DELETE_DURATION), notification);
        fade.setFromValue(notification.getOpacity());
        fade.setToValue(0);
        fade.setOnFinished(event -> removeNotification(notification));

        return fade;
    }

    private void appendNotification(Notification notification) {
        BorderPane newNotification = NotificationController.createNotification(notification, this);
        notification.getReadProperty().addListener((obs, oldValue, newValue) -> updateIndicators());

        notificationsQueue.offer(newNotification);
        notificationsContainer.getChildren().add(0, newNotification);

        TranslateTransition translate = getTranslateNotificationAdd(newNotification);
        translate.play();

        updateIndicators();
    }

    private void deleteOldestNotification() {
        deleteNotification(notificationsQueue.poll(), false);
    }

    public void addNotification(Notification newNotification) {
        if (notificationsQueue.size() >= MAX_NOTIFICATIONS) {
            ParallelTransition transition = getShiftingNotificationsTransition(newNotification);
            transition.play();
        } else {
            appendNotification(newNotification);
        }
    }

    public void deleteNotification(BorderPane notification, boolean doFade) {
        if (doFade) {
            FadeTransition fade = getFadeTransition(notification);
            fade.play();
        } else {
            removeNotification(notification);
        }
    }

    private void removeNotification(BorderPane notification) {
        notificationsContainer.getChildren().remove(notification);
        updateIndicators();
    }

    public void bindHasNotificationIndicator(Circle hasNotificationIndicator) {
        hasNotificationIndicator.visibleProperty().bind(oneUnread);
    }

    private void updateAllNotificationsElapsed() {
        iterateNotifications(NotificationController::updateElapsed);
    }

    @FXML
    private void markAllAsReadOption() {
        iterateNotifications(NotificationController::markAsRead);
    }

    @FXML
    private void viewAllNotificationsOption() {
        // TODO Open large parameters view where you can change APP parameters (whats inside config.properties in fact)
        //modalController.openNotifications();
        //applicationController.openNotifications();
        System.out.println("View all notifications");
    }

    private final BooleanProperty noNotification = new SimpleBooleanProperty(true);
    private final BooleanProperty oneUnread = new SimpleBooleanProperty(false);

    private void updateIndicators() {
        noNotification.set(notificationsContainer.getChildren().isEmpty());
        oneUnread.set(notificationsQueue.stream()
                .anyMatch(notification ->
                        !((BooleanProperty) notification.getProperties().get("NOTIFICATION_READ_PROPERTY")).get()));
    }

    private void iterateNotifications(Consumer<NotificationController> action) {
        for (BorderPane notification : notificationsQueue) {
            NotificationController controller = (NotificationController) notification.getProperties().get("CONTROLLER");

            if (controller != null) {
                action.accept(controller);
            }
        }
    }

}
