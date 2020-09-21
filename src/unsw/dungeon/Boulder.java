package unsw.dungeon;

import java.util.ArrayList;

public class Boulder extends Entity {
    public Boulder(int x, int y) {
        super(x, y, "boulders");
    }

    /**
     * Controls collision behaviour between player and boulders
     * @param p
     */
    public void collide(Player p) {
        // Grab the x and y coordinates of the player
        int player_x = p.getX();
        int player_y = p.getY();

        // Grab the coordinates of this entity
        int entity_x = getX();
        int entity_y = getY();
        int entity_future_x = entity_x;
        int entity_future_y = entity_y;

        // Determine the direction that the entity is being pushed in
        ArrayList<Entity> a;
        
        if (player_x < entity_x) { // Player pushing right
            a = p.checkIfCollision(entity_x + 1, entity_y);
            entity_future_x += 1;

        } else if (player_x > entity_x) { // Player pushing left
            a = p.checkIfCollision(entity_x - 1, entity_y);
            entity_future_x -= 1;

        } else if (player_y < entity_y) { // Player pushing down
            a = p.checkIfCollision(entity_x, entity_y + 1);
            entity_future_y += 1;

        } else { // Player pushing up
            a = p.checkIfCollision(entity_x, entity_y - 1);
            entity_future_y -= 1;
        }

        boolean boulder_moved = false;
        // Checks if we collide with anything
        if (a.size() == 0) { // Nothing there
            // Future x and y of player are correct, set entity to move 
            x().set(entity_future_x);
            y().set(entity_future_y);
            boulder_moved = true;
        } else {
            // Check whether there's a switch there
            boolean contains_switch = false;
            FloorSwitch floorSwitch = null;

            for (Entity entity: a) {
                if (entity.getName().equals("switch")) {
                    contains_switch = true;
                    floorSwitch = (FloorSwitch)entity;
                }
            }

            if (!contains_switch) { // No switch but something else is there --> can't move as player
                p.setFutureX(player_x);
                p.setFutureY(player_y);
            } else if (contains_switch && a.size() == 1) { // contains a switch by itself --> move the boulder, activate switch, 
                x().set(entity_future_x);
                y().set(entity_future_y);
                (floorSwitch).on();
                p.playSoundEffect("sounds/switchOn.mp3");
                p.notifyObservers(this, "goals", "decrement");
                p.notifyObservers(this, "dungeon", "checkIfLevelComplete");
                boulder_moved = true;
            } else if (contains_switch && a.size() > 1) { // contains a switch + boulder --> can't move as player
                p.setFutureX(player_x);
                p.setFutureY(player_y);
            } 
        } 

        // Check if the boulder we're moving already was on a switch
        if (boulder_moved) {
            ArrayList<Entity> b;
            b = p.checkIfCollision(entity_x, entity_y);
            for (Entity entity: b) {
                if (entity.getName().equals("switch")) {
                    ((FloorSwitch)entity).off();
                    p.playSoundEffect("sounds/switchOff.mp3");
                    p.notifyObservers(this, "goals", "increment");
                    break;
                }
            }
        }
    }
}