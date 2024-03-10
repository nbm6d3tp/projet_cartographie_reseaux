package fr.internetcartographier.controller.hud;


import fr.internetcartographier.controller.Controller;
import fr.internetcartographier.controller.ControllerManager;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Arrays;

/**
 * Controls the status update pop-up display in a graphical user interface.
 * Handles displaying different types of messages and their associated icons.
 * Manages message expiration and animations.
 *
 * @author Nelson PROIA (nelson.proia@dauphine.eu)
 * Université Paris Dauphine-PSL
 */
public class StatusUpdatePopupController extends Controller {

    /**
     * Maximum length allowed for the message displayed in the pop-up.
     */
    private final static int MESSAGE_MAX_LENGTH = 100;

    /**
     * Duration for the fade animation.
     */
    private final static double FADE_DURATION = 0.2;

    /**
     * The container for the status update pop-up.
     */
    @FXML
    private VBox statusUpdatePopup;

    /**
     * Container for the loading bar within the status update pop-up.
     */
    @FXML
    private StackPane statusUpdatePopupLoadingBarContainer;

    /**
     * Loading bar within the status update pop-up.
     */
    @FXML
    private StackPane statusUpdatePopupLoadingBar;

    /**
     * Container for the status pop-up message.
     */
    @FXML
    private HBox statusPopupMessageContainer;

    /**
     * Icon displayed in the status update pop-up.
     */
    @FXML
    private ImageView statusUpdatePopupIcon;

    /**
     * Label displaying the message in the status update pop-up.
     */
    @FXML
    private Label statusUpdatePopupMessage;

    /**
     * Sequential animation for message expiration.
     */
    private SequentialTransition expirationAnimation;

    /**
     * Initial status pop-up opacity.
     */
    private double initialOpacity;

    /**
     * Initializes the status update pop-up.
     */
    @FXML
    private void initialize() {
        ControllerManager controllerManager = ControllerManager.getInstance();
        controllerManager.registerController("StatusUpdatePopupController", this);

        initialOpacity = statusUpdatePopup.getOpacity();

        statusUpdatePopupLoadingBarContainer.prefWidthProperty().bind(statusPopupMessageContainer.widthProperty());

        statusUpdatePopup.setOnMouseClicked(event -> {
            hideMessage();
            event.consume();
        });
    }

    /**
     * Displays a temporary message in the pop-up for a specific duration.
     *
     * @param message The message to display.
     * @param type    The type of the message (SUCCESS, ERROR, WARNING, INFORMATION, LOADING).
     * @param seconds The duration in seconds for which the message will be displayed.
     * @throws IllegalArgumentException If the message length exceeds the maximum allowed length.
     */
    public void displayTemporaryMessage(String message, PopupType type, double seconds) throws IllegalArgumentException {
        updateMessage(message, type);

        statusUpdatePopup.setVisible(true);
        statusUpdatePopupLoadingBarContainer.setVisible(true);

        if (expirationAnimation == null) {
            expirationAnimation = new SequentialTransition(getFadeIn(), getTimeline(seconds), getFadeOut());
        } else {
            expirationAnimation.stop();
            expirationAnimation = new SequentialTransition(getTimeline(seconds), getFadeOut());
        }

        expirationAnimation.play();
    }

    /**
     * Displays a static message in the pop-up without an expiration.
     *
     * @param message The message to display.
     * @param type    The type of the message (SUCCESS, ERROR, WARNING, INFORMATION, LOADING).
     * @throws IllegalArgumentException If the message length exceeds the maximum allowed length.
     */
    public void displayMessage(String message, PopupType type) throws IllegalArgumentException {
        updateMessage(message, type);

        statusUpdatePopup.getStyleClass().add("popup-static-message");

        statusUpdatePopup.setVisible(true);
        statusUpdatePopupLoadingBarContainer.setVisible(false);

        if (expirationAnimation != null) {
            expirationAnimation.stop();
            expirationAnimation = null;
        }

        getFadeIn().play();
    }

    /**
     * Updates the message and its associated type in the pop-up.
     *
     * @param message The message to display.
     * @param type    The type of the message (SUCCESS, ERROR, WARNING, INFORMATION, LOADING).
     * @throws IllegalArgumentException If the message length exceeds the maximum allowed length.
     */
    private void updateMessage(String message, PopupType type) throws IllegalArgumentException {
        if (message.length() > MESSAGE_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("Error : Message length too long for popup message (%d)!", message.length()));
        }

        statusUpdatePopup.getStyleClass().removeAll(PopupType.getAllClasses());
        statusUpdatePopup.getStyleClass().remove("popup-static-message");

        statusUpdatePopup.getStyleClass().add(type.getStyleClass());

        statusUpdatePopupIcon.setImage(type.getIcon());
        statusUpdatePopupMessage.setText(message);

        statusUpdatePopup.layout();
    }

    /**
     * Hides the currently displayed message in the pop-up.
     */
    public void hideMessage() {
        if (expirationAnimation != null) {
            expirationAnimation.stop();
            expirationAnimation = null;
        }

        getFadeOut().play();
    }

    /**
     * Creates and returns a fade-in animation.
     *
     * @return The fade-in animation.
     */
    private FadeTransition getFadeIn() {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(FADE_DURATION), statusUpdatePopup);

        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(initialOpacity);

        return fadeIn;
    }

    /**
     * Creates and returns a fade-out animation.
     *
     * @return The fade-out animation.
     */
    private FadeTransition getFadeOut() {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(FADE_DURATION), statusUpdatePopup);

        fadeOut.setFromValue(initialOpacity);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            statusUpdatePopup.setVisible(false);
            statusUpdatePopupLoadingBarContainer.setVisible(false);
        });

        return fadeOut;
    }

    /**
     * Creates and returns a timeline for the loading bar animation.
     *
     * @param seconds The duration in seconds for the loading bar animation.
     * @return The timeline for the loading bar animation.
     */
    private Timeline getTimeline(double seconds) {
        return new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(statusUpdatePopupLoadingBar.prefWidthProperty(), 0)),
                new KeyFrame(Duration.seconds(seconds), new KeyValue(statusUpdatePopupLoadingBar.prefWidthProperty(), statusUpdatePopupLoadingBarContainer.getPrefWidth(), Interpolator.EASE_OUT))
        );
    }

    /**
     * Enum defining different types of pop-up messages with associated icons and style classes.
     */
    public enum PopupType {

        SUCCESS("resources/icons/success.png", "popup-success"),
        ERROR("resources/icons/error.png", "popup-error"),
        WARNING("resources/icons/warning.png", "popup-warning"),
        INFORMATION("resources/icons/information.png", "popup-information"),
        LOADING("resources/icons/animated_globe.gif", "popup-loading");

        /**
         * The image icon associated with the type.
         */
        private final Image icon;

        /**
         * The CSS style class associated with the type.
         */
        private final String styleClass;

        /**
         * Constructor for PopupType.
         *
         * @param iconPath   The path to the icon associated with the type.
         * @param styleClass The CSS style class associated with the type.
         */
        PopupType(String iconPath, String styleClass) {
            icon = new Image(iconPath);
            this.styleClass = styleClass;
        }

        /**
         * Gets all the CSS style classes associated with all types.
         *
         * @return An array of all the CSS style classes.
         */
        private static String[] getAllClasses() {
            return Arrays.stream(PopupType.values())
                    .map(PopupType::getStyleClass)
                    .toArray(String[]::new);
        }

        /**
         * Gets the icon associated with this type.
         *
         * @return The image icon associated with the type.
         */
        public Image getIcon() {
            return icon;
        }

        /**
         * Gets the CSS style class associated with this type.
         *
         * @return The CSS style class associated with the type.
         */
        public String getStyleClass() {
            return styleClass;
        }

    }

}
