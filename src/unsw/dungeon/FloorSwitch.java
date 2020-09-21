package unsw.dungeon;

public class FloorSwitch extends Entity {
    private String state;
    public FloorSwitch(int x, int y) {
        super(x, y, "switch");
        this.state = "Off";
    }

    /**
     * Turns on the switch - when boulder is on it
     */
    public void on() {
        state = "On";
    }
    
    /**
     * Turns off the switch - when boulder is not on it
     */
    public void off() {
        state = "Off";
    }

    /**
     * Controls the collision behaviour between player and switch
     * @param p player
     */
    @Override
    public void collide(Player p) {
        // Do nothing, the player is allowed to move onto a switch
    }

    /**
     * Gets whether the switch is on or off. 
     * @return "On" or "Off" 
     */
    public String getState() {
        return state;
    }
    
}