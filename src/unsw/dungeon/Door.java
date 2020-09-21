package unsw.dungeon;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Door extends Entity {
    private int id;
    private BooleanProperty open;
    public Door(int id, int x, int y) {
        super(x, y, "door");
        this.id = id;  
        open = new SimpleBooleanProperty(false);
    }

    /**
     * Opens the door
     */
    public void open() {
        open.set(true);
        //state = "Open";
    }

    /**
     * Gets the id of the door
     * @return id of the door
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the state of the door
     * @return "Open" or "Closed" 
     */
    public BooleanProperty getState() {
        return open;
        //return state;
    }

    /**
     * Controls the collision behaviour between player and door
     * @param p player
     */
    public void collide(Player p) {
        if (open.get() == true) { // Player can move through
            // Don't need to change future X and future Y, still the same
        } else {
            // Player cannot move since it is closed
            p.setFutureX(p.getX());
            p.setFutureY(p.getY());

            // Check if player has right Key
            Key k = p.getKey();
            if (k == null) { // do nothing, we can't open the door
                
            } else if (k.getId() == id) { // open the door, remove the key
                p.playSoundEffect("sounds/openDoor.mp3");
                open();
                p.removeInventory(k); 
            } else if (k.getId() != id) { // key does not match the door, prohibited action
                p.displayProhibitedAction("Can't open door with the wrong key!");
            }
        }
    }

}