package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.*;

public class ExitTest {

    /**
     * Tests if the Exit is created correctly.
     */
    @Test
    public void ExitCreation() {
        Exit e = new Exit(1, 2);
        assertEquals(e.getX(), 1);
        assertEquals(e.getY(), 2);
        assertEquals(e.getState(), "Closed");
    }

    /**
     * Tests the open() function inside the Exit class
     */
    @Test
    public void openExit() {
        Exit e = new Exit(1, 2);
        e.open();
        assertEquals(e.getState(), "Open");
    }

    /**
     * User Story #7.1 + 7.3 Exit - Player can walk through open exits & finish level
     */
    @Test
    public void playerOpenExitInteraction() {
        Dungeon d = new Dungeon(5, 5);
        GoalComponent g = new GoalLeaf("exit", 0);
        d.setGoal(g);
        Player p = new Player(d, 0, 1);
        Exit e = new Exit(0, 2);
        d.addEntity(p);
        d.addEntity(e);
        //e.open();
        p.moveDown();
        
        // the player's coordinates haven't changed
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 1);

        p.moveDown();
        // Player's coordinates should have changed
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 2);
    }

    /**
     * User Story #7.3 Exit - Player cannot walk a closed exit (i.e. goals aren't finished)
     */
    @Test
    public void playerClosedExitInteraction() {
        Dungeon d = new Dungeon(4, 4);
        GoalComponent g = new GoalLeaf("enemies", 5);
        d.setGoal(g);
        Player p = new Player(d, 0, 1);
        Exit e = new Exit(0, 2);
        d.addEntity(p);
        d.addEntity(e);
        p.moveDown();

        // the player's coordinates don't change
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 1);
    }
}