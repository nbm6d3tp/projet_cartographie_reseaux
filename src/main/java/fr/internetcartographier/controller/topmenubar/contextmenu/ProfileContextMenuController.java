package fr.internetcartographier.controller.topmenubar.contextmenu;

import fr.internetcartographier.Main;
import fr.internetcartographier.controller.Controller;
import fr.internetcartographier.controller.ControllerManager;
import fr.internetcartographier.controller.SignInController;
import fr.internetcartographier.model.firebase.UserInfo;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.util.Optional;

public class ProfileContextMenuController extends Controller {

    // TODO depends on : modal | application |

    @FXML
    private ContextMenu profileContextMenu;

    @FXML
    private Label profileName;

    @FXML
    private Label openProfileSettingsOption;

    @FXML
    private Label viewProfileOption;

    @FXML
    private HBox logoutOption;

    @FXML
    private void initialize() {
        ControllerManager controllerManager = ControllerManager.getInstance();
        controllerManager.registerController("ProfileContextMenuController", this);

        profileContextMenu.getProperties().put("CONTROLLER", this);

        openProfileSettingsOption.setOnMouseClicked(event -> openProfileSettingsOption());
        viewProfileOption.setOnMouseClicked(event -> viewProfileOption());
        logoutOption.setOnMouseClicked(event -> logoutOption());
    }

    @FXML
    private void openProfileSettingsOption() {
        // TODO Open large parameters view where you can change APP parameters (whats inside config.properties in fact)
        //modalController.openProfileSettings();
        //applicationController.openProfileSettings();
        System.out.println("Open profile settings");
    }

    @FXML
    private void viewProfileOption() {
        // TODO Open large parameters view where you can change APP parameters (whats inside config.properties in fact)
        //modalController.openProfile();
        //applicationController.openProfile();
        System.out.println("Open profile");
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

    public void setProfileName(String name) {
        profileName.setText("@" + name);
    }

}
