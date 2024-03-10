package fr.internetcartographier.example;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class WorldWindJavaFXApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("WorldWind JavaFX Example");

        // Create a SwingNode to embed the WorldWind globe
        SwingNode swingNode = new SwingNode();
        createSwingContent(swingNode);

        StackPane pane = new StackPane();
        pane.getChildren().add(swingNode);

        primaryStage.setScene(new Scene(pane, 1000, 800));
        primaryStage.show();
    }

    private void createSwingContent(final SwingNode swingNode) {
        SwingUtilities.invokeLater(() -> {
            WorldWindowGLJPanel wwd = new WorldWindowGLJPanel();
            wwd.setPreferredSize(new Dimension(1000, 800));
            wwd.setModel(new BasicModel());

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(wwd, BorderLayout.CENTER);

            swingNode.setContent(panel);
        });
    }
}
