package unsw.dungeon;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.io.File;
import javafx.stage.Stage;
import java.io.IOException;

public class StartController {

    @FXML
    private Pane root;

    @FXML
    private Button startButton;

    @FXML
    private Button howToPlay;

    @FXML
    private ImageView human;

    @FXML
    private ImageView enemy;

    @FXML
    private ImageView potion;

    @FXML
    private ImageView sword;

    @FXML
    private ImageView treasure;

    @FXML
    private GridPane items;

    @FXML
    private ImageView playerDesc;

    @FXML
    private ImageView wallDesc;

    @FXML
    private ImageView exitDesc;

    @FXML
    private ImageView treasureDesc;

    @FXML
    private ImageView doorDesc;

    @FXML
    private ImageView boulderDesc;

    @FXML
    private ImageView keyDesc;

    @FXML
    private ImageView floorSwitchDesc;

    @FXML
    private ImageView portalDesc;

    @FXML
    private ImageView enemyDesc;

    @FXML
    private ImageView swordDesc;

    @FXML
    private ImageView potionDesc;

    @FXML
    private ImageView houndDesc;

    @FXML
    private Pane itemDesc;

    @FXML
    private Button gotIt;

    @FXML
    private Button exitGame;

    private Stage stage;

    public StartController(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void startGame(ActionEvent event) throws IOException {
        playSoundEffect("sounds/buttonClick.mp3");
        // Show the level selection screen
        LevelSelectController levelController = new LevelSelectController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SelectLevelView.fxml"));
        loader.setController(levelController);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        root.requestFocus();
        
        stage.setScene(scene);
        stage.show();

        // Center the screen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @FXML
    public void instructions(ActionEvent event) {
        playSoundEffect("sounds/buttonClick.mp3");
        itemDesc.setVisible(true);
    }

    @FXML
    public void closeInstructions(ActionEvent event) {
        playSoundEffect("sounds/buttonClick.mp3");
        itemDesc.setVisible(false);
    }

    @FXML
    public void closeGame(ActionEvent event) {
        playSoundEffect("sounds/buttonClick.mp3");
        stage.close();
    }

    @FXML
    public void initialize() {
        Image playerImage = new Image((new File("images/human_new.png")).toURI().toString());
        Image wallImage = new Image((new File("images/brick_brown_0.png")).toURI().toString());
        Image boulderImage = new Image((new File("images/boulder.png")).toURI().toString());
        Image switchImage = new Image((new File("images/pressure_plate.png")).toURI().toString());
        Image enemyImage = new Image((new File("images/deep_elf_master_archer.png")).toURI().toString());
        Image potionImage = new Image((new File("images/brilliant_blue_new.png")).toURI().toString());
        Image swordImage = new Image((new File("images/greatsword_1_new.png")).toURI().toString());
        Image closeDoorImage = new Image((new File("images/closed_door.png")).toURI().toString());
        Image exitImage = new Image((new File("images/exit.png")).toURI().toString());
        Image treasureImage = new Image((new File("images/gold_pile.png")).toURI().toString());
        Image keyImage = new Image((new File("images/key.png")).toURI().toString());
        Image portalImage = new Image((new File("images/portal.png")).toURI().toString());
        Image houndImage = new Image((new File("images/hound.png")).toURI().toString());

        human.setImage(playerImage);
        enemy.setImage(enemyImage);
        potion.setImage(potionImage);
        sword.setImage(swordImage);
        treasure.setImage(treasureImage);

        // Load the images into the first column 
        playerDesc.setImage(playerImage);
        wallDesc.setImage(wallImage);
        boulderDesc.setImage(boulderImage);
        floorSwitchDesc.setImage(switchImage);
        enemyDesc.setImage(enemyImage);
        potionDesc.setImage(potionImage);
        swordDesc.setImage(swordImage);
        doorDesc.setImage(closeDoorImage);
        exitDesc.setImage(exitImage);
        treasureDesc.setImage(treasureImage);
        keyDesc.setImage(keyImage);
        portalDesc.setImage(portalImage);
        houndDesc.setImage(houndImage);

        // Load the descriptions into the second column
        itemDesc.setVisible(false);
        Background b = new Background(new BackgroundFill(Color.MISTYROSE, null, null));
        itemDesc.setBackground(b);
    }

    public void playSoundEffect(String mFile) {
        Media sound = new Media(new File(mFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}