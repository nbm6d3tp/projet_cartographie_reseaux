package fr.internetcartographier.controller.hud;

import fr.internetcartographier.controller.Controller;
import fr.internetcartographier.controller.ControllerManager;
import fr.internetcartographier.controller.Util;
import fr.internetcartographier.controller.globe.GlobeController;
import fr.internetcartographier.controller.topmenubar.contextmenu.NotificationsContextMenuController;
import fr.internetcartographier.model.InternetCartographier;
import fr.internetcartographier.model.WeightMetric;
import fr.internetcartographier.model.geolocationservice.GeoIP2;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.geolocationservice.HostIP;
import fr.internetcartographier.controller.topmenubar.contextmenu.Notification;
import java.io.IOException;
import java.net.URISyntaxException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import javafx.util.StringConverter;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class HUDController extends Controller {

	@FXML
	private Circle globeHUDGeolocationServiceStatusIndicator;

	@FXML
	private ComboBox<GeolocationService> globeHUDGeolocationServiceSelector;

	@FXML
	private ComboBox<WeightMetric> globeHUDWeightMetricSelector;

	@FXML
	private Label globeHUDCurrentTracerouteTarget;

	private Timeline executedTracerouteTargetAnimation;

	private final InternetCartographier internetCartographier;

	public HUDController(InternetCartographier internetCartographier) {
		this.internetCartographier = internetCartographier;
	}

	public void setExecutingTracerouteTarget(String executing) {
		globeHUDCurrentTracerouteTarget.setText("traceroute on " + executing + " executing ...");

		if (executedTracerouteTargetAnimation.getStatus() == Animation.Status.RUNNING) {
			executedTracerouteTargetAnimation.stop();
		}

		executedTracerouteTargetAnimation.play();
	}

	@FXML
	private void initialize() throws ParserConfigurationException, SAXException, URISyntaxException, IOException {
		ControllerManager controllerManager = ControllerManager.getInstance();
		controllerManager.registerController("HUDController", this);

		initializeGeolocationServiceSelector();
		initializeWeightMetricSelector();

		executedTracerouteTargetAnimation = new Timeline(
				new KeyFrame(Duration.seconds(3), event -> globeHUDCurrentTracerouteTarget.setText("")));
	}

	private void initializeGeolocationServiceSelector()
			throws ParserConfigurationException, SAXException, URISyntaxException, IOException {
		GeolocationService hostIP = HostIP.getInstance();
		GeolocationService geoIP2 = GeoIP2.getInstance();

		ObservableList<GeolocationService> geolocationServices = FXCollections.observableArrayList(hostIP, geoIP2);

		globeHUDGeolocationServiceSelector.setConverter(new StringConverter<>() {
			@Override
			public String toString(GeolocationService object) {
				return object == null ? null : object.getName();
			}

			@Override
			public GeolocationService fromString(String string) {
				return null;
			}
		});

		globeHUDGeolocationServiceSelector.setItems(geolocationServices);

		globeHUDGeolocationServiceSelector.setOnAction(event -> {
			GeolocationService geolocationService = globeHUDGeolocationServiceSelector.getValue();
			internetCartographier.updateGeolocationService(geolocationService);
			updateGeolocationServiceStatusIndicator();

			ControllerManager controllerManager = ControllerManager.getInstance();
			GlobeController globeController = (GlobeController) controllerManager.getController("GlobeController");
			globeController.refresh();

			controllerManager = ControllerManager.getInstance();
			StatusUpdatePopupController statusUpdatePopupController = (StatusUpdatePopupController) controllerManager
					.getController("StatusUpdatePopupController");
			statusUpdatePopupController.displayTemporaryMessage(
					"Geolocation service successfully changed to '" + geolocationService.getName() + "'",
					StatusUpdatePopupController.PopupType.LOADING, 8);

			NotificationsContextMenuController notificationsContextMenuController = (NotificationsContextMenuController) controllerManager
					.getController("NotificationsContextMenuController");
			Notification notification = new Notification(
					"Changed to '" + geolocationService.getName() + "' geolocation service.",
					Notification.NotificationType.INFORMATION);
			notificationsContextMenuController.addNotification(notification);

		});

		globeHUDGeolocationServiceSelector.setValue(internetCartographier.getGeolocationService());
		updateGeolocationServiceStatusIndicator();
	}

	private void initializeWeightMetricSelector() {
		globeHUDWeightMetricSelector.setConverter(new StringConverter<>() {
			@Override
			public String toString(WeightMetric object) {
				return object == null ? null : object.toString();
			}

			@Override
			public WeightMetric fromString(String string) {
				return null;
			}
		});

		globeHUDWeightMetricSelector.setItems(Util.getEnumValues(WeightMetric.class));

		globeHUDWeightMetricSelector.setOnAction(event -> {
			WeightMetric weightMetric = globeHUDWeightMetricSelector.getValue();
			internetCartographier.updateWeightMetric(weightMetric);

			ControllerManager controllerManager = ControllerManager.getInstance();
			StatusUpdatePopupController statusUpdatePopupController = (StatusUpdatePopupController) controllerManager
					.getController("StatusUpdatePopupController");
			statusUpdatePopupController.displayTemporaryMessage(
					"Weight metric successfully changed to '" + weightMetric + "'",
					StatusUpdatePopupController.PopupType.LOADING, 8);

			NotificationsContextMenuController notificationsContextMenuController = (NotificationsContextMenuController) controllerManager
					.getController("NotificationsContextMenuController");
			Notification notification = new Notification("Changed to '" + weightMetric + "' weight metric.",
					Notification.NotificationType.INFORMATION);
			notificationsContextMenuController.addNotification(notification);
		});

		globeHUDWeightMetricSelector.setValue(WeightMetric.CONSTANT);
	}

	private void updateGeolocationServiceStatusIndicator() {
		globeHUDGeolocationServiceStatusIndicator
				.setFill(globeHUDGeolocationServiceSelector.getValue().getStatus().getAssociatedColor());
	}

}
