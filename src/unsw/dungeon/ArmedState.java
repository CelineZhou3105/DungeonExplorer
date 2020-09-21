package unsw.dungeon;

public class ArmedState implements PlayerState {
    private Player player;
    public ArmedState(Player p) {
        this.player = p;
    }
    /**
     * Function for when player is picking up potion while armed - Sets them to potionState instead
     */
    public void pickupPotion() {
        // Do nothing
        player.setState(player.getPotionState());
    }

    /**
     * Function for when player tries to pick up another sword while armed - does nothing
     */
    public void pickupSword() {
        System.out.println("You already have a sword. You can't pick up another one!");
    }

    /**
     * Function for when sword breaks and player is no longer lethal to enemies
     */
    public void exitLethalMode() {
        player.setState(player.getNormalState());
    }
}