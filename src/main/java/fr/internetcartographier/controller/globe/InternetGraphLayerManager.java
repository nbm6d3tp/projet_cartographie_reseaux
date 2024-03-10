package fr.internetcartographier.controller.globe;

import fr.internetcartographier.model.InternetCartographier;
import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.internetgraph.Edge;
import fr.internetcartographier.model.internetgraph.InternetGraph;
import fr.internetcartographier.model.internetgraph.Node;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.*;
import javafx.util.Pair;

import java.awt.*;
import java.util.*;
import java.util.List;

public class InternetGraphLayerManager {

    private final RenderableLayer pathLayer;

    private final Map<String, PointPlacemark> pointPlacemarks;
    private final Map<Path, Pair<String, String>> edges;
    private final Map<Path, Color> colors;
    private final Map<Path, Boolean> directs;

    private final WorldWindowGLJPanel wwd;

    public InternetGraphLayerManager(WorldWindowGLJPanel wwd) {
        this.wwd = wwd;
        pathLayer = new RenderableLayer();
        wwd.getModel().getLayers().add(pathLayer);

        pointPlacemarks  = new HashMap<>();
        edges = new HashMap<>();
        colors = new HashMap<>();
        directs = new HashMap<>();
    }

    public void addEdge(String ipAddressA, String ipAddressB, Color color, boolean dashed) {
        Position start = pointPlacemarks.get(ipAddressA).getPosition();
        Position end = pointPlacemarks.get(ipAddressB).getPosition();

        if (start == null || end == null) {
            return;
        }

        if (dashed) {
            List<Position> positions = calculatePositions(start, end);

            for (int i = 0; i < positions.size() - 1; i += 2) {
                Position p1 = positions.get(i);
                Position p2 = positions.get(i + 1);

                Path path = new Path(Arrays.asList(p1, p2));
                path.setAltitudeMode(WorldWind.ABSOLUTE);
                path.setFollowTerrain(true);
                path.setPathType(AVKey.LINEAR);

                ShapeAttributes attrs = new BasicShapeAttributes();
                attrs.setOutlineMaterial(new Material(color));
                attrs.setOutlineWidth(2);

                attrs.setOutlineStipplePattern((short) 0xF0F0);

                path.setAttributes(attrs);
                pathLayer.addRenderable(path);
                edges.put(path, new Pair<>(ipAddressA, ipAddressB));
                colors.put(path, color);
                directs.put(path, true);
            }
        } else {
            Path path = new Path(Arrays.asList(start, end));

            path.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
            path.setPathType(AVKey.GREAT_CIRCLE);
            path.setFollowTerrain(true);

            ShapeAttributes dashedAttributes = new BasicShapeAttributes();
            dashedAttributes.setOutlineMaterial(Material.MAGENTA);
            dashedAttributes.setOutlineWidth(2);
            dashedAttributes.setOutlineStipplePattern((short) 0xAAAA);
            dashedAttributes.setOutlineStippleFactor(5);
            path.setAttributes(dashedAttributes);

            edges.put(path, new Pair<>(ipAddressA, ipAddressB));
            colors.put(path, color);
            directs.put(path, false);
        }

        wwd.redraw();
    }

    public void addNode(double latitude, double longitude, String ipAddress) {
        if (!pointPlacemarks.containsKey(ipAddress)) {
            Position position = Position.fromDegrees(latitude, longitude, 0);
            PointPlacemark node = new PointPlacemark(position);

            node.setLabelText(ipAddress);
            node.setLineEnabled(false);
            node.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);

            pathLayer.addRenderable(node);
            pointPlacemarks.put(ipAddress, node);
        }

        wwd.redraw();
    }

    private List<Position> calculatePositions(Position startPos, Position endPos) {
        List<Position> positions = new ArrayList<>();

        int numPositions = 50;

        for (int i = 0; i <= numPositions; i++) {
            double pathLat = startPos.getLatitude().degrees + (endPos.getLatitude().degrees - startPos.getLatitude().degrees) * i / numPositions;
            double pathLon = startPos.getLongitude().degrees + (endPos.getLongitude().degrees - startPos.getLongitude().degrees) * i / numPositions;
            double pathAlt = startPos.getAltitude() + (endPos.getAltitude() - startPos.getAltitude()) * i / numPositions;

            positions.add(Position.fromDegrees(pathLat, pathLon, pathAlt));
        }

        return positions;
    }

    public void updateImport(InternetCartographier internetCartographier, InternetGraph internetGraph) {
        pathLayer.removeAllRenderables();

        Set<Node> nodes = internetGraph.getNodes();
        Set<Edge> edges = internetGraph.getEdges();

        for (Node node : nodes) {
            Optional<GeolocationData> geolocationData = node.getGeolocationData(internetCartographier.getGeolocationService().getClass());

            geolocationData.ifPresent(data -> addNode(data.getLatitude(), data.getLongitude(), node.getStringIpAddress()));
        }

        for (Edge edge : edges) {
            addEdge(edge.getNodeA().getStringIpAddress(), edge.getNodeB().getStringIpAddress(), GlobeController.getRandomBasicColor(), true);
        }

        wwd.redraw();
    }

    public void refresh(InternetCartographier internetCartographier, InternetGraph internetGraph) {
        pathLayer.removeAllRenderables();

        for (Map.Entry<String, PointPlacemark> entry : pointPlacemarks.entrySet()) {
            Optional<Node> node = internetGraph.getNode(entry.getKey());
            PointPlacemark pointPlacemark = entry.getValue();

            if (node.isPresent()) {
                Optional<GeolocationData> geolocationData = node.get().getGeolocationData(internetCartographier.getGeolocationService().getClass());

                if (geolocationData.isPresent()) {
                    GeolocationData location = geolocationData.get();

                    pathLayer.addRenderable(pointPlacemark);
                    pointPlacemark.setPosition(Position.fromDegrees(location.getLatitude(), location.getLongitude(), 0));
                }
            }
        }

        Map<Path, Pair<String, String>> edgesBis = new HashMap<>(edges);
        Map<Path, Color> colorsBis = new HashMap<>(colors);
        Map<Path, Boolean> directsBis = new HashMap<>(directs);

        edges.clear();
        colors.clear();
        directs.clear();

        Set<Path> paths = edgesBis.keySet();

        for (Path path : paths) {
            Pair<String, String> nodes = edgesBis.get(path);

            addEdge(nodes.getKey(), nodes.getValue(), colorsBis.get(path), directsBis.get(path));
        }

        wwd.redraw();
    }

    public PointPlacemark getNode(String ipAddress) {
        return pointPlacemarks.get(ipAddress);
    }

}
