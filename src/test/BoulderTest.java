package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.*;

public class BoulderTest {
    /**
     * Tests whether a boulder is created correctly.
     */
    @Test
    public void boulderCreation() {
        Boulder b = new Boulder(1,2);
        assertEquals(b.getX(), 1);
        assertEquals(b.getY(), 2);
    }

    /**
     * User Story #11.1 Boulders - Player can push boulder around level
     */
    @Test
    public void playerPushBoulder() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 1, 3);
        Boulder b = new Boulder(2, 3);
        d.addEntity(p);
        d.addEntity(b);
        p.moveRight();
        // Assert the player has moved
        assertEquals(p.getX(), 2);
        assertEquals(p.getY(), 3);

        // Assert the boulder has moved
        assertEquals(b.getX(), 3);
        assertEquals(b.getY(), 3);
    }

    /**
     * User Story #11.2 Boulders - Boulders cannot be pushed through walls or portals
     */
    @Test
    public void playerPushBoulderWallPortal() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 1, 3);
        Boulder b = new Boulder(2, 3);
        Wall w = new Wall(3, 3);
        d.addEntity(p);
        d.addEntity(b);
        d.addEntity(w);

        p.moveRight();
        // Pushed into a wall, nothing should not have moved
        assertEquals(p.getX(), 1);
        assertEquals(p.getY(), 3);
        assertEquals(b.getX(), 2);
        assertEquals(b.getY(), 3);

        
        d.removeEntity(w);
        Portal portal = new Portal(1, 3, 3);
        d.addEntity(portal);
        p.moveRight();
        // Push into a portal, nothing should not have moved
        assertEquals(p.getX(), 1);
        assertEquals(p.getY(), 3);
        assertEquals(b.getX(), 2);
        assertEquals(b.getY(), 3);
    }

    /**
     * User Story #11.3 Boulders - Player cannot push more than one boulder at a time
     */
    @Test
    public void playerPushMultipleBoulder() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 1, 3);
        Boulder b = new Boulder(2, 3);
        Boulder b2 = new Boulder(3,3);
        d.addEntity(p);
        d.addEntity(b);
        d.addEntity(b2);

        p.moveRight();
        // Assert the player has not moved
        assertEquals(p.getX(), 1);
        assertEquals(p.getY(), 3);
    }

}