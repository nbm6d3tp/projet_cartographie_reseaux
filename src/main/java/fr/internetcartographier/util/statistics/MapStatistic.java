package fr.internetcartographier.util.statistics;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class MapStatistic<K, V> extends Statistic<TreeMap<K, V>> {

    private final static double CONTAINER_SPACING = 3;
    private final static double ROW_SPACING = 5;

    private final TreeMap<K, V> rankedValues;

    public MapStatistic(String name, TreeMap<K, V> rankedValues) {
        super(name);

        this.rankedValues = rankedValues;
    }

    @Override
    public TreeMap<K, V> getValue() {
        return rankedValues;
    }

    @Override
    public Node getUIComponent() {
        VBox container = new VBox();
        container.setSpacing(CONTAINER_SPACING);
        container.getStyleClass().add("map-statistic-container");

        Label nameLabel = new Label(getName() + " :");
        nameLabel.getStyleClass().add("map-statistic-name");

        ObservableList<Node> children = container.getChildren();
        children.add(nameLabel);

        int rank = 1;

        for (Map.Entry<K, V> entry : rankedValues.entrySet()) {
            children.add(getRow(rank++, entry.getKey(), entry.getValue()));
        }

        return container;
    }

    private HBox getRow(int rank, K key, V value) {
        HBox row = new HBox();
        row.setSpacing(ROW_SPACING);
        row.getStyleClass().add("map-statistic-row");

        Label rankLabel = new Label(String.valueOf(rank));
        rankLabel.getStyleClass().add("map-statistic-rank");

        Label valueLabel = new Label(value.toString());
        valueLabel.getStyleClass().add("map-statistic-value");

        Label keyLabel = new Label(key.toString());
        keyLabel.getStyleClass().add("map-statistic-key");


        row.getChildren().addAll(rankLabel, keyLabel, valueLabel);

        return row;
    }

    @Override
    public String getResultInString() {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<K, V> entry : getValue().entrySet()) {
            K key = entry.getKey();
            V val = entry.getValue();

            result.append(val.toString()).append(" ").append(key.toString()).append("\\n");
        }

        return result.toString();
    }

}
