package fr.internetcartographier.controller.leftmenubar;

import fr.internetcartographier.controller.Controller;
import fr.internetcartographier.controller.ControllerManager;
import fr.internetcartographier.controller.leftmenubar.tab.QueueTabController;
import fr.internetcartographier.controller.leftmenubar.tab.ShortestPathController;
import fr.internetcartographier.controller.leftmenubar.tab.StatisticsTabController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class LeftMenuBarController extends Controller {

    @FXML
    private VBox targets;
    @FXML
    private StackPane targetsIndicator;

    @FXML
    private VBox queue;
    @FXML
    private StackPane queueIndicator;

    @FXML
    private VBox shortestPath;
    @FXML
    private StackPane shortestPathIndicator;

    @FXML
    private VBox statistics;
    @FXML
    private StackPane statisticsIndicator;

    @FXML
    private ImageView expand;

    @FXML
    private StackPane content;
    @FXML
    private VBox targetsTab;
    @FXML
    private VBox queueTab;
    @FXML
    private VBox shortestPathTab;
    @FXML
    private VBox statisticsTab;

    @FXML
    private void initialize() {
        animations = new HashMap<>();

        expand.setOnMouseClicked(event -> {
            //open expanded menu view (or just make the glob disapear and let it extand itself
        });

        initializeTab(targets, targetsTab, targetsIndicator);
        initializeTab(queue, queueTab, queueIndicator);
        initializeTab(shortestPath, shortestPathTab, shortestPathIndicator);
        initializeTab(statistics, statisticsTab, statisticsIndicator);

        selectedTab = targets;
        selectedIndicator = targetsIndicator;

        selectTab(targets, targetsTab, targetsIndicator);
        selectedIndicator.setMaxWidth(Region.USE_COMPUTED_SIZE);
    }

    private VBox selectedTab;
    private StackPane selectedIndicator;


    private void initializeTab(VBox tab, VBox tabContent, StackPane indicator) {
        tab.setOnMouseEntered(event -> hover(tab, indicator));
        tab.setOnMouseExited(event -> cancelHover(tab, indicator));
        tab.setOnMouseClicked(event -> {
            selectTab(tab, tabContent, indicator);
            update();
        });
    }

    private void update() {
        ControllerManager controllerManager = ControllerManager.getInstance();
        QueueTabController queueTabController = (QueueTabController) controllerManager.getController("QueueTabController");
        ShortestPathController shortestPathController = (ShortestPathController) controllerManager.getController("ShortestPathController");
        StatisticsTabController statisticsTabController = (StatisticsTabController) controllerManager.getController("StatisticsTabController");

        queueTabController.update();
        shortestPathController.update();
        statisticsTabController.update();
    }

    private Map<StackPane, Timeline> animations;

    private void hover(VBox tab, StackPane indicator) {
        if (tab != selectedTab) {
            Timeline animation = getHoverAnimation(indicator, tab.getWidth());
            animations.put(indicator, animation);

            indicator.setPrefWidth(0);
            tab.setOpacity(1);
            animation.play();
        }
    }

    private void cancelHover(VBox tab, StackPane indicator) {
        if (tab != selectedTab) {
            if (animations.containsKey(indicator)) {
                animations.get(indicator).stop();
            }

            tab.setOpacity(0.8);
            indicator.setPrefWidth(0);
        }
    }

    private void selectTab(VBox tab, VBox tabContent, StackPane indicator) {
        selectedTab.setOpacity(0.8);
        selectedIndicator.setPrefWidth(0);
        selectedIndicator.setMaxWidth(Region.USE_PREF_SIZE);

        tab.setOpacity(1);
        indicator.setPrefWidth(tab.getWidth());

        content.getChildren().clear();
        content.getChildren().add(tabContent);

        selectedTab = tab;
        selectedIndicator = indicator;
    }

    private final static double HOVER_ANIMATION_DURATION = 0.1;

    private Timeline getHoverAnimation(StackPane indicator, double width) {
        return new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(indicator.prefWidthProperty(), 0)),
                new KeyFrame(Duration.seconds(HOVER_ANIMATION_DURATION), new KeyValue(indicator.prefWidthProperty(), width))
        );
    }

}
