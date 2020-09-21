package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.*;

public class SwordTest {
    /**
     * Tests whether a sword has been created correctly.
     */
    @Test
    public void swordCreation(){
        Sword s = new Sword(1, 2);
        assertEquals(s.getX(), 1);
        assertEquals(s.getY(), 2);
        assertEquals(s.getHits(), 5);
    }

    /**
     * Tests the minusHit() function inside the Sword class
     */
    @Test
    public void swordMinusHit(){
        Sword s = new Sword(1, 2);
        s.minusHit();
        assertEquals(s.getHits(), 4);
    }

    /**
     * Tests the checkIfBroken() function inside the Sword class
     */
    @Test
    public void swordCheckIfBroken(){
        Sword s = new Sword(1, 2);
        assertEquals(s.checkIfBroken(), false);
        
        s.setHits(0);
        assertEquals(s.checkIfBroken(), true);
    }

    /**
     * User Story #37.1 + 37.2 + 37.4 Swords - Player can pickup, sword appears in inventory, player changes state
     */
    @Test
    public void playerPickupSword() {
        Dungeon d = new Dungeon(5,5);
        Player p = new Player(d, 1, 3);
        Sword s = new Sword(1, 2);
        d.addEntity(p);
        d.addEntity(s);
        p.moveUp();

        // Assert player now has sword in inventory
        assertEquals(p.checkIfInInventory(s), true);

        // Assert player is in ArmedState
        assert(p.getState() instanceof ArmedState);
    }

    /**
     * User Story #37.3 Swords - Player can only pick up one sword at a time
     */
    @Test
    public void playerPickupSwordOnce() {
        Dungeon d = new Dungeon(5,5);
        Player p = new Player(d, 1, 3);
        Sword s = new Sword(1, 2);
        Sword s2 = new Sword(1, 1);
        d.addEntity(p);
        d.addEntity(s);
        d.addEntity(s2);
        p.moveUp();
        // Assert player now has sword in inventory
        assertEquals(p.checkIfInInventory(s), true);

        p.moveUp();

        // Assert player has moved 
        assertEquals(p.getX(), 1);
        assertEquals(p.getY(), 1);

        // Assert the second sword is still there
        Entity ent = null;
        for (Entity sword: p.checkIfCollision(1, 1)) {
            if (sword.getName().equals("sword")) {
                ent = sword;
            }
        }
        assertEquals(ent, s2);
    }

}