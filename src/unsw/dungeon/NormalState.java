package unsw.dungeon;

public class NormalState implements PlayerState {
    private Player player;
    public NormalState(Player p) {
        this.player = p;
    }

    /**
     * When the player picks up a potion while in a normal state
     */
    public void pickupPotion() {
        player.setState(player.getPotionState());
    }

    /**
     * When the player picks up a sword while in a normal state
     */
    public void pickupSword() {
        player.setState(player.getArmedState());
    }

    /**
     * When the player tries to return to normal state while in a normal state
     */
    public void exitLethalMode() {
        // Do nothing
        System.out.println("You're already not holding anything lethal to enemies.");
    }
}