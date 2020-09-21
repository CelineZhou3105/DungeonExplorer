package unsw.dungeon;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DungeonApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Set up the start menu
        StartController startController = new StartController(primaryStage);
        FXMLLoader startLoader = new FXMLLoader(getClass().getResource("DungeonStartView.fxml"));
        startLoader.setController(startController);
        Parent startRoot = startLoader.load();
        Scene startScene = new Scene(startRoot);
        primaryStage.setScene(startScene);
        primaryStage.show();

        primaryStage.setTitle("Dungeon");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
