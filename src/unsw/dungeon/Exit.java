package unsw.dungeon;

public class Exit extends Entity {
    private String state;
    public Exit(int x, int y) {
        super(x, y, "exit");
        state = "Closed";
    }

    /**
     * Opens the exit (done when goals are complete)
     */
    public void open() {
        state = "Open";
    }

    /**
     * Gets the state of the exit
     * @return whether it is open or closed
     */
    public String getState() {
        return state;
    }

    /**
     * Controls the collision behaviour between player and exit
     * @param p player
     */
    @Override
    public void collide(Player p) {
        if (p.checkIfDungeonComplete()) {
            if (state.equals("Open")) {
                // Leave the future coordinates at they are, finish the level
                p.finishLevel();
            } else {
                open();
                // Don't let player move
                p.setFutureX(p.getX());
                p.setFutureY(p.getY());
            }
        } else {
            // Don't let player move
            p.setFutureX(p.getX());
            p.setFutureY(p.getY());
            p.displayProhibitedAction("Can't exit until all the required goals are completed!");
        }
    }
}