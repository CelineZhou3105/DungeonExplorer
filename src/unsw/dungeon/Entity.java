package unsw.dungeon;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * An entity in the dungeon.
 * @author Robert Clifton-Everest
 *
 */
public class Entity {

    // IntegerProperty is used so that changes to the entities position can be
    // externally observed.
    private String name;
    private IntegerProperty x, y;
    private BooleanProperty visibility;
    /**
     * Create an entity positioned in square (x,y)
     * @param x
     * @param y
     */
    public Entity(int x, int y, String name) {
        this.name = name;
        this.x = new SimpleIntegerProperty(x);
        this.y = new SimpleIntegerProperty(y);
        visibility = new SimpleBooleanProperty(true);
    }

    /**
     * Gets the BooleanProperty for the visibility of the entity
     */
    public BooleanProperty getVisibility() {
        return visibility;
    }

    /**
     * Sets the BooleanProperty for the visibility of the entity
     * @param b new value
     */
    public void setVisibility(boolean b) {
        visibility.set(b);
    }

    /**
     * Gets the name of the entity, can be one of "player", "wall", "exit", "treasure", "door", "key", "boulder", "switch", "portal", "enemy", "sword", "potion"
     * @return name of the entity
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the integer property for entity's x coordinate
     * @return IntegerProperty object
     */
    public IntegerProperty x() {
        return x;
    }

    /**
     * Gets the integer property for entity's y coordinate
     * @return IntegerProperty object
     */
    public IntegerProperty y() {
        return y;
    }

    /**
     * Gets the int value for the entity's x coordinate
     * @return x-coordinate
     */
    public int getY() {
        return y().get();
    }

    /**
     * Gets the int value for the entity's y coordinate
     * @return y-coordinate
     */
    public int getX() {
        return x().get();
    }

    /**
     * Sets the int value for the entity's x coordinate
     * @param x value we want to set it to
     */
    public void setX(int x) {
        this.x = new SimpleIntegerProperty(x);
    }

    /**
     * Sets the int value for the entity's y coordinate
     * @param x value we want to set it to
     */
    public void setY(int y) {
        this.y = new SimpleIntegerProperty(y);
    }

    public void collide(Player p) {
        // Do nothing
    }
}
