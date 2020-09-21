package unsw.dungeon;

public class PotionState implements PlayerState {
    private Player player;
    public PotionState(Player p) {
        this.player = p;
    }

    /**
     * When the player picks up a potion while in a potion state
     */
    public void pickupPotion() {
        // Do nothing
        System.out.println("You can't pickup another potion while consuming one.");
    }

    /**
     * When the player picks up a sword while in a potion state
     */
    public void pickupSword() {
        // Do nothing
        System.out.println("You've got a sword, but potion is stronger so it is still active");
    }

    /**
     * When the potion runs out, the player will either
     * change back to normal state or
     * change to armed state if they have a sword in inventory
     */
    public void exitLethalMode() {
        Sword s = new Sword(1,1);
        if (player.checkIfInInventory(s)) {
            // go back to armed state since player still has sword
            player.setState(player.getArmedState());
        } else {
            player.setState(player.getNormalState());
        }
    }
}