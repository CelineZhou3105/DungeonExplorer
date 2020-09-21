package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.*;

public class DoorTest {
    /**
     * Tests whether a Door is created correctly.
     */
    @Test
    public void doorCreation(){
        Door d = new Door(0,1,2);
        assertEquals(d.getX(), 1);
        assertEquals(d.getY(), 2);
        assertEquals(d.getId(), 0);
        assertEquals(d.getState(), "Closed");
    }

    /**
     * Tests open() function inside the Door class.
     */
    @Test
    public void doorOpen(){
        Door d = new Door(0,1,2);
        d.open();
        assertEquals(d.getState(), "Open");
    }

    /**
     * User Story #9.1 Doors - Player can walk through an open door
     */
    @Test
    public void playerOpenDoorInteraction() {
        Dungeon d = new Dungeon(3, 3);
        Player p = new Player(d, 0, 1);
        Door door = new Door(5, 0, 2);
        d.addEntity(p);
        d.addEntity(door);

        door.open();
        p.moveDown();
        // Assert that player was able to move into the door's coordinates
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 2);
    }

    /**
     * User Story #9.2 Doors - Player can't work through a closed door
     */
    @Test
    public void playerClosedDoorInteraction() {
        Dungeon d = new Dungeon(3, 3);
        Player p = new Player(d, 0, 1);
        Door door = new Door(5, 0, 2);
        d.addEntity(p);
        d.addEntity(door);
        p.moveDown();

        // Assert that player was not able to move through closed door
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 1);
    }

    /**
     * User Story #9.3 Doors - Player is able to unlock a door while holding the correct key
     */
    @Test
    public void playerUnlockDoorSuccess() {
        Dungeon d = new Dungeon(3, 3);
        Player p = new Player(d, 0, 1);
        Key k = new Key(1, 1, 1);
        Door door = new Door(1, 2, 1);
        d.addEntity(p);
        d.addEntity(k);
        d.addEntity(door);
        p.moveRight(); // pick up key
        assertEquals(p.checkIfInInventory(k), true); // check if key is in inventory

        p.moveRight(); // unlock the door
        assertEquals(door.getState(), "Open"); // check if door is open

        assertEquals(p.checkIfInInventory(k), false); // check if key has disappeared

        // player shouldn't have moved after unlocking the door
        assertEquals(p.getX(), 1);
        assertEquals(p.getY(), 1);
    }

    /**
     * User Story #9.4 Doors - Player cannot open a door with the wrong key
     */
    @Test
    public void playerUnlockDoorFail() {
        Dungeon d = new Dungeon(3, 3);
        Player p = new Player(d, 0, 1);
        Key k = new Key(1, 1, 1);
        Door door = new Door(2, 2, 1);
        d.addEntity(p);
        d.addEntity(k);
        d.addEntity(door);

        p.moveRight(); // pick up key
        assertEquals(p.checkIfInInventory(k), true); // check if key is in inventory

        p.moveRight(); // attempt to unlock the door with the wrong key
        assertEquals(door.getState(), "Closed"); // check if door remain closed

        assertEquals(p.checkIfInInventory(k), true); // key should still be in inventory
    }
}