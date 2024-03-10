package fr.internetcartographier.controller.topmenubar.option;

import fr.internetcartographier.controller.ControllerManager;
import fr.internetcartographier.controller.topmenubar.contextmenu.SearchContextMenuController;
import fr.internetcartographier.model.InternetCartographier;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.List;
import java.util.Objects;

public class SearchController extends OptionController {

    private static final double SEARCH_BAR_SIZE = 300;
    private final static double SEARCH_BAR_ANIMATION_DURATION = 0.4;

    @FXML
    private StackPane searchBar;
    @FXML
    private TextField searchTextField;

    private boolean isSearchBarDisplayed = false;
    private Timeline displaySearchBarTimeline;
    private Timeline hideSearchBarTimeline;

    private final InternetCartographier internetCartographier;

    public SearchController(InternetCartographier internetCartographier) {
        this.internetCartographier = internetCartographier;
    }

    @Override
    @FXML
    protected void initialize() {
        super.initialize();

        displaySearchBarTimeline = getSearchBarAnimation(false);
        hideSearchBarTimeline = getSearchBarAnimation(true);

        ((SearchContextMenuController) contextMenuController).setContextMenuSize(SEARCH_BAR_SIZE);

        Node contextMenuNode = contextMenu.getStyleableNode();

        contextMenuNode.setOnMouseExited(null);

        option.setOnMouseClicked(event -> displaySearchBar());
        option.setOnMouseEntered(event -> {
            if (!isSearchBarDisplayed) {
                hover();
            }
        });
        option.setOnMouseExited(event -> {
            if (!isSearchBarDisplayed) {
                cancelHover();
            }
        });

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Objects.equals(newValue, "")) {
                hideContextMenu();
            } else {
                displayContextMenu();

                ControllerManager controllerManager = ControllerManager.getInstance();
                SearchContextMenuController searchContextMenuController = (SearchContextMenuController) controllerManager.getController("SearchContextMenuController");

                List<IPAddress> results = internetCartographier.searchIPAddresses(newValue);
                searchContextMenuController.updateSearchResult(results);
            }
        });

        searchTextField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                hideSearchBar();
            }
        });
    }

    @Override
    protected void displayContextMenu() {
        contextMenu.show(option, Side.BOTTOM, 0, 3);
    }

    private void displaySearchBar() {
        if (!isSearchBarDisplayed) {
            cancelHover();
            icon.setOpacity(1);
            option.setAlignment(Pos.CENTER_LEFT);
            displaySearchBarTimeline.play();
        }
    }

    private void hideSearchBar() {
        if (isSearchBarDisplayed) {
            searchTextField.clear();
            icon.setOpacity(0.8);
            searchBar.setVisible(false);
            hideContextMenu();
            hideSearchBarTimeline.play();
        }
    }

    private Timeline getSearchBarAnimation(boolean reverse) {
        double initialWidth = reverse ? SEARCH_BAR_SIZE : 0;
        double finalWidth = reverse ? 0 : SEARCH_BAR_SIZE;
        double searchFinalWidth = reverse ? icon.getFitWidth() : SEARCH_BAR_SIZE;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(option.prefWidthProperty(), initialWidth), new KeyValue(indicator.prefWidthProperty(), initialWidth)),
                new KeyFrame(Duration.seconds(SEARCH_BAR_ANIMATION_DURATION), new KeyValue(option.prefWidthProperty(), searchFinalWidth), new KeyValue(indicator.prefWidthProperty(), finalWidth))
        );

        if (reverse) {
            timeline.setOnFinished(event -> isSearchBarDisplayed = false);
        } else {
            timeline.setOnFinished(event -> {
                isSearchBarDisplayed = true;
                searchBar.setVisible(true);
                searchTextField.requestFocus();
            });
        }

        return timeline;
    }

}
