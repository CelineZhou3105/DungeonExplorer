package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import unsw.dungeon.*;

public class StateTest {

    /**
     * Tests the pickupPotion() function inside the NormalState class
     */
    @Test
    public void normalStatePickupPotion() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 2, 2);
        d.addEntity(p);
        d.setPlayer(p);

        // Should change player to potionState
        p.getState().pickupPotion();
        assert(p.getState() instanceof PotionState);
    }

    /**
     * Tests the pickupSword() function inside the NormalState class
     */
    @Test 
    public void normalStatePickupSword() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 2, 2);
        d.addEntity(p);
        d.setPlayer(p);

        // Should change player to potionState
        p.getState().pickupSword();
        assert(p.getState() instanceof ArmedState);
    }

    /**
     * Tests the exitLethalMode() function inside the NormalState class
     */
    @Test
    public void normalStateExitLethalMode() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 2, 2);
        d.addEntity(p);
        d.setPlayer(p);

        // Should change player to potionState
        p.getState().exitLethalMode();
        assert(p.getState() instanceof NormalState);
    }

    /**
     * Tests the pickupPotion() function inside the PotionState class
     */
    @Test 
    public void potionStatePickupPotion() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 2, 2);
        d.addEntity(p);
        d.setPlayer(p);
        p.setState(new PotionState(p));

        p.getState().pickupPotion();
        assert(p.getState() instanceof PotionState);
    }

    /**
     * Tests the pickupSword() function inside the PotionState class
     */
    @Test 
    public void potionStatePickupSword() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 2, 2);
        d.addEntity(p);
        d.setPlayer(p);
        p.setState(new PotionState(p));

        p.getState().pickupSword();
        assert(p.getState() instanceof PotionState);
    }

    /**
     * Tests the exitLethalMode() function inside the PotionState class
     */
    @Test 
    public void potionStateExitLethalMode() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 2, 2);
        d.addEntity(p);
        d.setPlayer(p);
        p.setState(new PotionState(p));

        p.getState().exitLethalMode();
        assert(p.getState() instanceof NormalState);
    }

    /**
     * Tests the special case when player's potion wears off but player still has sword
     */
    public void potionStateToArmedState() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 2, 2);
        Sword s = new Sword(2, 3);
        d.addEntity(p);
        d.setPlayer(p);
        d.addEntity(s);
        p.addInventory(s);
        p.setState(new PotionState(p));
        
        p.getState().exitLethalMode();
        assert(p.getState() instanceof ArmedState);
    }

    /**
     * Tests the pickupPotion() function inside the ArmedState class
     */
    @Test
    public void armedStatePickupPotion() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 2, 2);
        d.addEntity(p);
        d.setPlayer(p);
        p.setState(new ArmedState(p));

        p.getState().pickupPotion();
        assert(p.getState() instanceof PotionState);
    }

    /**
     * Tests the pickupSword() function inside the ArmedState class
     */
    @Test
    public void armedStatePickupSword() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 2, 2);
        d.addEntity(p);
        d.setPlayer(p);
        p.setState(new ArmedState(p));

        p.getState().pickupSword();
        assert(p.getState() instanceof ArmedState);
    }

    /**
     * Tests the exitLethalMode() function inside the ArmedState class
     */
    @Test
    public void armedStateExitLethalMode() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 2, 2);
        d.addEntity(p);
        d.setPlayer(p);
        p.setState(new ArmedState(p));

        p.getState().exitLethalMode();
        assert(p.getState() instanceof NormalState);

    }
}