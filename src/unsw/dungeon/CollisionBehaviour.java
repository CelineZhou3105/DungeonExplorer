package unsw.dungeon;

public interface CollisionBehaviour {
    /**
     * Function for when player collides with an entity
     * @param player Player of the dungeon
     */
    public void collide(Player player);
}