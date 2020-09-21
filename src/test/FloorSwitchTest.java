package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.*;

public class FloorSwitchTest {
    /**
     * Tests whether a floor switch is created correctly.
     */
    @Test
    public void floorSwitchCreation(){
        FloorSwitch s = new FloorSwitch(1,2);
        assertEquals(s.getX(), 1);
        assertEquals(s.getY(), 2);
        assertEquals(s.getState(), "Off");
    }

    /**
     * Tests that a floor switch triggers and untriggers correctly.
     */
    @Test
    public void floorSwitchOnOff(){
        FloorSwitch s = new FloorSwitch(1,2);
        s.on();
        assertEquals(s.getState(), "On");
        s.off();
        assertEquals(s.getState(), "Off");
    }

    /**
     * User Story #12.1 + #12.2 FloorSwitch - Player will not be blocked by the floor switch, player cannot trigger it
     */
    @Test
    public void playerStepsOnSwitch() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 1, 3);
        FloorSwitch f = new FloorSwitch(2, 3);
        d.addEntity(p);
        d.addEntity(f);
        p.moveRight();

        // Assert that player has moved
        assertEquals(p.getX(), 2);
        assertEquals(p.getY(), 3);

        // Assert floor switch is still off
        assertEquals(f.getState(), "Off");
    }

    /**
     * User Story #12.3 + 12.4 FloorSwitch - Switches will trigger when a boulder is on top of it, will untrigger when a boulder is off it
     */
    @Test
    public void boulderOnSwitch() {
        Dungeon d = new Dungeon(10, 10);
        Player p = new Player(d, 1, 2);
        Boulder b = new Boulder(1, 3);
        FloorSwitch f = new FloorSwitch(1,4);

        d.addEntity(p);
        d.addEntity(b);
        d.addEntity(f);

        p.moveDown();
        // Check Switch is on
        assertEquals(f.getState(), "On");

        p.moveDown();
        // Check Switch is off
        assertEquals(f.getState(), "Off");
    }

}