package fr.internetcartographier.example;

import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.util.BasicDragger;

import javax.swing.*;

public class GlobeGraphApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Globe Graph Example");
            WorldWindowGLJPanel wwd = new WorldWindowGLJPanel();
            wwd.setModel(new BasicModel());
            frame.add(wwd);
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            RenderableLayer layer = new RenderableLayer();
            wwd.getModel().getLayers().add(layer);

            // Add points
            addPoint(layer, Position.fromDegrees(40.0, -100.0), "Point 1");
            addPoint(layer, Position.fromDegrees(45.0, -110.0), "Point 2");

            // Add lines
            addLine(layer, Position.fromDegrees(40.0, -100.0), Position.fromDegrees(45.0, -110.0));

            wwd.addSelectListener(new BasicDragger(wwd));
            frame.setVisible(true);
        });
    }

    private static void addPoint(RenderableLayer layer, Position position, String label) {
        PointPlacemark pointPlacemark = new PointPlacemark(position);
        pointPlacemark.setLabelText(label);
        pointPlacemark.setLineEnabled(false);
        pointPlacemark.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
        layer.addRenderable(pointPlacemark);
    }

    private static void addLine(RenderableLayer layer, Position startPos, Position endPos) {
        Path path = new Path(startPos, endPos);
        path.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
        ShapeAttributes attrs = new BasicShapeAttributes();
        attrs.setOutlineMaterial(new Material(java.awt.Color.RED));
        path.setAttributes(attrs);
        layer.addRenderable(path);
    }
}

