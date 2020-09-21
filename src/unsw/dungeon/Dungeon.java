/**
 *
 */
package unsw.dungeon;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.BooleanProperty;

/**
 * A dungeon in the interactive dungeon player.
 *
 * A dungeon can contain many entities, each occupy a square. More than one
 * entity can occupy the same square.
 *
 * @author Robert Clifton-Everest
 *
 */
public class Dungeon implements Observer{

    private int width, height;
    private List<Entity> entities;
    private Player player;
    private GoalComponent goal;
    private BooleanProperty complete;

    public Dungeon(int width, int height) {
        this.width = width;
        this.height = height;
        this.entities = new ArrayList<>();
        this.player = null;
        this.complete = new SimpleBooleanProperty(false);
    }

    /**
     * Returns the boolean property that tracks whether the dungeon is complete
     */
    public BooleanProperty getComplete() {
        return complete;
    }

    /**
     * Sets the dungeon to complete.
     * @param b the state of the dungeon
     */
    public void setComplete(boolean b) {
        complete.set(b);
    }

    /**
     * Gets the width of the dungeon.
     * @return width of the dungeon
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the dungeon.
     * @return height of the dungeon.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the player inside the dungeon.
     * @return the player object
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player of the dungeon to one specified
     * @param player player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Adds an entity to the dungeon
     * @param entity entity to be added
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Removes an entity from the dungeon
     * @param entity entity to be removed
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    /**
     * Checks if a space in the dungeon is occupied by an entity
     * @param x X coordinate we want to check
     * @param y Y coordinate we want to check
     * @return the entity that occupies (x, y), if any
     */
    public ArrayList<Entity> checkOccupied(int x, int y) {
        ArrayList<Entity> list = new ArrayList<Entity>();

        for (Entity e: entities) {
            if (e.getX() == x && e.getY() == y) {
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Finds a matching portal
     * @param p
     * @return the portal object
     */
    public Portal findMatchingPortal(Portal p) {
        for (Entity e : entities) {
            if (e.getName().equals("Portal")) {
                if (e.getX() != p.getX() || e.getY() != p.getY()) { // Matching portal must have different coordinates
                    if (((Portal) e).getId() == p.getId()) {
                        return ((Portal) e);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Sets the goal of the dungeon
     * @param goal goal to set
     */
    public void setGoal(GoalComponent goal) {
        this.goal = goal;
    }

    /**
     * Gets the goal(s) of the dungeon
     * @return GoalComponent object of for the dungeon
     */
    public GoalComponent getGoal() {
        return goal;
    }

    /**
     * Updates the relevant entities to check if goals have been complete
     */
    @Override
    public void update(Entity e, String request) {
        if (goal instanceof GoalLeaf) {
            GoalLeaf single = (GoalLeaf)goal;
            if (single.getGoal().equals(e.getName()) && request.equals("checkIfLevelComplete")) {
                if (goal.isLevelComplete()) {
                    // open exit animation if exit exist
                    // level complete
                    player.finishLevel();
                }
            }
        } else if (goal instanceof GoalComposite) {
            GoalComposite multi = (GoalComposite)goal;
            if (multi.isLevelComplete() && request.equals("checkIfLevelComplete")) {
                boolean hasExitAsGoal = false;
                for (GoalComponent g: multi.getGoalList()) {
                    if (g instanceof GoalLeaf) {
                        GoalLeaf sg = (GoalLeaf)g;
                        if (sg.getGoal().equals("exit")) {
                            hasExitAsGoal = true;
                        }
                    }
                }
                if (!hasExitAsGoal) {
                    // open exit animation if exit exist
                    // level complete
                    player.finishLevel();
                }
            }
        }
    }

    /**
     * Counts the number of boulders in the dungeon
     * @return number of boulders
     */
    public int countBoulders() {
        int boulders = 0;
        for (Entity e: entities) {
            if (e.getName().equals("boulders")) {
                boulders++;
            }
        }
        return boulders;
    }

    /**
     * Counts the number of enemies in dungeon
     * @return number of enemies
     */
    public int countEnemies() {
        int enemies = 0;
        for (Entity e: entities) {
            if (e.getName().equals("enemies") || e.getName().equals("hound")) {
                enemies++;
            }
        }
        return enemies;
    }

    /**
     * Counts the number of treasures in dungeon
     * @return number of treasures
     */
    public int countTreasure() {
        int treasure = 0;
        for (Entity e: entities) {
            if (e.getName().equals("treasure")) {
                treasure++;
            }
        }
        return treasure;
    }

    /**
     * Checks if the level is complete
     * @return true, or false
     */
    public boolean isLevelComplete() {
        if (goal.isLevelComplete()) {
            return true;
        }
        return false;
    }

    /**
     * Returns the list of entities residing in the dungeon
     * @return an ArrayList of entities
     */
    public List<Entity> getEntityList() {
        return entities;
    }
}
