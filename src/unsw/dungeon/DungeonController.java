package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.converter.NumberStringConverter;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;


import javax.tools.Tool;
import java.io.File;

/**
 * A JavaFX controller for the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class DungeonController {

    @FXML
    private GridPane dungeonGrid;

    private List<ImageView> initialEntities;

    private Player player;

    private Dungeon dungeon;

    private String map;

    @FXML
    private GridPane inventoryGrid;

    private ImageView keyImage;

    private ImageView potionImage;

    private ImageView swordImage;

    @FXML
    private GridPane goalGrid;

    private GoalComponent goal;

    private ArrayList<Label> labelList;

    @FXML
    private Label prohibitedLabel;

    @FXML
    private GridPane instructionGrid;

    public DungeonController(Dungeon dungeon, List<ImageView> initialEntities, String map) {
        this.dungeon = dungeon;
        player = dungeon.getPlayer();
        this.initialEntities = new ArrayList<>(initialEntities);
        this.map = map;
        goal = dungeon.getGoal();
        labelList = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        Image ground = new Image((new File("images/dirt_0_new.png")).toURI().toString());

        // Add the ground first so it is below all other entities
        for (int x = 0; x < dungeon.getWidth(); x++) {
            for (int y = 0; y < dungeon.getHeight(); y++) {
                dungeonGrid.add(new ImageView(ground), x, y);
            }
        }
        for (ImageView entity : initialEntities) {
            dungeonGrid.getChildren().add(entity);
        }
        trackComplete(dungeon, dungeonGrid);

        // set up inventory
        player.getKeyProperty().bindBidirectional(player.getKeyView().imageProperty());
        player.getPotionProperty().bindBidirectional(player.getPotionView().imageProperty());
        player.getSwordProperty().bindBidirectional(player.getSwordView().imageProperty());
        inventoryGrid.add(player.getKeyView(), 0, 0);
        inventoryGrid.add(player.getPotionView(), 1, 0);
        inventoryGrid.add(player.getSwordView(), 2, 0);

        // add tool tip for inventory items
        player.addToolTip();

        // set up goal display
        int x = 0;
        int y = 0;
        if (goal instanceof GoalLeaf) {
            addGoalLeafToGrid((GoalLeaf)goal, x, y);
        } else {
            addGoalCompositeToGrid((GoalComposite)goal, x, y);
        }
        player.setProhibitedLabel(prohibitedLabel);
    }

    public void addGoalLeafToGrid(GoalLeaf goal, int x, int y) {
        Label goalDes = new Label();
        if (goal.getGoal().equals("exit")) {
            goalDes.setText("exit: reach the exit");
            goalDes.setTextFill(Color.web("#ffffff"));
            goalDes.setFont(new Font("Comic Sans MS", 12));
            goalGrid.add(goalDes, x, y);
        } else {
            goalDes.setText(goal.getGoal() + " left: ");
            goalDes.setTextFill(Color.web("#ffffff"));
            Label goalNum = new Label();
            goalNum.setText(goal.getRemainingQuestAsInt() + " ");
            goalNum.setTextFill(Color.web("#ffffff"));
            goalNum.setFont(new Font("Comic Sans MS", 12));

            labelList.add(goalNum);
            labelList.get(labelList.size() - 1).textProperty().bindBidirectional(goal.getRemainingQuest(), new NumberStringConverter());
            goalGrid.add(goalDes, x, y);
            goalGrid.add(labelList.get(labelList.size() - 1), x + 1, y);
        }
    }

    public void addGoalCompositeToGrid(GoalComposite goal, int x, int y) {
        for (GoalComponent g: goal.getGoalList()) {
            if (g instanceof GoalLeaf) {
                addGoalLeafToGrid((GoalLeaf)g, x, y);
                x = x + 3;
            } else {
                addGoalCompositeToGrid((GoalComposite)g, x, y);
            }
            // stop if its the last goal
            if (goal.getGoalList().indexOf(g) != goal.getGoalList().size() - 1) {
                Label spacing = new Label();
                spacing.setText(" " + goal.getType() + " ");
                spacing.setTextFill(Color.web("#ffffff"));
                spacing.setFont(new Font("Comic Sans MS", 12));
                goalGrid.add(spacing, x - 1, y);
            }
        }
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
        case UP:
            player.moveUp();
            break;
        case DOWN:
            player.moveDown();
            break;
        case LEFT:
            player.moveLeft();
            break;
        case RIGHT:
            player.moveRight();
            break;
        case ESCAPE:
            // Change focus of scenes to restart menu
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = (Scene)((Node)event.getSource()).getScene();

            // Make a new scene with the pause menu
            PauseController c = new PauseController(scene, "Paused", map);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PauseView.fxml"));
            loader.setController(c);
            try {
                Parent pauseRoot = loader.load();
                Scene pause = new Scene(pauseRoot);
                pauseRoot.requestFocus();
                stage.setScene(pause);
                stage.show();
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
                stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
            } catch (Exception e) {
                System.out.println("Could not open pause menu: " + e.toString());
            }
            break;
        default:
            break;
        }
    }

    private void trackComplete(Dungeon d, Node node) {
        d.getComplete().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable,
            Boolean oldValue, Boolean newValue)  {
                // When it's complete, we show the pause screen
                Stage stage = (Stage)(node.getScene().getWindow());
                Scene scene = (Scene)(node.getScene());

                // Make a new scene with the pause menu
                PauseController c = new PauseController(scene, "You Win!", map);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("PauseView.fxml"));
                loader.setController(c);
                try {
                    Parent pauseRoot = loader.load();
                    Scene pause = new Scene(pauseRoot);
                    pauseRoot.requestFocus();
                    stage.setScene(pause);
                    stage.show();
                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2); 
                    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
                } catch (Exception e) {
                    System.out.println("Could not open pause menu: " + e.toString());
                }
                
            }
        });
    }

}

