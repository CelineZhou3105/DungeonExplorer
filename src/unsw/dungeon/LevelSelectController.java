package unsw.dungeon;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.io.File;

public class LevelSelectController {

    @FXML
    private Button maze;

    @FXML
    private ImageView mazeImg;

    @FXML
    private ImageView houndImg;

    @FXML
    private ImageView enemyImg;

    @FXML
    private ImageView potionImg;

    @FXML
    private ImageView portalImg;

    @FXML
    private ImageView boulderImg;

    @FXML
    private Button back;

    private String map; 

    @FXML
    void goBack(ActionEvent event) {
        playSoundEffect("sounds/buttonClick.mp3");
        // Load the start screen
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        StartController startController = new StartController(stage);
        FXMLLoader startLoader = new FXMLLoader(getClass().getResource("DungeonStartView.fxml"));
        startLoader.setController(startController);

        try {
            Parent startRoot = startLoader.load();
            Scene startScene = new Scene(startRoot);
            stage.setScene(startScene);
            stage.show();
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
            stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
        } catch (Exception e) {
            System.out.println("Couldn't go back to the start screen: " + e.toString());
        }
    }

    @FXML
    void selectPortalLevel(ActionEvent event) {
        playSoundEffect("sounds/buttonClick.mp3");
        map = "portalKeyDungeon.json";
        openLevel(event);
    }

    @FXML
    void selectEnemyLevel(ActionEvent event) {
        playSoundEffect("sounds/buttonClick.mp3");
        map = "enemyBoulderDungeon.json";
        openLevel(event);
    }

    @FXML
    void selectHoundsLevel(ActionEvent event) {
        playSoundEffect("sounds/buttonClick.mp3");
        map = "houndDungeon.json";
        openLevel(event);
    }

    @FXML
    void selectMazeLevel(ActionEvent event) {
        playSoundEffect("sounds/buttonClick.mp3");
        map = "maze.json";
        openLevel(event);
    }

    @FXML
    void selectBoulderLevel(ActionEvent event) {
        map = "boulders.json";
        openLevel(event);
    }

    @FXML 
    void selectPotionLevel(ActionEvent event) {
        map = "potionDungeon.json";
        openLevel(event);
    }

    @FXML
    public void initialize() {
        Image wallImage = new Image((new File("images/brick_brown_0.png")).toURI().toString());
        Image boulderImage = new Image((new File("images/boulder.png")).toURI().toString());
        Image houndImage = new Image((new File("images/hound.png")).toURI().toString());
        Image enemyImage = new Image((new File("images/deep_elf_master_archer.png")).toURI().toString());
        Image portalImage = new Image((new File("images/portal.png")).toURI().toString());
        Image potionImage = new Image((new File("images/brilliant_blue_new.png")).toURI().toString());

        mazeImg.setImage(wallImage);
        boulderImg.setImage(boulderImage);
        houndImg.setImage(houndImage);
        enemyImg.setImage(enemyImage);
        portalImg.setImage(portalImage);
        potionImg.setImage(potionImage);
    }

    public void openLevel(ActionEvent event) {
        // Get the stage
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        // Load the correct level
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
            System.out.println("Could not load level: " + e);
        }
    }

    public void playSoundEffect(String mFile) {
        Media sound = new Media(new File(mFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}