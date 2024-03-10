package fr.internetcartographier.controller.topmenubar.contextmenu;

import fr.internetcartographier.Main;
import fr.internetcartographier.controller.ApplicationController;
import fr.internetcartographier.controller.Controller;
import fr.internetcartographier.controller.ControllerManager;
import fr.internetcartographier.controller.SignInController;
import fr.internetcartographier.controller.globe.GlobeController;
import fr.internetcartographier.controller.hud.StatusUpdatePopupController;
import fr.internetcartographier.model.InternetCartographier;
import fr.internetcartographier.model.internetgraph.InternetGraph;
import fr.internetcartographier.model.internetgraph.Node;
import fr.internetcartographier.util.csv.CSVReader;
import fr.internetcartographier.util.csv.CSVWrite;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsContextMenuController extends Controller {

    @FXML
    private ContextMenu settingsContextMenu;

    @FXML
    private Label openSettingsOption;

    @FXML
    private Label importOption;

    @FXML
    private Label exportOption;

    @FXML
    private HBox logoutOption;

    @FXML
    private Label quitOption;

    private final InternetCartographier internetCartographier;

    public SettingsContextMenuController(InternetCartographier internetCartographier) {
        this.internetCartographier = internetCartographier;
    }

    @FXML
    private void initialize() {
        settingsContextMenu.getProperties().put("CONTROLLER", this);

        fileChooserImport = getFileChooserImport();
        directoryChooserExport = getDirectoryChooserExport();

        openSettingsOption.setOnMouseClicked(event -> openSettingsOption());
        importOption.setOnMouseClicked(event -> {
            try {
                importOption();
            } catch (IOException e) {
                throw new RuntimeException("Error: During file import!");
            }
        });
        exportOption.setOnMouseClicked(event -> {
            try {
                exportOption();
            } catch (IOException e) {
                throw new RuntimeException("Error: During file export!");
            }
        });
        logoutOption.setOnMouseClicked(event -> logoutOption());
        quitOption.setOnMouseClicked(event -> quitOption());
    }

    private FileChooser fileChooserImport;

    private DirectoryChooser directoryChooserExport;

    @FXML
    private void openSettingsOption() {
        // TODO Open large parameters view where you can change APP parameters (whats inside config.properties in fact)
        //modalController.openApplicationSettings();
        //applicationController.openApplicationSettings();
        System.out.println("Open settings");
    }

    @FXML
    private void importOption() throws IOException {
        ControllerManager controllerManager = ControllerManager.getInstance();
        StatusUpdatePopupController statusUpdatePopupController = (StatusUpdatePopupController) controllerManager.getController("StatusUpdatePopupController");

        List<File> selectedFiles = fileChooserImport.showOpenMultipleDialog(null);

        if (selectedFiles != null && selectedFiles.size() == 2) {
            InternetGraph importedGraph = CSVReader.importGraphFromCSVFile(selectedFiles.get(0), selectedFiles.get(1));
            internetCartographier.importCompleteGraph(importedGraph);
            GlobeController globeController = (GlobeController) controllerManager.getController("GlobeController");
            globeController.updateImport();
            statusUpdatePopupController.displayTemporaryMessage("Internet graph correctly imported!", StatusUpdatePopupController.PopupType.SUCCESS, 5);
        } else {
            statusUpdatePopupController.displayTemporaryMessage("Invalid files import!", StatusUpdatePopupController.PopupType.ERROR, 5);
        }
    }

    @FXML
    private void exportOption() throws IOException {
        File selectedDirectory = directoryChooserExport.showDialog(null);

        if (selectedDirectory != null) {
            CSVWrite.exportCsvNodesFile(internetCartographier, selectedDirectory.getAbsolutePath() + "nodes.csv", selectedDirectory.getAbsolutePath() + "edges.csv");

            ControllerManager controllerManager = ControllerManager.getInstance();
            StatusUpdatePopupController statusUpdatePopupController = (StatusUpdatePopupController) controllerManager.getController("StatusUpdatePopupController");
            statusUpdatePopupController.displayTemporaryMessage("Internet graph correctly exported!", StatusUpdatePopupController.PopupType.SUCCESS, 5);
        }
    }

    @FXML
    private void logoutOption() {
        // TODO Should display a confirm popup using modal controller (getting true or false) then if yes call application controller to initiate a logout

        //boolean sure = modalController.displayConfirmModal(this) // and somehow we need to wait, maybe callback function by passing this controller ?
        boolean sure = true;

        if (sure) {
            ControllerManager controllerManager = ControllerManager.getInstance();
            SignInController signInController = (SignInController) controllerManager.getController("SignInController");
            signInController.logout();

            Main main = controllerManager.getMain();
            main.goToSignIn();
        }
    }

    @FXML
    private void quitOption() {
        // TODO Should display a confirm popup using modal controller (getting true or false) then if yes call application controller to initiate a quit

        //boolean sure = modalController.displayConfirmModal(this) // and somehow we need to wait, maybe callback function by passing this controller ?
        boolean sure = true;

        if (sure) {
            ControllerManager controllerManager = ControllerManager.getInstance();
            ApplicationController applicationController = (ApplicationController) controllerManager.getController("ApplicationController");
            applicationController.quit();
        }
    }

    private static String readFileAsString(File file) {
        StringBuilder contentBuilder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            // TODO Should use the application HUD to display an error message and also add a notifications
        }

        return contentBuilder.toString();
    }

    private static void saveToCSV(File directory, String content, String filename) {
        if (isValidCSVContent(content) && isValidFilename(filename)) {
            File fileToSave = new File(directory, filename);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(content);
            } catch (IOException e) {
                // TODO Should use the application HUD to display an error message and also add a notifications
            }
        } else {
            // TODO Should use the application HUD to display an error message and also add a notifications
            throw new RuntimeException("Error: Invalid content or filename. File not saved!");
        }
    }

    private static boolean isValidCSVContent(String content) {
        String[] lines = content.split("\n");

        if (lines.length < 2) {
            return false; // CSV should have at least two lines (header and data)
        }

        String[] headers = lines[0].split(",");

        for (String header : headers) {
            if (header.trim().isEmpty()) {
                return false; // Empty header found
            }
        }

        for (int i = 1; i < lines.length; i++) {
            String[] data = lines[i].split(",");

            if (data.length != headers.length) {
                return false; // Inconsistent number of fields in data rows
            }
        }

        return true;
    }

    private static boolean isValidFilename(String filename) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9_-]+\\.csv");
        Matcher matcher = pattern.matcher(filename);

        return matcher.matches();
    }

    private static FileChooser getFileChooserImport() {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("Select CSV Files");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);

        return fileChooser;
    }

    private static DirectoryChooser getDirectoryChooserExport() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        directoryChooser.setTitle("Choose Output Directory");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        return directoryChooser;
    }

}
