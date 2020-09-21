package unsw.dungeon;

import java.util.Timer;
import java.util.TimerTask;

public class Potion extends Entity {
    public Potion(int x, int y) {
        super(x, y, "potion");
    }

    /**
     * Controls collision behaviour between player and potion.
     * @param p player
     */
    @Override
    public void collide(Player p) {
        p.addInventory(this);
        p.getState().pickupPotion();
        p.notifyObservers(this, "enemies", "run");
        potionTimeLimit(p, this);
    }

    /**
     * Implements a 10 second time limit for potions picked up by the player
     * @param p player
     * @param e potion
     */
    private void potionTimeLimit(Player p, Potion e) {
        int potionTimer = 10000; // 10 seconds
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                p.removeInventory(e);
                p.getState().exitLethalMode();
                p.notifyObservers(e, "enemies", "chase");
            }
        }, potionTimer);
    }
}