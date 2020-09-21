package unsw.dungeon;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.json.JSONObject;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tooltip;

import javax.tools.Tool;
import java.io.File;

/**
 * A DungeonLoader that also creates the necessary ImageViews for the UI,
 * connects them via listeners to the model, and creates a controller.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonControllerLoader extends DungeonLoader {

    private List<ImageView> entities;

    //Images
    private Image playerImage;
    private Image wallImage;
    private Image boulderImage;
    private Image switchImage;
    private Image enemyImage;
    private Image potionImage;
    private Image swordImage;
    private Image closeDoorImage;
    private Image exitImage;
    private Image treasureImage;
    private Image keyImage;
    private Image portalImage;
    private Image houndImage;
    private Image openDoorImage;
    private String map;

    public DungeonControllerLoader(String filename) throws FileNotFoundException {
        super(filename);
        entities = new ArrayList<>();
        playerImage = new Image((new File("images/human_new.png")).toURI().toString());
        wallImage = new Image((new File("images/brick_brown_0.png")).toURI().toString());
        boulderImage = new Image((new File("images/boulder.png")).toURI().toString());
        switchImage = new Image((new File("images/pressure_plate.png")).toURI().toString());
        enemyImage = new Image((new File("images/deep_elf_master_archer.png")).toURI().toString());
        potionImage = new Image((new File("images/brilliant_blue_new.png")).toURI().toString());
        swordImage = new Image((new File("images/greatsword_1_new.png")).toURI().toString());
        closeDoorImage = new Image((new File("images/closed_door.png")).toURI().toString());
        exitImage = new Image((new File("images/exit.png")).toURI().toString());
        treasureImage = new Image((new File("images/gold_pile.png")).toURI().toString());
        keyImage = new Image((new File("images/key.png")).toURI().toString());
        portalImage = new Image((new File("images/portal.png")).toURI().toString());
        houndImage = new Image((new File("images/hound.png")).toURI().toString());
        openDoorImage = new Image((new File("images/open_door.png")).toURI().toString());
        map = filename; // this is the map chosen by the user 
    }

    public DungeonControllerLoader(JSONObject json) {
        super(json);
    }

    @Override
    public void onLoad(Entity player) {
        ImageView view = new ImageView(playerImage);
        trackVisibility(player, view);
        trackDeath((Player)player, view);
        addEntity(player, view);
        ((Player) player).setPlayerView(view);
    }

    @Override
    public void onLoad(Wall wall) {
        ImageView view = new ImageView(wallImage);
        addEntity(wall, view);
    }

    @Override
    public void onLoad(Boulder boulder) {
        ImageView view = new ImageView(boulderImage);
        addEntity(boulder, view);
    }

    @Override
    public void onLoad(FloorSwitch floorSwitch) {
        ImageView view = new ImageView(switchImage);
        addEntity(floorSwitch, view);
    }

    @Override
    public void onLoad(Enemy enemy) {
        ImageView view = new ImageView(enemyImage);
        trackVisibility(enemy, view);
        addEntity(enemy, view);
    }

    @Override
    public void onLoad(Potion potion) {
        ImageView view = new ImageView(potionImage);
        trackVisibility(potion, view);
        addEntity(potion, view);
    }

    @Override
    public void onLoad(Sword sword) {
        ImageView view = new ImageView(swordImage);
        trackVisibility(sword, view);
        addEntity(sword, view);
    }

    @Override
    public void onLoad(Door door) {
        ImageView view = new ImageView(closeDoorImage);
        Tooltip.install(view, new Tooltip("id: " + door.getId()));
        trackOpenDoors(door, view);
        addEntity(door, view);
    }

    @Override
    public void onLoad(Exit exit) {
        ImageView view = new ImageView(exitImage);
        addEntity(exit, view);
    }

    @Override
    public void onLoad(Treasure treasure) {
        ImageView view = new ImageView(treasureImage);
        trackVisibility(treasure, view);
        addEntity(treasure, view);
    }

    @Override
    public void onLoad(Key key) {
        ImageView view = new ImageView(keyImage);
        Tooltip.install(view, new Tooltip("id: " + key.getId()));
        trackVisibility(key, view);
        addEntity(key, view);
    }

    @Override
    public void onLoad(Portal portal) {
        ImageView view = new ImageView(portalImage);
        Tooltip.install(view, new Tooltip("id: " + portal.getId()));
        addEntity(portal, view);
    }

    @Override
    public void onLoad(Hound hound) {
        ImageView view = new ImageView(houndImage);
        trackVisibility(hound, view);
        addEntity(hound, view);
    }

    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entities.add(view);
    }

    /**
     * Set a node in a GridPane to have its position track the position of an
     * entity in the dungeon.
     *
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the
     * model will automatically be reflected in the view.
     * @param entity
     * @param node
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());
        entity.x().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setColumnIndex(node, newValue.intValue());
            }
        });
        entity.y().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                GridPane.setRowIndex(node, newValue.intValue());
            }
        });
    }

    /**
     * Sets a node in the GridPane to invisible when its visibility BooleanProperty changes.
     * @param entity  
     * @param node
     */
    private void trackVisibility(Entity entity, Node node) {
        entity.getVisibility().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue)  {
                    node.setVisible(newValue);
                }
        });
    }

    private void trackDeath(Player player, Node node) {
        player.getDead().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue)  {
                    // Change the scene to restart menu since player died
                    Stage stage = (Stage)(node.getScene().getWindow());
                    Scene scene = (Scene)(node.getScene());

                    // Make a new scene with the pause menu
                    PauseController c = new PauseController(scene, "Game Over!", map);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("PauseView.fxml"));
                    loader.setController(c);
                    try {
                        Parent pauseRoot = loader.load();
                        Scene pause = new Scene(pauseRoot);
                        pauseRoot.requestFocus();
                        stage.setScene(pause);
                        stage.show();
                    } catch (Exception e) {
                        System.out.println("Could not open restart menu: " + e.toString());
                    }
                }
            });
    }

    private void trackOpenDoors(Door door, ImageView img) {
        door.getState().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable,
                Boolean oldValue, Boolean newValue) {
                    img.setImage(openDoorImage);
                }
        });
    }

    /**
     * Create a controller that can be attached to the DungeonView with all the
     * loaded entities.
     * @return
     * @throws FileNotFoundException
     */
    public DungeonController loadController() throws FileNotFoundException {
        return new DungeonController(load(), entities, map);
    }


}
