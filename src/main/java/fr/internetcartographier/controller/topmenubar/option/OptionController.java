package fr.internetcartographier.controller.topmenubar.option;

import fr.internetcartographier.controller.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class OptionController extends Controller {

    private final static double HOVER_ANIMATION_DURATION = 0.1;

    @FXML
    protected ContextMenu contextMenu;
    @FXML
    protected Controller contextMenuController;

    @FXML
    protected VBox option;
    @FXML
    protected ImageView icon;
    @FXML
    protected StackPane indicator;

    private Timeline hoverAnimation;

    @FXML
    protected void initialize() {
        hoverAnimation = getHoverAnimation(icon.getFitWidth());

        Node contextMenuNode = contextMenu.getStyleableNode();

        contextMenuNode.setOnMouseExited(event -> {
            if (isMouseNotOverNode(option, event.getScreenX(), event.getScreenY())) {
                hideContextMenu();
                cancelHover();
            }
        });

        option.setOnMouseEntered(event -> {
            if (!contextMenu.isShowing()) {
                hover();
                displayContextMenu();
            }
        });

        option.setOnMouseExited(event -> {
            if (isMouseNotOverNode(contextMenuNode, event.getScreenX(), event.getScreenY())) {
                hideContextMenu();
                cancelHover();
            }
        });
    }

    public Controller getContextMenuController() {
        return contextMenuController;
    }

    protected void hover() {
        icon.setOpacity(1);
        hoverAnimation.play();
    }

    protected void cancelHover() {
        hoverAnimation.stop();
        icon.setOpacity(0.8);
        indicator.setPrefWidth(0);
    }

    protected void displayContextMenu() {
        displayBottomRightContextMenu();
    }

    private void displayBottomRightContextMenu() {
        Bounds boundsInScreen = option.localToScreen(option.getBoundsInLocal());

        double x = boundsInScreen.getMaxX();
        double y = boundsInScreen.getMaxY();

        contextMenu.show(option, 0, 0);

        double contextMenuSize = ((Region) contextMenu.getSkin().getNode()).getWidth();

        contextMenu.setAnchorX(x - contextMenuSize);
        contextMenu.setAnchorY(y + 3);
    }

    protected void hideContextMenu() {
        contextMenu.hide();
    }

    private Timeline getHoverAnimation(double width) {
        return new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(indicator.prefWidthProperty(), indicator.getWidth())),
                new KeyFrame(Duration.seconds(HOVER_ANIMATION_DURATION), new KeyValue(indicator.prefWidthProperty(), width))
        );
    }

    public static boolean isMouseNotOverNode(Node node, double x, double y) {
        Bounds bounds = node.localToScreen(node.getBoundsInLocal());

        return (bounds != null && !bounds.contains(x, y));
    }

}
