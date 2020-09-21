package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


import unsw.dungeon.*;

public class PortalTest {
    /**
     * Tests whether a portal is created correctly.
     */
    @Test
    public void portalCreation() {
        Portal p = new Portal(0, 1,2);
        assertEquals(p.getX(), 1);
        assertEquals(p.getY(), 2);
        assertEquals(p.getId(), 0);
    }

    /**
     * User Story #13 Portal - Player gets teleported to the corresponding portal
     */
    @Test
    public void playerPortalInteraction() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 1, 3);
        Portal portal1 = new Portal(1, 2, 3);
        Portal portal2 = new Portal(1, 4, 2);
        d.addEntity(p);
        d.addEntity(portal1);
        d.addEntity(portal2);
        p.moveRight();
        
        // Assert player has been teleported
        assertEquals(p.getX(), 4);
        assertEquals(p.getY(), 2);
    }

}

