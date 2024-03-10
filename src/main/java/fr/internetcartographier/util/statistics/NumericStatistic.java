package fr.internetcartographier.util.statistics;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class NumericStatistic<T> extends Statistic<T> {

    private final static double SPACING = 5;

    private final T value;

    private final String unit;

    public NumericStatistic(String name, T value, String unit) {
        super(name);

        this.value = value;
        this.unit = unit;
    }

    public T getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public HBox getUIComponent() {
        HBox container = new HBox();
        container.setSpacing(SPACING);
        container.getStyleClass().add("statistic-container");

        Label nameLabel = new Label(getName() + " :");
        nameLabel.getStyleClass().add("statistic-name");

        Label valueLabel = new Label(value.toString() + " " + unit);
        valueLabel.getStyleClass().add("statistic-value");

        container.getChildren().addAll(nameLabel, valueLabel);

        return container;
    }

    @Override
    public String getResultInString() {
        return getValue().toString();
    }

}
