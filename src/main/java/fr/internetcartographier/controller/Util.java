package fr.internetcartographier.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Util {

    public static <T extends Enum<T>> ObservableList<T> getEnumValues(Class<T> enumClass) {
        T[] enumConstants = enumClass.getEnumConstants();

        return FXCollections.observableArrayList(enumConstants);
    }

}
