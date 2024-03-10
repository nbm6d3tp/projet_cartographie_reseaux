package fr.internetcartographier.controller;

import fr.internetcartographier.Main;
import fr.internetcartographier.controller.hud.StatusUpdatePopupController;
import fr.internetcartographier.model.InternetCartographier;
import fr.internetcartographier.model.firebase.DatabaseConnection;
import fr.internetcartographier.model.firebase.StatisticDatabase;
import fr.internetcartographier.model.firebase.UserInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

public class SignInController extends Controller {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button signIn;
    @FXML
    private Button signUp;

    @FXML
    private Label statusMessage;

    private final Main main;

    private final InternetCartographier internetCartographier;

    public SignInController(Main main, InternetCartographier internetCartographier) {
        this.main = main;
        this.internetCartographier = internetCartographier;
    }

    @FXML
    private void initialize() {
        ControllerManager controllerManager = ControllerManager.getInstance();
        controllerManager.registerController("SignInController", this);

        signIn.setOnAction(event -> {
            try {
                userInfo = DatabaseConnection.getUser(usernameField.getText(), passwordField.getText());
            } catch (IllegalStateException e) {
                statusMessage.setText("Credentials error during sign in!");
            }

            if (userInfo != null && userInfo.isPresent()) {
                List<StatisticDatabase> histories = DatabaseConnection.getHistories(userInfo.get().getIdToken());

//                for (StatisticDatabase statisticDatabase : histories) {
//                    System.out.println(statisticDatabase);
//                }

                main.goToApplication();
            }
        });

        signUp.setOnAction(event -> {
            try {
                userInfo = DatabaseConnection.saveUser(usernameField.getText(), passwordField.getText());
            } catch (IllegalStateException e) {
                statusMessage.setText("Credentials error during sign up!");
            }

            if (userInfo != null && userInfo.isPresent()) {
                main.goToApplication();
            }
        });
    }

    private Optional<UserInfo> userInfo;

    public Optional<UserInfo> getUserInfo() {
        return userInfo;
    }

    public void logout() {
        if (userInfo.isPresent()) {
            String jwt = userInfo.get().getIdToken();
            DatabaseConnection.saveHistory(jwt, internetCartographier.getStatistics());
        } else {
            throw new RuntimeException("Error: No user logged at the moment!");
        }

        userInfo = Optional.empty();
    }

}
