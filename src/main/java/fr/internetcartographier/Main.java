package fr.internetcartographier;

import fr.internetcartographier.controller.ApplicationController;
import fr.internetcartographier.controller.ControllerManager;
import fr.internetcartographier.controller.SignInController;
import fr.internetcartographier.controller.globe.GlobeController;
import fr.internetcartographier.controller.globe.InternetGraphLayerManager;
import fr.internetcartographier.controller.hud.HUDController;
import fr.internetcartographier.controller.leftmenubar.tab.QueueTabController;
import fr.internetcartographier.controller.leftmenubar.tab.ShortestPathController;
import fr.internetcartographier.controller.leftmenubar.tab.StatisticsTabController;
import fr.internetcartographier.controller.leftmenubar.tab.TargetsTabController;
import fr.internetcartographier.controller.topmenubar.contextmenu.ProfileContextMenuController;
import fr.internetcartographier.controller.topmenubar.contextmenu.SettingsContextMenuController;
import fr.internetcartographier.controller.topmenubar.option.SearchController;
import fr.internetcartographier.controller.traceroutemanager.TracerouteManager;
import fr.internetcartographier.controller.traceroutemanager.TracerouteParameters;
import fr.internetcartographier.model.InternetCartographier;
import fr.internetcartographier.model.WeightMetric;
import fr.internetcartographier.model.firebase.UserInfo;
import fr.internetcartographier.model.geolocationservice.GeolocationData;
import fr.internetcartographier.model.geolocationservice.GeolocationService;
import fr.internetcartographier.model.internetgraph.Node;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {

	private InternetCartographier internetCartographier;

	private TracerouteParameters tracerouteParameters;

	private TracerouteManager tracerouteManager;

	@Override
	public void init() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Configuration configuration;

        try {
            configuration = Configuration.getInstance();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

		Class<?> clazz = Class.forName(configuration.getProperty("geolocationservice"));
		Method method = clazz.getMethod("getInstance");

		GeolocationService geolocationService = (GeolocationService) method.invoke(null);
		WeightMetric weightMetric = WeightMetric.valueOf(configuration.getProperty("weightmetric"));

		internetCartographier = new InternetCartographier(geolocationService, weightMetric);

		boolean hostNameResolution = Boolean.parseBoolean(configuration.getProperty("hostnameresolution"));
		int maxHops = Integer.parseInt(configuration.getProperty("maxhops"));
		double timeout = Double.parseDouble(configuration.getProperty("timeout"));

		tracerouteParameters = new TracerouteParameters(hostNameResolution, maxHops, timeout);

		int simultaneousTraceroute = Integer.parseInt(configuration.getProperty("simultaneoustraceroute"));
		int traceroutePerMinute = Integer.parseInt(configuration.getProperty("tracerouteperminute"));

		tracerouteManager = new TracerouteManager(tracerouteParameters, simultaneousTraceroute, traceroutePerMinute);
	}

	private Stage stage;
	private Scene signIn;
	private Scene application;

	@Override
	public void start(Stage stage) throws IOException {
		this.stage = stage;

		FXMLLoader applicationLoader = new FXMLLoader(getClass().getResource("../../resources/fxml/application.fxml"));
		applicationLoader.setControllerFactory(clazz -> {
			if (clazz == ApplicationController.class) {
				return new ApplicationController(internetCartographier);
			} else if (clazz == HUDController.class) {
				return new HUDController(internetCartographier);
			} else if (clazz == SearchController.class) {
				return new SearchController(internetCartographier);
			} else if (clazz == TargetsTabController.class) {
				return new TargetsTabController(tracerouteManager, tracerouteParameters);
			} else if (clazz == GlobeController.class) {
				return new GlobeController(internetCartographier);
			} else if (clazz == ShortestPathController.class) {
				return new ShortestPathController(internetCartographier);
			} else if (clazz == QueueTabController.class) {
				return new QueueTabController(tracerouteManager);
			} else if (clazz == StatisticsTabController.class) {
				return new StatisticsTabController(internetCartographier, tracerouteManager);
			} else if (clazz == SettingsContextMenuController.class) {
				return new SettingsContextMenuController(internetCartographier);
			} else {
				try {
					return clazz.getDeclaredConstructor().newInstance();
				} catch (Exception e) {
					throw new RuntimeException("Error: No default constructor for " + clazz + "!");
				}
			}
		});
		application = new Scene(applicationLoader.load());

		FXMLLoader signInLoader = new FXMLLoader(getClass().getResource("../../resources/fxml/sign_in.fxml"));
		signInLoader.setControllerFactory(clazz -> {
			if (clazz == SignInController.class) {
				return new SignInController(this, internetCartographier);
			} else {
				throw new RuntimeException("Error: Invalid controller class in sign in scene!");
			}
		});
		signIn = new Scene(signInLoader.load());

		stage.setTitle("Internet Cartographier");
		stage.setOnShown(event -> goToSignIn());
		stage.show();

		ControllerManager controllerManager = ControllerManager.getInstance();
		controllerManager.registreMain(this);
	}

	private FadeTransition sceneTransition;

	private final ChangeListener<Number> widthChangeListener = (obs, oldWidth, newWidth) -> stage.setMinWidth(newWidth.doubleValue());
	private final ChangeListener<Number> heightChangeListener = (obs, oldWidth, newWidth) -> stage.setMinHeight(newWidth.doubleValue());

	public void goToApplication() {
		stage.widthProperty().addListener(widthChangeListener);
		stage.heightProperty().addListener(heightChangeListener);

		ControllerManager controllerManager = ControllerManager.getInstance();
		GlobeController globeController = (GlobeController) controllerManager.getController("GlobeController");
		InternetGraphLayerManager internetGraphLayerManager = globeController.getInternetGraphLayerManager();

		Node origin = internetCartographier.getOrigin();
		GeolocationService geolocationService = internetCartographier.getGeolocationService();
		Optional<GeolocationData> originGeolocationData = origin.getGeolocationData(geolocationService.getClass());

		if (originGeolocationData.isPresent()) {
			GeolocationData geolocationData = originGeolocationData.get();
			internetGraphLayerManager.addNode(geolocationData.getLatitude(), geolocationData.getLongitude(),
					origin.getStringIpAddress());
		}

		SignInController signInController = (SignInController) controllerManager.getController("SignInController");
		Optional<UserInfo> userInfoOptional = signInController.getUserInfo();

		ProfileContextMenuController profileContextMenuController = (ProfileContextMenuController) controllerManager.getController("ProfileContextMenuController");

		if (userInfoOptional.isPresent()) {
			UserInfo userInfo = userInfoOptional.get();
			profileContextMenuController.setProfileName(userInfo.getEmail());
		} else {
			profileContextMenuController.setProfileName("Undefined");
		}

		setupSceneTransition(application);

		stage.setMaximized(true);
		stage.widthProperty().removeListener(widthChangeListener);
		stage.heightProperty().removeListener(heightChangeListener);
	}

	public void goToSignIn() {
		stage.widthProperty().addListener(widthChangeListener);
		stage.heightProperty().addListener(heightChangeListener);

		setupSceneTransition(signIn);

		stage.widthProperty().removeListener(widthChangeListener);
		stage.heightProperty().removeListener(heightChangeListener);
	}

	private void setupSceneTransition(Scene signIn) {
		if (sceneTransition != null) {
			sceneTransition.stop();
		}

		sceneTransition = new FadeTransition(Duration.millis(500), signIn.getRoot());
		sceneTransition.setFromValue(0.0);
		sceneTransition.setToValue(1.0);
		sceneTransition.play();

		stage.setScene(signIn);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
