package unsw.dungeon;

public class Sword extends Entity {
    private int hits;
    public Sword(int x, int y) {
        super(x, y, "sword");
        hits = 5;
    }

    /**
     * Checks if the sword is broken 
     * @return true if sword should break, false if sword isn't broken
     */
    public boolean checkIfBroken() {
        if (hits == 0) {
            return true;
        }
        return false; 
    }

    /**
     * Adds another hit to the sword
     */
    public void minusHit() {
        hits -= 1;
    }

    /**
     * Returns the number of hits the sword has endured
     * @return number of hits on the sword
     */
    public int getHits() {
        return hits;
    }

    /**
     * Sets the number of hits the sword has endured
     * @param x the number of hits
     */
    public void setHits(int x) {
        hits = x;
    }

    /**
     * Controls the collision behaviour between player and sword
     * @param p player
     */
    @Override
    public void collide(Player p) {
        p.addInventory(this);
        p.getState().pickupSword();
    }
}