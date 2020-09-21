package unsw.dungeon;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

/**
 * The player entity
 * @author Robert Clifton-Everest
 *
 */
public class Player extends Entity implements Subject {

    private Dungeon dungeon;
    private PlayerState state;
    private PlayerState potionState;
    private PlayerState normalState;
    private PlayerState armedState;
    private ArrayList<Entity> inventory;
    private IntegerProperty futureX, futureY;
    private ArrayList<Enemy> enemyObserver;
    private ArrayList<GoalLeaf> goalObserver;
    private int treasureCollected;
    private BooleanProperty dead;
    private Image keyImage;
    private Image potionImage;
    private Image swordImage;
    private ImageView keyView;
    private ImageView potionView;
    private ImageView swordView;
    private ImageView playerView;
    private ObjectProperty<Image> keyProperty;
    private ObjectProperty<Image> potionProperty;
    private ObjectProperty<Image> swordProperty;
    private Timeline timeline;
    private int animationCounter;
    private Label prohibitedLabel;
    private Timeline prohibitedTimeline;

    /**
     * Create a player positioned in square (x,y)
     * @param dungeon
     * @param x
     * @param y
     */
    public Player(Dungeon dungeon, int x, int y) {
        super(x, y, "player");
        this.dungeon = dungeon;
        potionState = new PotionState(this);
        normalState = new NormalState(this);
        armedState = new ArmedState(this);
        state = new NormalState(this);
        inventory = new ArrayList<Entity>();
        futureX = new SimpleIntegerProperty(x);
        futureY = new SimpleIntegerProperty(y);
        enemyObserver = new ArrayList<Enemy>();
        goalObserver = new ArrayList<GoalLeaf>();
        treasureCollected = 0;
        dead = new SimpleBooleanProperty(false);

        keyImage = new Image((new File("images/key_grey.png")).toURI().toString());
        potionImage = new Image((new File("images/brilliant_blue_new_grey.png")).toURI().toString());
        swordImage = new Image((new File("images/greatsword_grey.png")).toURI().toString());
        keyView = new ImageView(keyImage);
        potionView = new ImageView(potionImage);
        swordView = new ImageView(swordImage);
        keyProperty = new SimpleObjectProperty();
        potionProperty = new SimpleObjectProperty();
        swordProperty = new SimpleObjectProperty();
        keyProperty.set(keyImage);
        potionProperty.set(potionImage);
        swordProperty.set(swordImage);
    }

    /**
     * Moves the player up a square.
     * Enemy moves accordingly.
     */
    public void moveUp() {
        // Set the future coordinates to expected coordinates
        setFutureX(getX());
        setFutureY(getY() - 1);

        // Check if they will collide with anything
        ArrayList<Entity> list = checkIfCollision(getX(), getY() - 1);
        if (!list.isEmpty()) { // One item there
            for (Entity e: list){
                e.collide(this);
            }
        }

        // Move the player
        x().set(futureX.get());
        if (getY() > 0) {
            y().set(futureY.get());
        }

        // Move the enemy
        Iterator<Enemy> itr = enemyObserver.iterator();
        while (itr.hasNext()) {
            Enemy e = itr.next();
            if (e.getState().equals("chase")) {
                e.basicMovement(this);
            } else if (e.getState().equals("run")) {
                e.runAway(this);
            }
        }
    }

    /**
     * Moves the player down a square.
     * Enemy moves accordingly.
     */
    public void moveDown() {
        // Set the future coordinates to expected coordinates
        setFutureX(getX());
        setFutureY(getY() + 1);

        // Check if they will collide with anything
        ArrayList<Entity> list = checkIfCollision(getX(), getY() + 1);
        if (!list.isEmpty()) { // One item there
            for (Entity e: list){
                e.collide(this);
            }
        }

        // Move the player
        x().set(futureX.get());
        if (getY() < dungeon.getHeight() - 1) {
            y().set(futureY.get());
        }

        // Move the enemy
        Iterator<Enemy> itr = enemyObserver.iterator();
        while (itr.hasNext()) {
            Enemy enemy = itr.next();
            if (enemy.getState().equals("chase")) {
                enemy.basicMovement(this);
            } else if (enemy.getState().equals("run")) {
                enemy.runAway(this);
            }
        }
    }

    /**
     * Moves the player left a square.
     * Enemy moves accordingly.
     */
    public void moveLeft() {
        // Set the future coordinates to expected coordinates
        setFutureX(getX() - 1);
        setFutureY(getY());

        // Check if they will collide with anything
        ArrayList<Entity> list = checkIfCollision(getX() - 1, getY());
        if (!list.isEmpty()) { // One item there
            for (Entity e: list){
                e.collide(this);
            }
        }

        // Move the player
        y().set(futureY.get());
        if (getX() > 0) {
            x().set(futureX.get());
        }

        // Move the enemy
        Iterator<Enemy> itr = enemyObserver.iterator();
        while (itr.hasNext()) {
            Enemy e = itr.next();
            if (e.getState().equals("chase")) {
                e.basicMovement(this);
            } else if (e.getState().equals("run")) {
                e.runAway(this);
            }
        }
    }

    /**
     * Moves the player right a square.
     * Enemy moves accordingly.
     */
    public void moveRight() {
        // Set the future coordinates to expected coordinates
        setFutureX(getX() + 1);
        setFutureY(getY());

        // Check if they will collide with anything
        ArrayList<Entity> list = checkIfCollision(getX() + 1, getY());
        if (!list.isEmpty()) { // One item there
            for (Entity e: list){
                e.collide(this);
            }
        }

        // Move the player
        y().set(futureY.get());
        if (getX() < dungeon.getWidth() - 1) {
            x().set(futureX.get());
        }

        // Move the enemy
        Iterator<Enemy> itr = enemyObserver.iterator();
        while (itr.hasNext()) {
            Enemy e = itr.next();
            if (e.getState().equals("chase")) {
                e.basicMovement(this);
            } else if (e.getState().equals("run")) {
                e.runAway(this);
            }
        }
    }

    public void setState(PlayerState s) {
        this.state = s;
    }

    public PlayerState getState() {
        return state;
    }

    public PlayerState getPotionState() {
        return potionState;
    }

    public PlayerState getNormalState() {
        return normalState;
    }
    
    public PlayerState getArmedState() {
        return armedState;
    }

    public void setFutureX(int x) {
        futureX = new SimpleIntegerProperty(x);
    }

    public void setFutureY(int y) {
        futureY = new SimpleIntegerProperty(y);
    }

    /**
     * Adds a treasure to the treasures the player has collected so far
     * @param treasure the treasure object the player has picked up
     */
    public void addTreasure(Treasure treasure) {
        treasure.setVisibility(false);
        dungeon.removeEntity(treasure);       
        treasureCollected++;
    }

    /**
     * Adds an item to the player's inventory
     * @param e the item you want to add
     */
    public void addInventory(Entity e) {
        // Make sure the player doesn't have this type of item yet (only allowed to carry one of each)
        if (!checkIfInInventory(e)) {
            // remove from the floor and add to player's inventory
            e.setVisibility(false);
            dungeon.removeEntity(e);
            inventory.add(e);
            if (e.getName().equals("key")) {
                keyImage = new Image((new File("images/key.png")).toURI().toString());
                keyProperty.set(keyImage);
                changeKeyToolTip(true, (Key)e);
                playSoundEffect("sounds/keyPickUp.mp3");
            } else if (e.getName().equals("potion")) {
                potionImage = new Image((new File("images/brilliant_blue_new.png")).toURI().toString());
                potionProperty.set(potionImage);
                changePotionToolTip(true, (Potion)e);
                playSoundEffect("sounds/potionPickUp.mp3");
            } else if (e.getName().equals("sword")) {
                swordImage = new Image((new File("images/greatsword_1_new.png")).toURI().toString());
                swordProperty.set(swordImage);
                changeSwordToolTip(true, (Sword)e);
                playSoundEffect("sounds/swordPickUp.mp3");
            }
        } else {
            displayProhibitedAction("Can't pick up more than 1 item of the same type!");
        }
    }

    /**
     * Removes the specified item from the player's inventory
     * @param e item to be removed
     */
    public void removeInventory(Entity e) {
        inventory.remove(e);
        if (e.getName().equals("key")) {
            keyImage = new Image((new File("images/key_grey.png")).toURI().toString());
            keyProperty.set(keyImage);
            changeKeyToolTip(false, (Key)e);
        } else if (e.getName().equals("potion")) {
            potionImage = new Image((new File("images/brilliant_blue_new_grey.png")).toURI().toString());
            potionProperty.set(potionImage);
            changePotionToolTip(false, (Potion)e);
        } else if (e.getName().equals("sword")) {
            swordImage = new Image((new File("images/greatsword_grey.png")).toURI().toString());
            swordProperty.set(swordImage);
            changeSwordToolTip(false, (Sword)e);
        }
    }

    /**
     * Gets a key out of the player's inventory
     * @return the Key object
     */
    public Key getKey() {
        for (Entity e: inventory) {
            if (e instanceof Key) {
                return (Key)e;
            }
        }
        return null;
    }

    /**
     * Finds a matching portal inside the dungeon
     * @param p The first portal in the match
     * @return The matching portal
     */
    public Portal getMatchingPortal(Portal p) {
        return dungeon.findMatchingPortal(p);
    }

    /**
     * Checks if an entity lies at coordinates (x, y)
     * @param x X coordinate of the space we want to check
     * @param y Y coordinate of the space we want to check
     * @return Entity that occupies (x,y) in the dungeon, if any
     */
    public ArrayList<Entity> checkIfCollision(int x, int y) {
        return dungeon.checkOccupied(x, y);
    }

    /**
     * Checks whether the player is lethal to enemies or not.
     * @return if player is holding potion or sword they are lethal, otherwise not.
     */
    public boolean isLethal() {
        if (state instanceof NormalState) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Kills an enemy, removing it from the dungeon
     * @param e the enemy to be killed
     */
    public void killEnemy(Entity e) {
        // Notify the goals to check if all enemies have been killed
        notifyObservers(e, "goals", "decrement");
        notifyObservers(e, "dungeon", "checkIfLevelComplete");
        // Remove enemy from dungeon and minus one from sword durability
        if (e instanceof Enemy) {
            enemyObserver.remove(e);
        }
        e.setVisibility(false);
        dungeon.removeEntity(e);
        // If player is not in potion state
        if (!(getState() instanceof PotionState)) {
            // Check inventory for sword
            Sword s = null;
            for (Entity entity: inventory) {
                if (entity instanceof Sword) {
                    s = (Sword)entity;
                }
            }
            s.minusHit();
            if (s.checkIfBroken()) {
                removeInventory(s);
                state.exitLethalMode();
            } else {
                changeSwordToolTip(true, s);
            }
            playSoundEffect("sounds/swordHit.mp3");
            killEnemyAnimation();
        } else {
            playSoundEffect("sounds/potionHit.mp3");
        }
    }

    /**
     * Called when the player dies.
     */
    public void die() {
        playSoundEffect("sounds/gameOver.mp3");
        dungeon.removeEntity(this);
        this.setVisibility(false);
        dead.set(true);
        System.out.println("Game Over! Better luck next time.");
    }

    /**
     * Called when the player successfully finishes the level
     */
    public void finishLevel() {
        dungeon.setComplete(true);
        playSoundEffect("sounds/levelComplete2.mp3");
        System.out.println("You've finished the level woo!");
    }

    /**
     * Adds an observer to the player
     * @param o the observer
     * @param observerName the name of the list it will be added to
     */
    public void addObserver(Observer o, String observerName) {
        if (observerName.equals("enemies")) {
            enemyObserver.add((Enemy)o);
        } else if (observerName.equals("goals")) {
            goalObserver.add((GoalLeaf)o);
        }
    }

    /**
     * Removes an observer from the player
     * @param o the observer
     * @param observerName the name of the list it will be removed from
     */
    public void removeObserver(Observer o, String observerName) {
        if (observerName.equals("enemies")) {
            enemyObserver.remove(o);
        } else if (observerName.equals("goals")) {
            goalObserver.remove(o);
        }
    }

    /**
     * Notifies the list of observer requested
     * @param e the entity subjected to the notification
     * @param observerName the name of the list that will be notified
     * @param request the requested action to be notified
     */
    public void notifyObservers(Entity e, String observerName, String request) {
        if (observerName.equals("enemies")) {
            System.out.println("notifying enemies to " + request);
            for (Enemy o: enemyObserver) {
                o.update(e, request);
            }
        } else if (observerName.equals("goals")) {
            for (GoalLeaf o: goalObserver) {
                o.update(e, request);
            }
        } else if (observerName.equals("dungeon")) {
            dungeon.update(e, request);
        }
    }

    /**
     * Checks whether the observer object passed in is an observer for the player
     * @param e the observer object in question
     * @param observerName the name of the list it is checked against
     * @return true or false
     */
    public boolean isObserver(Observer e, String observerName) {
        if (observerName.equals("enemies")) {
            for (Enemy o :enemyObserver) {
                if (o == (Enemy)e) {
                    return true;
                }
            }
        } else if (observerName.equals("goals")) {
            for (GoalLeaf o: goalObserver) {
                if (o == (GoalLeaf)e) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getTreasureCollected() {
        return treasureCollected;
    }

    /**
     * Checks if the item type is already inside the player's inventory
     * @param e the item we want to check
     * @return true if there already is, false otherwise
     */
    public boolean checkIfInInventory(Entity e) {
        for (Entity ent: inventory) {
            if (ent.getName().equals(e.getName())) {
                return true;
            }
        }
        return false; 
    }

    /**
     * Checks whether the given coordinates is within the dungeon's scope
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true or false
     */
    public boolean checkIfWithinDungeon(int x, int y) {
        int width = dungeon.getWidth();
        int height = dungeon.getHeight();
        if (x >= 0 && x <= width) {
            if (y >= 0 && y <= height) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the dungeon is complete
     * @return true or false
     */
    public boolean checkIfDungeonComplete() {
        if (dungeon.isLevelComplete()) {
            return true;
        }
        return false;
    }

    public BooleanProperty getDead() {
        return dead;
    }

    /**
     * Plays a sound file
     * @param mFile the sound file to be played
     */
    public void playSoundEffect(String mFile) {
        Media sound = new Media(new File(mFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public ImageView getKeyView() {
        return keyView;
    }

    public ImageView getPotionView() {
        return potionView;
    }

    public ImageView getSwordView() {
        return swordView;
    }

    public ObjectProperty<Image> getKeyProperty() {
        return keyProperty;
    }

    public ObjectProperty<Image> getPotionProperty() {
        return potionProperty;
    }

    public ObjectProperty<Image> getSwordProperty() {
        return swordProperty;
    }

    /**
     * Adds tooltips for the images in inventory
     */
    public void addToolTip() {
        Tooltip.install(keyView, new Tooltip("not in inventory"));
        Tooltip.install(potionView, new Tooltip("not in inventory"));
        Tooltip.install(swordView, new Tooltip("not in inventory"));
    }

    /**
     * Changes the tooltip for the key image in inventory
     * @param isInInventory whether the key is in inventory
     * @param k the key
     */
    public void changeKeyToolTip(boolean isInInventory, Key k) {
        if (isInInventory) {
            Tooltip.install(keyView, new Tooltip("id: " + k.getId()));
        } else {
            Tooltip.install(keyView, new Tooltip("not in inventory"));
        }
    }

    /**
     * Changes the tooltip for the potion image in inventory
     * @param isInInventory whether the potion is in inventory
     * @param p the potion
     */
    public void changePotionToolTip(boolean isInInventory, Potion p) {
        if (isInInventory) {
            Tooltip.install(potionView, new Tooltip("potion currently effective"));
        } else {
            Tooltip.install(potionView, new Tooltip("not in inventory"));
        }
    }

    /**
     * Changes the tooltip for the sword image in inventory
     * @param isInInventory whether the sword is in inventory
     * @param s the sword
     */
    public void changeSwordToolTip(boolean isInInventory, Sword s) {
        if (isInInventory) {
            Tooltip.install(swordView, new Tooltip("uses left: " + s.getHits()));
        } else {
            Tooltip.install(swordView, new Tooltip("not in inventory"));
        }
    }

    public void setPlayerView(ImageView view) {
        playerView = view;
    }

    /**
     * Plays the enemy slaying animation
     */
    public void killEnemyAnimation() {
        timeline = new Timeline();
        timeline.setCycleCount(4);
        animationCounter = 1;
        KeyFrame k = new KeyFrame(Duration.millis(100), event -> {
            if (animationCounter == 4) {
                playerView.setImage(new Image((new File("images/human_new.png")).toURI().toString()));
            } else {
                playerView.setImage(new Image((new File("images/human_sword_" + animationCounter + ".png")).toURI().toString()));
            }
            animationCounter++;
        });
        timeline.getKeyFrames().add(k);
        timeline.play();
    }

    public void setProhibitedLabel(Label label) {
        prohibitedLabel = label;
    }

    /**
     * Displays a text that outlines the player doing a prohibited action,
     * disappears after 2 seconds
     * @param text the text
     */
    public void displayProhibitedAction(String text) {
        prohibitedLabel.setText(text);
        prohibitedTimeline = new Timeline();
        prohibitedTimeline.setCycleCount(1);
        KeyFrame k = new KeyFrame(Duration.millis(2000), event -> {
            prohibitedLabel.setText(" ");
        });
        prohibitedTimeline.getKeyFrames().add(k);
        prohibitedTimeline.play();
    }
}
