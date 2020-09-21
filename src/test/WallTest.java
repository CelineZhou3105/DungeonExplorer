package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.*;

public class WallTest {
    /**
     * Tests if the wall is created correctly.
     */
    @Test
    public void wallCreation() {
        Wall w = new Wall(1, 2);
        assertEquals(w.getX(), 1);
        assertEquals(w.getY(), 2);
    }

    /**
     * User Story #6.1 Walls - Tests that player cannot pass through walls
     */
    @Test
    public void playerWallInteraction() {
        Dungeon d = new Dungeon(2, 2);
        Player p = new Player(d, 0, 1);
        Wall w = new Wall(0, 2);
        d.addEntity(p);
        d.addEntity(w);
        p.moveDown();
        
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 1);
    }

    /**
     * User Story #6.2 Walls - Walls will block enemies
     */
    @Test
    public void enemyWallInteraction() {
        Dungeon d = new Dungeon(5, 5);
        Enemy e = new Enemy(2, 2);
        Wall w = new Wall(1, 1);
        Wall w2 = new Wall(1, 2);
        Wall w3 = new Wall(1, 3);
        Wall w4 = new Wall(2, 1);
        Wall w5 = new Wall(2, 3);
        Wall w6 = new Wall(3, 1);
        Wall w7 = new Wall(3, 2);
        Wall w8 = new Wall(3, 3);
        Player p = new Player(d, 2, 4);
        
        d.addEntity(e);
        d.addEntity(w);
        d.addEntity(w2);
        d.addEntity(w3);
        d.addEntity(w4);
        d.addEntity(w5);
        d.addEntity(w6);
        d.addEntity(w7);
        d.addEntity(w8);

        e.basicMovement(p);
        //Assert the enemy cannot move 
        assertEquals(e.getX(), 2);
        assertEquals(e.getY(), 2);
    }

    /**
     * User Story #6.3 Walls - Walls will block boulders if they are pushed towards the wall by the player 
     */
    @Test
    public void wallBlockBoulder() {
        Dungeon d = new Dungeon(5,5);
        Player p = new Player(d, 1, 1);
        Boulder b = new Boulder(1,2);
        Wall w = new Wall(1,3);
        d.addEntity(p);
        d.addEntity(b);
        d.addEntity(w);

        p.moveDown();
        // Assert that player and boulder have not moved
        assertEquals(p.getX(), 1);
        assertEquals(p.getY(), 1);
        assertEquals(b.getX(), 1);
        assertEquals(b.getY(), 2);
    }
}