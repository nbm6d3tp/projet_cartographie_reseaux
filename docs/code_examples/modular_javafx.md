# Modular FXML

### MainView.fxml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.layout.BorderPane?>
<?import com.example.MainController?>

<BorderPane xmlns:fx="http://javafx.com/fxml" fx:controller="com.example.MainController">
    <!-- MainView UI components -->

    <!-- Include the first subview -->
    <fx:include source="SubView1.fxml"/>

    <!-- Include the second subview -->
    <fx:include source="SubView2.fxml"/>
</BorderPane>
```

### SubView1.fxml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.layout.VBox?>
<?import com.example.SubController1?>

<VBox xmlns:fx="http://javafx.com/fxml" fx:controller="com.example.SubController1">
    <!-- SubView1 UI components -->
</VBox>
```

### SubView2.fxml

```xml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.layout.HBox?>
<?import com.example.SubController2?>

<HBox xmlns:fx="http://javafx.com/fxml" fx:controller="com.example.SubController2">
    <!-- SubView2 UI components -->
</HBox>
```

### YourJavaFXApp.java

```java
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class YourJavaFXApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize required services or dependencies
        SomeService someService = new SomeService(/* initialize dependencies */);
        AnotherService anotherService = new AnotherService(/* initialize dependencies */);

        // Load the main view
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("MainView.fxml"));

        // Set a custom controller factory
        mainLoader.setControllerFactory(controllerClass -> {
            if (controllerClass == MainController.class) {
                return new MainController(someService, anotherService);
            } else if (controllerClass == SubController1.class) {
                return new SubController1(someService);
            } else if (controllerClass == SubController2.class) {
                return new SubController2(anotherService);
            }
            return null; // Return null for unknown controllers
        });

        try {
            Parent root = mainLoader.load();
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle("Your JavaFX App");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

