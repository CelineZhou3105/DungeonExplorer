package unsw.dungeon;

public class Treasure extends Entity {
    public Treasure(int x, int y) {
        super(x, y, "treasure");
    }

    /**
     * Controls collision between player and treasure
     * @param p player 
     */
    @Override
    public void collide(Player p) {
        p.playSoundEffect("sounds/coinPickUp.mp3");
        p.addTreasure(this);
        p.notifyObservers(this, "goals", "decrement");
        p.notifyObservers(this, "dungeon", "checkIfLevelComplete");
    }
}