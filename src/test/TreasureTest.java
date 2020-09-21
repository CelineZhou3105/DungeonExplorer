package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.*;

public class TreasureTest {
    /**
     * Tests whether Treasure is created correctly. 
     */
    @Test
    public void treasureCreation(){
        Treasure t = new Treasure(1,2);
        assertEquals(t.getX(), 1);
        assertEquals(t.getY(), 2);
    }

    /**
     * User Story #8.1 + 8.2 Treasure - Player can collect treasure, treasure disappears from dungeon
     */
    @Test
    public void playerCollectsTreasure() {
        Dungeon d = new Dungeon(3, 3);
        Player p = new Player(d, 0, 1);
        Treasure t1 = new Treasure(1, 1);
        Treasure t2 = new Treasure(1, 2);
        d.addEntity(p);
        d.addEntity(t1);
        d.addEntity(t2);

        // Checks that the treasure collected incremented
        p.moveRight();
        assertEquals(p.getTreasureCollected(), 1);
        p.moveDown();
        assertEquals(p.getTreasureCollected(), 2);

        // Check treasure disappears from dungeon
        assertEquals(d.checkOccupied(1, 1).size(), 0);
        assertEquals(d.checkOccupied(1, 2).size(), 1); // only player is there

        // Check treasure is not in player's inventory
        assertEquals(p.checkIfInInventory(t1), false);
        assertEquals(p.checkIfInInventory(t2), false);
    }
}