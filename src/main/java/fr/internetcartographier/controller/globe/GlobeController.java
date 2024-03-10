package fr.internetcartographier.controller.globe;

import fr.internetcartographier.controller.Controller;
import fr.internetcartographier.controller.ControllerManager;
import fr.internetcartographier.model.InternetCartographier;
import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import fr.internetcartographier.model.internetgraph.Edge;
import fr.internetcartographier.model.internetgraph.InternetGraph;
import fr.internetcartographier.model.internetgraph.Node;
import fr.internetcartographier.model.tracerouteresults.TracerouteResult;
import fr.internetcartographier.model.tracerouteresults.TracerouteRow;
import gov.nasa.worldwind.BasicModel;
import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.animation.AnimationSupport;
import gov.nasa.worldwind.animation.Animator;
import gov.nasa.worldwind.animation.BasicAnimator;
import gov.nasa.worldwind.animation.PositionAnimator;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLJPanel;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.pick.PickedObjectList;
import gov.nasa.worldwind.poi.BasicPointOfInterest;
import gov.nasa.worldwind.poi.PointOfInterest;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.view.firstperson.BasicFlyView;
import gov.nasa.worldwind.view.firstperson.FlyToFlyViewAnimator;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import gov.nasa.worldwind.view.orbit.OrbitView;
import gov.nasa.worldwind.view.orbit.OrbitViewEyePointAnimator;
import gov.nasa.worldwindx.examples.util.BalloonController;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

import javax.swing.*;
import javax.xml.stream.Location;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GlobeController extends Controller {

    @FXML
    private SwingNode globeContainer;

    private WorldWindowGLJPanel worldWindowGLJPanel;

    private InternetGraphLayerManager internetGraphLayerManager;

    private final InternetCartographier internetCartographier;

    private final InternetGraph internetGraph;

    private final IPAddress origin;

    public GlobeController(InternetCartographier internetCartographier) {
        this.internetCartographier = internetCartographier;
        internetGraph = internetCartographier.getInternetGraph();
        origin = internetCartographier.getOrigin().getIpAddress();
    }

    @FXML
    private void initialize() {
        ControllerManager controllerManager = ControllerManager.getInstance();
        controllerManager.registerController("GlobeController", this);

        worldWindowGLJPanel = new WorldWindowGLJPanel();

        worldWindowGLJPanel.setMinimumSize(new Dimension(500, 500));
        worldWindowGLJPanel.setModel(new BasicModel());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(worldWindowGLJPanel, BorderLayout.CENTER);

        globeContainer.setContent(panel);
        internetGraphLayerManager = new InternetGraphLayerManager(worldWindowGLJPanel);

        worldWindowGLJPanel.addSelectListener(event -> {
            if (event.getEventAction().equals(SelectEvent.LEFT_CLICK)) {
                // Should hand click on point placemarks and display information about the clicked node
            }
        });
    }

    public void moveViewOn(String ipAddress) {
        PointPlacemark ipAddressPoint = internetGraphLayerManager.getNode(ipAddress);

        System.out.println("Neighbors : " + internetGraph.getNeighbors(ipAddress));

        if (ipAddressPoint != null) {
            BasicOrbitView view = (BasicOrbitView) worldWindowGLJPanel.getView();
            Position ipAddressPosition = ipAddressPoint.getPosition();

            view.addEyePositionAnimator(1000, view.getEyePosition(), new Position(ipAddressPosition.getLatitude(), ipAddressPosition.getLongitude(), 1e5));
        }
    }

    public InternetGraphLayerManager getInternetGraphLayerManager() {
        return internetGraphLayerManager;
    }

    public void update(TracerouteResult tracerouteResult) {
        IPAddress previousNode = origin;
        boolean direct = true;

        for (TracerouteRow row : tracerouteResult) {
            if (row.isReachable()) {
                Optional<Node> node = internetGraph.getNode(row.getIpAddress());

                if (node.isPresent()) {
                    Optional<GeolocationData> geolocationDataOpt = node.get().getGeolocationData(internetCartographier.getGeolocationService().getClass());

                    if (geolocationDataOpt.isPresent()) {
                        GeolocationData geolocationData = geolocationDataOpt.get();

                        internetGraphLayerManager.addNode(geolocationData.getLatitude(), geolocationData.getLongitude(), node.get().getStringIpAddress());
                    }

                    if (previousNode != null) {
                        direct = true;
                        internetGraphLayerManager.addEdge(node.get().getStringIpAddress(), previousNode.getStringIpAddress(), getRandomBasicColor(), direct);
                    }
                }

                previousNode = row.getIpAddress();
            } else {
                direct = false;
            }
        }
    }

    public void updateImport() { internetGraphLayerManager.updateImport(internetCartographier, internetGraph); }

    public void refresh() {
        internetGraphLayerManager.refresh(internetCartographier, internetGraph);
    }

    public static Color getRandomBasicColor() {
        Random random = new Random();
        Color[] basicColors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA, Color.ORANGE};

        return basicColors[random.nextInt(basicColors.length)];
    }

}
