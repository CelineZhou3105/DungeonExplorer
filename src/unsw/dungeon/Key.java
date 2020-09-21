package unsw.dungeon;

public class Key extends Entity {
    private int id;
    public Key(int id, int x, int y) {
        super(x, y, "key");
        this.id = id;  
    }

    public int getId() {
        return id;
    }

    /**
     * Controls collision behaviour between player and key
     * @param p player
     */
    @Override
    public void collide(Player p) {
        p.addInventory(this);
    }

}
