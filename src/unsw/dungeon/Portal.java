package unsw.dungeon;

public class Portal extends Entity {
    private int id;
    public Portal(int id, int x, int y) { 
        super(x, y, "Portal");
        this.id = id;
    }

    /**
     * Gets the id of the portal.
     * @return id of the portal
     */
    public int getId() {
        return id;
    }

    /**
     * Controls the collision behaviour between player and portal
     * @param p player
     */
    @Override
    public void collide(Player p) {
        // Get the matching portal
        Portal matchingPortal = p.getMatchingPortal(this);

        // Set the player's future coordinates to the matching portal
        p.setFutureX(matchingPortal.getX());
        p.setFutureY(matchingPortal.getY());
        p.playSoundEffect("sounds/teleport2.mp3");
    }
}