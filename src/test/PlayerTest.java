package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;


import unsw.dungeon.*;

public class PlayerTest {
    /**
     * Test if the player is created correctly.
     */
    @Test
    public void playerCreation() {
        Dungeon d = new Dungeon(2, 2);
        Player p = new Player(d, 0, 1);
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 1);
    }

    /**
     * Tests the moveUp(), moveDown(), moveLeft(), moveRight() functions inside the Player class.
     */
    @Test 
    public void playerMove() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 2, 1);
        d.addEntity(p);

        p.moveRight();
        // Assert player moved right
        assertEquals(p.getX(), 3);
        assertEquals(p.getY(), 1);

        p.moveDown();
        // Assert player moved down
        assertEquals(p.getX(), 3);
        assertEquals(p.getY(), 2);

        p.moveLeft();
        // Assert player moved left
        assertEquals(p.getX(), 2);
        assertEquals(p.getY(), 2);

        p.moveUp();
        // Assert player moved up 
        assertEquals(p.getX(), 2);
        assertEquals(p.getY(), 1);
    }

    /**
     * Tests the addInventory() function 
     */
    @Test
    public void playerAddInventory() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 0, 0);
        Key k1 = new Key(1, 0, 1);
        d.addEntity(p);
        d.addEntity(k1);
        // Check that the key is inside the inventory
        p.addInventory(k1);
        assertEquals(p.checkIfInInventory(k1), true);

        // Check that only one of the keys can be picked up (i.e second key is still in dungeon)
        Key k2 = new Key(2, 0, 2);
        d.addEntity(k2);
        p.addInventory(k2);
        assertEquals(d.checkOccupied(0, 2).size(), 1);
    }

    /**
     * Tests the removeInventory() function inside the Player class
     */
    @Test
    public void playerRemoveInventory() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 0, 0);
        Key k1 = new Key(1, 0, 1);
        d.addEntity(p);
        d.addEntity(k1);

        // Remove from empty inventory - should do nothing
        p.removeInventory(k1);
        assertEquals(d.checkOccupied(0, 1).size(), 1);

        // Remove item that player has not picked up - should do nothing
        Potion potion = new Potion(0, 2);
        d.addEntity(potion);
        p.removeInventory(potion);

        // Remove the item from inventory
        p.addInventory(k1);
        p.removeInventory(k1);
        assertEquals(p.checkIfInInventory(k1), false);
    }

    /**
     * Tests the getKey() function inside the Player class
     */
    @Test
    public void playerGetKey() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 0, 0);
        d.addEntity(p);

        // Assert getKey returns null when no key in player's inventory
        assertEquals(p.getKey(), null);

        // Assert getKey returns the correct key 
        Key k = new Key(1, 0, 1);
        d.addEntity(k);
        p.addInventory(k);
        assertEquals(p.getKey(), k);
    }

    /**
     * Tests the getMatchingPortal() function inside the Player class
     */
    @Test 
    public void playerGetMatchingPortal() {
        Dungeon d = new Dungeon(5,5);
        Player p = new Player(d, 2, 2);
        Portal p1 = new Portal(1, 2, 3);
        Portal p2 = new Portal(1, 4, 4);
        d.addEntity(p);
        d.addEntity(p1);
        d.addEntity(p2);

        assertEquals(p.getMatchingPortal(p1), p2);
        assertEquals(p.getMatchingPortal(p2), p1);
    }

    /**
     * Tests the checkIfCollision() function inside the Player class
     */
    @Test
    public void playerCheckIfCollision() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 2, 2);
        d.addEntity(p);

        // When nothing is there, checkIfCollision should return an empty list
        ArrayList<Entity> list = p.checkIfCollision(2, 3);
        assertEquals(list.size(), 0);

        // When something is there, checkIfCollision should return the list containing the correct object
        Treasure t = new Treasure(3,3);
        d.addEntity(t);
        ArrayList<Entity> list2 = p.checkIfCollision(3, 3);
        assertEquals(list2.size(), 1);
        assertEquals(list2.get(0), t);
    }

    /**
     * Checks the addObserver() + removeObserver() function inside the Player class
     */
    @Test 
    public void playerAddObserver() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 2, 2);
        Enemy e = new Enemy(2, 3);
        d.addEntity(p);
        d.addEntity(e);

        // Check the enemy is actually in the list
        p.addObserver(e, "enemies");
        assertEquals(p.isObserver(e, "enemies"), true);

        // Checks a goal is actually in the list
        GoalLeaf g = new GoalLeaf("enemies", 5);
        p.addObserver(g, "goals");
        assertEquals(p.isObserver(g, "goals"), true);

        // Checks an enemy is successfully removed
        p.removeObserver(e, "enemies");
        assertEquals(p.isObserver(e, "enemies"), false);

        // Checks a goal has been removed
        p.removeObserver(g, "goals");
        assertEquals(p.isObserver(g, "goals"), false);
    }

    /**
     * Checks the isLethal() function inside the Player class
     */
    @Test 
    public void playerIsLethal() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 0, 1);
        d.addEntity(p);

        // Test for the NormalState --> should return false
        assertEquals(p.isLethal(), false);

        // Test for the ArmedState --> should return true
        ArmedState armed = new ArmedState(p);
        p.setState(armed);
        assertEquals(p.isLethal(), true);

        // Test for the PotionState --> should return true
        PotionState potion = new PotionState(p);
        p.setState(potion);
        assertEquals(p.isLethal(), true);
        
    }

    /**
     * Tests the killEnemy() function inside the Player class
     */
    @Test 
    public void playerKillEnemy() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 0, 1);
        Enemy e = new Enemy(0, 2);
        Sword s = new Sword(0, 3);
        
        d.addEntity(p);
        d.addEntity(e);
        d.addEntity(s);


        p.addInventory(s);
        p.killEnemy(e);

        // Checks that we've removed the enemy from the dungeon
        assertEquals(d.checkOccupied(0, 2).size(), 0);

        // Checks that the enemy is no longer an observer
        assertEquals(p.isObserver(e, "enemies"), false);

        // Check that our sword has taken a hit
        assertEquals(s.getHits(),4);
    }

    /**
     * Test the die() function inside the Player class
     */
    @Test 
    public void playerDie() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 0, 1);
        d.addEntity(p);

        p.die();
        // Assert player is no longer in dungeon
        assertEquals(d.checkOccupied(0, 1).size(), 0);
    }

    /**
     * Tests the finishLevel() function inside the Player class
     */
    @Test 
    public void playerFinishLevel() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 0, 1);
        d.addEntity(p);

        p.finishLevel();
        // Should print out the correct statement 

    }



}