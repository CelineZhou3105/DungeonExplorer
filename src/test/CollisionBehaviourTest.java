package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.*;
import java.util.ArrayList;

public class CollisionBehaviourTest {
    
    @Test
    public void collisionDeniedTest() {
        // Wall - player should not move
        Dungeon d = new Dungeon(7, 7);
        // GoalComponent g = new GoalLeaf("enemies", 5);
        // d.setGoal(g);
        Player p = new Player(d, 1, 1);
        Wall w = new Wall(1,2);
        d.addEntity(p);
        d.addEntity(w);

        p.moveDown();
        assertEquals(p.getX(), 1);
        assertEquals(p.getY(), 1);

        // FloorSwitch - player moves but nothing happens
        FloorSwitch f = new FloorSwitch(2, 1);
        d.addEntity(f);
        p.moveRight();
        assertEquals(p.getX(), 2);
        assertEquals(p.getY(), 1);
    }

    @Test
    public void collisionEnemyTest() {
        Dungeon d = new Dungeon(7, 7);
        GoalComponent g = new GoalLeaf("exit", 0);
        d.setGoal(g);
        Player p = new Player(d, 2, 2);
        d.setPlayer(p);
        Enemy e = new Enemy(1, 2);
        d.addEntity(p);
        d.addEntity(e);
        
        // Collision with enemy in normalState - dies
        assert(p.getState() instanceof NormalState);
        p.moveLeft();
        ArrayList<Entity> list = d.checkOccupied(1, 2);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getName(), "enemies");

        // Collision with enemy in potionState - enemies should die
        Player p2 = new Player(d, 3, 3);
        p2.setState(new PotionState(p2));
        d.addEntity(p2);
        e.setX(3);
        e.setY(4);
        p2.moveDown();
        ArrayList<Entity> list2 = d.checkOccupied(3, 4);
        assertEquals(list2.size(), 1);
        assertEquals(list2.get(0).getName(), "player");
        
        // Collision with enemy in ArmedState
        p2.setState(new ArmedState(p2));
        Enemy e2 = new Enemy(3, 5);
        d.addEntity(e2);
        p2.moveDown();

        ArrayList<Entity> list3 = d.checkOccupied(3, 5);
        assertEquals(list3.size(), 1);
        assertEquals(list3.get(0).getName(), "player");
    }

    @Test
    public void collisionExitTest() {
        Dungeon d = new Dungeon(5, 5);
        GoalComponent g = new GoalLeaf("exit", 0);
        d.setGoal(g);
        Player p = new Player(d, 0, 1);
        Exit e = new Exit(0, 2);
        d.addEntity(p);
        d.addEntity(e);
        p.moveDown();

        // Closed exit, should not move the player
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 1);

        // the player's coordinates have changed
        e.open();
        p.moveDown();
        assertEquals(p.getX(), 0);
        assertEquals(p.getY(), 2);
    }

    @Test 
    public void collisionMatchTest() {
        Dungeon d = new Dungeon(8, 8);
        Player p = new Player(d, 2, 2);
        Key key = new Key(2, 2, 3);
        Door door = new Door(1, 2, 4);
        d.addEntity(p);
        d.addEntity(key);
        d.addEntity(door);

        // Closed Door with wrong key - shouldn't have moved
        p.moveDown();
        assertEquals(p.getX(), 2);
        assertEquals(p.getY(), 3);
        p.moveDown();
        assertEquals(p.getX(), 2);
        assertEquals(p.getY(), 3);

        // Close Door with right key - still shouldn't have moved but door is open
        p.removeInventory(key);
        Key key2 = new Key(1, 2, 2);
        d.addEntity(key2);
        p.moveUp();
        p.moveDown();
        p.moveDown();
        assertEquals(p.getX(), 2);
        assertEquals(p.getY(), 3);
        assertEquals(door.getState(), "Open");

        // Open door
        Door door2 = new Door(4,2,4);
        door2.open();
        p.moveDown();
        assertEquals(p.getX(), 2);
        assertEquals(p.getY(), 4);
    }

    @Test
    public void collisionMoveTest() {
        Dungeon d = new Dungeon(10, 10);
        GoalComponent g = new GoalLeaf("boulders", 3);
        d.setGoal(g);
        Player p = new Player(d, 2, 3);
        Boulder b = new Boulder(3, 3);
        d.addEntity(p);
        d.addEntity(b);

        // Moving boulder normally - nothing in square we want to move it to
        p.moveRight();
        assertEquals(p.getX(), 3);
        assertEquals(p.getY(), 3);
        assertEquals(b.getX(), 4);
        assertEquals(b.getY(), 3);

        // Moving onto a switch
        FloorSwitch f = new FloorSwitch(5, 3);
        d.addEntity(f);
        p.moveRight();
        assertEquals(f.getState(), "On");
        assertEquals(p.getX(), 4);
        assertEquals(p.getY(), 3);
        assertEquals(b.getX(), 5);
        assertEquals(b.getY(), 3);

        // Moving onto a switch with a boulder - player should not have moved
        FloorSwitch f2 = new FloorSwitch(6, 3);
        Boulder b2 = new Boulder(6, 3);
        d.addEntity(f2);
        d.addEntity(b2);
        p.moveRight();
        assertEquals(p.getX(), 4);
        assertEquals(p.getY(), 3);

        // Moving onto a place where something else is there
        d.removeEntity(f2);
        d.removeEntity(b2);
        Key k = new Key(1, 6, 3);
        d.addEntity(k);
        p.moveRight();
        assertEquals(p.getX(), 4);
        assertEquals(p.getY(), 3);
    }

    @Test
    public void collisionPickup() {
        Dungeon d = new Dungeon(10, 10);
        GoalComponent g = new GoalLeaf("treasure", 3);
        d.setGoal(g);
        Player p = new Player(d, 2, 2);
        Sword sword = new Sword(2, 3);
        Key key = new Key(1,2,4);
        Treasure treasure = new Treasure(2, 5);
        Potion potion = new Potion(2, 6);
        d.addEntity(p);
        d.addEntity(sword);
        d.addEntity(key);
        d.addEntity(treasure);
        d.addEntity(potion);

        // Pickup a sword
        p.moveDown();
        assertEquals(p.checkIfInInventory(sword), true);
        
        // Pickup a key
        p.moveDown();
        p.checkIfInInventory(key);
        assertEquals(p.checkIfInInventory(key), true);

        // Pickup a treasure
        p.moveDown();
        p.checkIfInInventory(treasure);
        assertEquals(p.checkIfInInventory(treasure), false);

        // Pickup a potion
        p.moveDown();
        p.checkIfInInventory(potion);
        assertEquals(p.checkIfInInventory(potion), true);
    }

    @Test
    public void collisionTeleport() {
        Dungeon d = new Dungeon(10, 10);
        Player p = new Player(d, 2, 2);
        Portal portal1 = new Portal(1, 2, 3);
        Portal portal2 = new Portal(1, 5, 5);

        d.addEntity(p);
        d.addEntity(portal1);
        d.addEntity(portal2);
        p.moveDown();
        // Assert that player is now at the matching portal
        assertEquals(p.getX(), 5);
        assertEquals(p.getY(), 5);
    }




}