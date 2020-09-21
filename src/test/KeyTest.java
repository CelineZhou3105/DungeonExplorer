package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import unsw.dungeon.*;

public class KeyTest {
    /**
     * Tests whether a Key is created correctly.
     */
    @Test
    public void keyCreation(){
        Key k = new Key(0,1,2);
        assertEquals(k.getX(), 1);
        assertEquals(k.getY(), 2);
        assertEquals(k.getId(), 0);
    }

    /**
     * User Story #10.1 Key - player can pickup key by moving onto it
     */
    @Test
    public void playerPickupKey() {
        Dungeon d = new Dungeon(3, 3);
        Player p = new Player(d, 0, 1);
        Key key = new Key(5, 0, 2);
        d.addEntity(p);
        d.addEntity(key);
        
        p.moveDown();
        // Check it's in the inventory
        assertEquals(p.getKey(), key);
        // Check that key has been removed from dungeon
        ArrayList<Entity> list = d.checkOccupied(0, 2);
        assertEquals(list.size(), 1); // only the player
        assertEquals(list.get(0).getName(),"player");
    }

    /**
     * User Story #10.3 Key - player can only pickup one key at a time
     */
    @Test
    public void playerPickupOneKey() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 0, 1);
        Key key = new Key(5, 0, 2);
        Key key2 = new Key(6, 0, 3);
        d.addEntity(p);
        d.addEntity(key);
        d.addEntity(key2);

        p.moveDown();
        // Check first key in the inventory
        assertEquals(p.getKey(), key);

        p.moveDown();
        // Check that second key is still in the dungeon
        ArrayList<Entity> list = d.checkOccupied(0,3);
        Entity k = list.get(0);
        if (k instanceof Key) {
            assertEquals((Key)k, key2);
        }
    }
}