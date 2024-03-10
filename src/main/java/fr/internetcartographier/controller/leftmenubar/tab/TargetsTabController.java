package fr.internetcartographier.controller.leftmenubar.tab;

import fr.internetcartographier.controller.Controller;
import fr.internetcartographier.controller.traceroutemanager.TracerouteManager;
import fr.internetcartographier.controller.traceroutemanager.TracerouteParameters;
import fr.internetcartographier.model.geolocationservice.IPAddress;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TargetsTabController extends Controller {

    private final static int MAX_IP_SEGMENT_VALUE = 255;
    private static final String DOMAIN_PATTERN = "^([a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*)?$";

    @FXML
    private ToggleGroup targetType;
    @FXML
    private VBox ipAddressInput;
    @FXML
    private RadioButton ipAddress;
    @FXML
    private VBox domainNameInput;
    @FXML
    private RadioButton domainName;
    @FXML
    private VBox ipAddressesRangeInput;
    @FXML
    private RadioButton ipAddressesRange;
    @FXML
    private VBox ipAddressFileInput;
    @FXML
    private RadioButton ipAddressFile;

    private final TracerouteManager tracerouteManager;

    private final TracerouteParameters tracerouteParameters;

    public TargetsTabController(TracerouteManager tracerouteManager, TracerouteParameters tracerouteParameters) {
        this.tracerouteManager = tracerouteManager;
        this.tracerouteParameters = tracerouteParameters;
    }

    private RadioButton selectedRadioButton;

    private void initializeTargetTypeToggleGroup() {
        targetType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof RadioButton) {
                selectedRadioButton = (RadioButton) newValue;
            }
        });

        selectedRadioButton = domainName;

        domainName.setOnAction(event -> setInputVisibility(domainNameInput, ipAddressesRangeInput, ipAddressFileInput, ipAddressInput));
        ipAddress.setOnAction(event -> setInputVisibility(ipAddressInput, domainNameInput, ipAddressesRangeInput, ipAddressFileInput));
        ipAddressesRange.setOnAction(event -> setInputVisibility(ipAddressesRangeInput , ipAddressFileInput, ipAddressInput, domainNameInput));
        ipAddressFile.setOnAction(event -> setInputVisibility(ipAddressFileInput, ipAddressInput, domainNameInput, ipAddressesRangeInput));
    }

    private void setInputVisibility(VBox enabled, VBox... disabled) {
        enabled.setDisable(false);

        for (VBox other : disabled) {
            other.setDisable(true);
        }
    }

    @FXML
    private TextField ipAddress1;
    @FXML
    private TextField ipAddress2;
    @FXML
    private TextField ipAddress3;
    @FXML
    private TextField ipAddress4;
    @FXML
    private Button ipAddressRandom;

    private String validIPAddress;

    private void initializeIPAddressInput() {
        ipAddress1.setTextFormatter(createIPSegmentFormatter());
        ipAddress2.setTextFormatter(createIPSegmentFormatter());
        ipAddress3.setTextFormatter(createIPSegmentFormatter());
        ipAddress4.setTextFormatter(createIPSegmentFormatter());

        ipAddressRandom.setOnMouseClicked(event -> {
            IPAddress random = IPAddress.generateRandomIPAddress();

            String[] ipAddressSegments = random.getStringIpAddress().split("\\.");

            ipAddress1.setText(ipAddressSegments[0]);
            ipAddress2.setText(ipAddressSegments[1]);
            ipAddress3.setText(ipAddressSegments[2]);
            ipAddress4.setText(ipAddressSegments[3]);
        });
    }

    private boolean validateIPAddressInput() {
        String ipAddressString = ipAddress1.getText() +
                "." + ipAddress2.getText() +
                "." + ipAddress3.getText() +
                "." + ipAddress4.getText();

        if (isIPAddressInvalid(ipAddressString)) {
            displayMessage("IP address (" + ipAddressString + ") is invalid!", true);
            return false;
        }

        validIPAddress = ipAddressString;
        return true;
    }

    private boolean isIPAddressInvalid(String ipAddressString) {
        try {
            new IPAddress(ipAddressString);

            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private TextFormatter<String> createIPSegmentFormatter() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();

            if (isValidIPSegment(newText) || newText.isEmpty()) {
                return change;
            }

            return null;
        };

        return new TextFormatter<>(filter);
    }

    private boolean isValidIPSegment(String text) {
        return text.matches("\\d{1,3}") && Integer.parseInt(text) <= MAX_IP_SEGMENT_VALUE;
    }

    @FXML
    private TextField domainNameField;

    private boolean validateDomainNameInput() {
        if (!domainNameField.getText().matches(DOMAIN_PATTERN)) {
            displayMessage("Invalid domain name!", true);
            return false;
        }

        return true;
    }

    @FXML
    private TextField ipRangeFrom1;
    @FXML
    private TextField ipRangeFrom2;
    @FXML
    private TextField ipRangeFrom3;
    @FXML
    private TextField ipRangeFrom4;
    @FXML
    private TextField ipRangeTo1;
    @FXML
    private TextField ipRangeTo2;
    @FXML
    private TextField ipRangeTo3;
    @FXML
    private TextField ipRangeTo4;

    private String validIPRangeTo;
    private String validIPRangeFrom;

    private void initializeIPAddressesRangeInput() {
        ipRangeFrom1.setTextFormatter(createIPSegmentFormatter());
        ipRangeFrom2.setTextFormatter(createIPSegmentFormatter());
        ipRangeFrom3.setTextFormatter(createIPSegmentFormatter());
        ipRangeFrom4.setTextFormatter(createIPSegmentFormatter());
        ipRangeTo1.setTextFormatter(createIPSegmentFormatter());
        ipRangeTo2.setTextFormatter(createIPSegmentFormatter());
        ipRangeTo3.setTextFormatter(createIPSegmentFormatter());
        ipRangeTo4.setTextFormatter(createIPSegmentFormatter());
    }

    private boolean validateIPAddressesRangeInput() {
        String ipRangeFromString = ipRangeFrom1.getText() +
                "." + ipRangeFrom2.getText() +
                "." + ipRangeFrom3.getText() +
                "." + ipRangeFrom4.getText();

        String ipRangeToString = ipRangeTo1.getText() +
                "." + ipRangeTo2.getText() +
                "." + ipRangeTo3.getText() +
                "." + ipRangeTo4.getText();

        if (isIPAddressInvalid(ipRangeFromString)) {
            displayMessage("From IP address (" + ipRangeFromString + ") is invalid!", true);
            return false;
        }

        if (isIPAddressInvalid(ipRangeToString)) {
            displayMessage("To IP address (" + ipRangeToString + ") is invalid!", true);
            return false;
        }

        if (IPAddress.ipToLong(ipRangeFromString) >= IPAddress.ipToLong(ipRangeToString)) {
            displayMessage("From IP address should be smaller than To IP address!", true);
            return false;
        }

        validIPRangeFrom = ipRangeFromString;
        validIPRangeTo = ipRangeToString;

        return true;
    }

    @FXML
    private Label fileName;
    @FXML
    private Button chooseFile;

    private File ipAddressSelectedFile;
    private FileChooser ipAddressFileChooser;

    private final List<String> validFileIPAddresses = new ArrayList<>();

    private void initializeIPAddressFileInput() {
        ipAddressFileChooser = getIPAddressFileChooser();

        chooseFile.setOnAction(event -> {
            ipAddressSelectedFile = ipAddressFileChooser.showOpenDialog(null);

            if (ipAddressSelectedFile != null) {
                fileName.setText(ipAddressSelectedFile.getName());
            }
        });
    }

    private boolean validateIPAddressFileInput() {
        validFileIPAddresses.clear();

        if (ipAddressSelectedFile == null) {
            displayMessage("You must select a file!", true);
            return false;
        }

        return validateIPAddressFile(ipAddressSelectedFile);
    }

    private boolean validateIPAddressFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean hasError = false;

            while ((line = reader.readLine()) != null) {
                boolean result = !isIPAddressInvalid(line);

                if (!result) {
                    hasError = true;
                    break;
                }

                validFileIPAddresses.add(line);
            }

            if (hasError) {
                displayMessage("IP address (" + line + ") is invalid!", true);
                return false;
            }
        } catch (IOException e) {
            displayMessage("Problem while reading the file!", true);
            return false;
        }

        return true;
    }

    private FileChooser getIPAddressFileChooser() {
        FileChooser ipAddressFileChooser = new FileChooser();

        ipAddressFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        ipAddressFileChooser.getExtensionFilters().add(extensionFilter);

        return ipAddressFileChooser;
    }

    @FXML
    private ToggleButton hostNameResolution;
    @FXML
    private Spinner<Integer> maxHops;
    @FXML
    private Spinner<Double> timeout;

    private void initializeTracerouteParameters() {
        BooleanProperty hostNameResolutionProperty = tracerouteParameters.getHostNameResolutionProperty();

        if (tracerouteParameters.getHostNameResolution()) {
            hostNameResolution.setSelected(true);
            hostNameResolution.setText("YES");
            hostNameResolution.getStyleClass().add("toggle-button-on");
        } else {
            hostNameResolution.setSelected(false);
            hostNameResolution.setText("NO");
            hostNameResolution.getStyleClass().add("toggle-button-off");
        }

        hostNameResolutionProperty.bind(hostNameResolution.selectedProperty());

        hostNameResolution.setOnAction(event -> {
            hostNameResolution.getStyleClass().removeAll("toggle-button-on", "toggle-button-off");

            if (hostNameResolution.isSelected()) {
                hostNameResolution.setText("YES");
                hostNameResolution.getStyleClass().add("toggle-button-on");
            } else {
                hostNameResolution.setText("NO");
                hostNameResolution.getStyleClass().add("toggle-button-off");
            }
        });

        SpinnerValueFactory<Integer> maxHopsFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, tracerouteParameters.getMaxHops());
        maxHops.setValueFactory(maxHopsFactory);
        maxHops.getEditor().setTextFormatter(createNumberFormatter("integer"));

        IntegerProperty maxHopsProperty = tracerouteParameters.getMaxHopsProperty();
        maxHopsProperty.bind(maxHops.valueProperty());

        SpinnerValueFactory<Double> timeoutFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 20.0, tracerouteParameters.getTimeout(), 0.1);
        timeout.setValueFactory(timeoutFactory);
        timeout.getEditor().setTextFormatter(createNumberFormatter("double"));

        DoubleProperty timeoutProperty = tracerouteParameters.getTimeoutProperty();
        timeoutProperty.bind(timeout.valueProperty());
    }

    private TextFormatter<String> createNumberFormatter(String type) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();

            if (("integer".equals(type) && isValidInteger(newText))
                    || ("double".equals(type) && isValidDouble(newText))
                    || newText.isEmpty()) {
                return change;
            }

            return null;
        };

        return new TextFormatter<>(filter);
    }

    private boolean isValidInteger(String input) {
        try {
            Integer.parseInt(input);

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDouble(String input) {
        try {
            Double.parseDouble(input);

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    private Spinner<Integer> simultaneousTraceroute;
    @FXML
    private Spinner<Integer> traceroutePerMinute;

    private void initializeExecutionParameters() {
        SpinnerValueFactory<Integer> simultaneousTracerouteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, tracerouteManager.getSimultaneousTraceroute());
        simultaneousTraceroute.setValueFactory(simultaneousTracerouteFactory);
        simultaneousTraceroute.getEditor().setTextFormatter(createNumberFormatter("integer"));

        IntegerProperty simultaneousTracerouteProperty = tracerouteManager.getSimultaneousTracerouteProperty();
        simultaneousTracerouteProperty.bind(simultaneousTraceroute.valueProperty());

        SpinnerValueFactory<Integer> traceroutePerMinuteFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE, tracerouteManager.getTraceroutePerMinute());
        traceroutePerMinute.setValueFactory(traceroutePerMinuteFactory);
        traceroutePerMinute.getEditor().setTextFormatter(createNumberFormatter("integer"));

        IntegerProperty traceroutePerMinuteProperty = tracerouteManager.getTraceroutePerMinuteProperty();
        traceroutePerMinuteProperty.bind(traceroutePerMinute.valueProperty());
    }

    @FXML
    private Button addToQueue;
    @FXML
    private Label displayMessage;

    @FXML
    private void initialize() {
        initializeTargetTypeToggleGroup();
        initializeIPAddressInput();
        initializeIPAddressesRangeInput();
        initializeIPAddressFileInput();
        initializeTracerouteParameters();
        initializeExecutionParameters();

        addToQueue.setOnAction(event -> addToTracerouteQueue());

        clearDisplayMessage = new Timeline(new KeyFrame(Duration.seconds(ERROR_MESSAGE_DISPLAY_DURATION), event -> displayMessage.setText("")));
        clearDisplayMessage.setCycleCount(1);
    }

    private boolean validateInputs() {
        if (selectedRadioButton != null) {
            if (selectedRadioButton == ipAddress) {
                return validateIPAddressInput();
            } else if (selectedRadioButton == domainName) {
                return validateDomainNameInput();
            } else if (selectedRadioButton == ipAddressesRange) {
                return validateIPAddressesRangeInput();
            } else if (selectedRadioButton == ipAddressFile) {
                return validateIPAddressFileInput();
            }
        }

        displayMessage("No target input option selected!", true);
        return false;
    }

    private final static double ERROR_MESSAGE_DISPLAY_DURATION = 5;

    private Timeline clearDisplayMessage;

    private void displayMessage(String message, boolean error) {
        displayMessage.getStyleClass().removeAll("success-message", "error-message");

        if (error) {
            displayMessage.getStyleClass().add("error-message");
        } else {
            displayMessage.getStyleClass().add("success-message");
        }

        displayMessage.setText(message);

        clearDisplayMessage.stop();
        clearDisplayMessage.play();
    }

    private void addToTracerouteQueue() {
        if (selectedRadioButton != null && validateInputs()) {
            if (selectedRadioButton == ipAddress) {
                tracerouteManager.addTarget(validIPAddress);
            } else if (selectedRadioButton == domainName) {
                tracerouteManager.addTarget(domainNameField.getText());
            } else if (selectedRadioButton == ipAddressesRange) {
                List<IPAddress> ipAddressesRange = IPAddress.generateIPAddressesInARange(new IPAddress(validIPRangeFrom), new IPAddress(validIPRangeTo));

                List<String> ipAddressesRangeString = ipAddressesRange.stream()
                        .map(IPAddress::getStringIpAddress)
                        .collect(Collectors.toList());

                tracerouteManager.addTargets(ipAddressesRangeString);
            } else if (selectedRadioButton == ipAddressFile) {
                tracerouteManager.addTargets(validFileIPAddresses);
            }

            displayMessage("Successfully added to the queue!", false);
        }
    }

}
