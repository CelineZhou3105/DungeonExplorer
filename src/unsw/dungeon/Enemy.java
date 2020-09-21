package unsw.dungeon;

import java.util.ArrayList;

public class Enemy extends Entity implements Observer{
    private String state;

    public Enemy(int x, int y) {
        super(x, y, "enemies");
        state = "chase";
    }

    /**
     * Gets the state of the enemy
     * @return "run" if the enemy is running away, "chase" if the enemy is chasing the player
     */
    public String getState() {
        return state;
    }

    /**
     * Controls basic movement for the enemy. Moves towards the player. 
     * @param p player object
     */
    public void basicMovement(Player p) {
        int ex = super.getX();
        int ey = super.getY();
        int px = p.getX();
        int py = p.getY();
        if (ey < py) { // if enemy is above player, move down
            moveOnY(p, ex, ey, px, ey + 1);
        } else if (ey > py) { // if enemy is below player, move up
            moveOnY(p, ex, ey, px, ey - 1);
        } else if (ey == py) { // if enemy is on the same y level as player, move towards the player on x
            if (ex < px) { // if enemy is left of player, move right
                moveOnX(p, ex + 1, ey);
            } else if (ex > px) { // if enemy is right of player, move left
                moveOnX(p, ex - 1, ey);
            }
        }
    }

    /**
     * Controls the enemy's movement when running away from the player
     * @param p player object
     */
    public void runAway(Player p) {
        int ex = super.getX();
        int ey = super.getY();
        int px = p.getX();
        int py = p.getY();
        if (ey < py) { // if enemy is above player, move up
            runAwayY(p, ex, ey, px, ey - 1);
        } else if (ey > py) { // if enemy is below player, move down
            runAwayY(p, ex, ey, px, ey + 1);
        } else if (ey == py) { // if enemy is on the same y level as player, move away from the player on x
            if (ex < px) { // if enemy is left of player, move left
                runAwayX(p, ex - 1, ey);
            } else if (ex > px) { // if enemy is right of player, move right
                runAwayX(p, ex + 1, ey);
            }
        }
    }

    /**
     * Moves the enemy towards the player in the y axis
     * @param p the player object
     * @param ex the x coordinate of the enemy
     * @param ey the y coordinate of the enemy
     * @param px the x coordinate of the player
     * @param newCoordinateY the new y coordinate to move the enemy to
     */
    public void moveOnY(Player p, int ex, int ey, int px, int newCoordinateY) {
        ArrayList<Entity> collisionY = p.checkIfCollision(ex, newCoordinateY);
        if (collisionY.isEmpty()) { // if there's nothing there, move
            super.y().set(newCoordinateY);
        } else { // if there's something there
            for (Entity e: collisionY) {
                if (e instanceof Player) { // if its a player
                    if (!p.isLethal()) {
                        super.y().set(newCoordinateY);
                        p.die(); // kill the player
                    }
                    break;
                } else { // can't move down/up because something is in the way
                    if (ex < px) { // if enemy is left of player, move right
                        moveOnX(p, ex + 1, ey);
                    } else if (ex > px) { // if enemy is right of player, move left
                        moveOnX(p, ex - 1, ey);
                    } else if (ex == px) { // if enemy is on the same x level as player, move left or right
                        int tempX = ex;
                        moveOnX(p, ex + 1, ey); // move right
                        if (tempX == super.getX()) { // if move right failed
                            moveOnX(p, ex - 1, ey); // move left
                        }
                    }
                }
            }
        }
    }

    /**
     * Moves the enemy towards the player in the x axis
     * @param p the player object
     * @param newCoordinateX the new x coordinate to move the enemy to
     * @param y the y coordinate of the enemy
     */
    public void moveOnX(Player p, int newCoordinateX, int y) {
        ArrayList<Entity> collisionX = p.checkIfCollision(newCoordinateX, y);
        if (collisionX.isEmpty()) { // if nothing is in the way, move
            super.x().set(newCoordinateX);
        } else {
            for (Entity e: collisionX) {
                if (e instanceof Player) { // if its a player
                    if (!p.isLethal()) { // if player is lethal, enemy dies
                        super.x().set(newCoordinateX);
                        p.die();
                    }
                    break;
                }
            }
        }
    }

    /**
     * Moves the enemy away from the player in the y axis
     * @param p the player object
     * @param ex the x coordinate of the enemy
     * @param ey the y coordinate of the enemy
     * @param px the x coordinate of the player
     * @param newCoordinateY the new y coordinate to move the enemy to
     */
    public void runAwayY(Player p, int ex, int ey, int px, int newCoordinateY) {
        ArrayList<Entity> collisionY = p.checkIfCollision(ex, newCoordinateY);
        if (collisionY.isEmpty() && p.checkIfWithinDungeon(ex, newCoordinateY)) {
            super.y().set(newCoordinateY);
        } else {
            if (ex < px) {
                runAwayX(p, ex - 1, ey);
            } else if (ex > px) {
                runAwayX(p, ex + 1, ey);
            }
        }
    }

    /**
     * Moves the enemy away from the player in the x axis
     * @param p the player object
     * @param newCoordinateX the new x coordinate to move the enemy to
     * @param y the y coordinate of the enemy
     */
    public void runAwayX(Player p, int newCoordinateX, int y) {
        ArrayList<Entity> collisionX = p.checkIfCollision(newCoordinateX, y);
        if (collisionX.isEmpty() && p.checkIfWithinDungeon(newCoordinateX, y)) {
            super.x().set(newCoordinateX);
        }
    }

    /**
     * When the enemy is called for an update, change the state to run or chase
     * @param e the entity the update call is related to
     * @param request the action to be made by the update call
     */
    @Override
    public void update(Entity e, String request) {
        if (e.getName().equals("potion") && request.equals("run")) {
            state = "run";
        } else if (e.getName().equals("potion") && request.equals("chase")) {
            state = "chase";
        }
    }


    /**
     * Controls collision behaviour between player and enemy
     * @param p player
     */
    @Override
    public void collide(Player p) {
        if (p.isLethal()) {
            // Enemy dies
            p.killEnemy(this);
        } else {
            p.die();
        }
    }
}
