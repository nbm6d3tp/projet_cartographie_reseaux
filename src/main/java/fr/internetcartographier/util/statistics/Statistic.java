package fr.internetcartographier.util.statistics;

import javafx.scene.Node;

public abstract class Statistic<T> {

	private final String name;

	protected Statistic(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract T getValue();

	public abstract Node getUIComponent();

	public abstract String getResultInString();

}
