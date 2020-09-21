package unsw.dungeon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import javafx.event.ActionEvent;

import java.io.File;

public class PauseController {

    @FXML
    private Pane pauseMenu;

    @FXML
    private Text gameOutcome;

    @FXML
    private Button resume;

    @FXML
    private Button restart;

    @FXML
    private Button exit;

    private Scene previousScene;
    private String outcomeText;
    private String map;
    
    public PauseController(Scene previous, String outcomeText, String map) {
        previousScene = previous;
        this.outcomeText = outcomeText;
        this.map = map;
    }

    @FXML
    public void closePause(ActionEvent event) {
        playSoundEffect("sounds/buttonClick.mp3");
        System.out.println("closing pause menu...");
        // Get the stage
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Set the scene on the stage to the previous one
        stage.setScene(previousScene);
        stage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @FXML
    public void restartLevel(ActionEvent event) {
        playSoundEffect("sounds/buttonClick.mp3");
        // Get the stage
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Reload the dungeon view
        try {
            DungeonControllerLoader dungeonLoader = new DungeonControllerLoader(map);
            DungeonController controller = dungeonLoader.loadController();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DungeonView.fxml"));
            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            root.requestFocus();
            stage.setScene(scene);
            stage.show();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            playSoundEffect("sounds/gameStart.mp3");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void exitToLevelSelect(ActionEvent event) {
        playSoundEffect("sounds/buttonClick.mp3");
        // Get the stage
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Load up the start page
        LevelSelectController levelController = new LevelSelectController();
        FXMLLoader startLoader = new FXMLLoader(getClass().getResource("SelectLevelView.fxml"));
        startLoader.setController(levelController);
        try {
            Parent startRoot = startLoader.load();
            Scene startScene = new Scene(startRoot);
            stage.setScene(startScene);
            stage.show();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void initialize() {
        //gameOutcome = new Text("Paused");
        gameOutcome.setText(outcomeText);
        gameOutcome.setTextAlignment(TextAlignment.CENTER);

        if (gameOutcome.getText().equals("You Win!") || gameOutcome.getText().equals("Game Over!")) {
            resume.setVisible(false);
        }
    }

    public void playSoundEffect(String mFile) {
        Media sound = new Media(new File(mFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}