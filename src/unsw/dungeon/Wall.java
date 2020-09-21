package unsw.dungeon;

public class Wall extends Entity {

    public Wall(int x, int y) {
        super(x, y, "wall");
    }

    /**
     * Controls the collision behaviour when player hits a wall
     * @param p player 
     */
    @Override
    public void collide(Player p) {
        // Player is not allowed to move
        p.setFutureX(p.getX());
        p.setFutureY(p.getY());
    }
}
