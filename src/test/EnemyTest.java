package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import unsw.dungeon.*;

public class EnemyTest {
    /**
     * Tests whether an enemy is created correctly.
     */
    @Test 
    public void enemyCreation() {
        Enemy e = new Enemy(1, 2);
        assertEquals(e.getX(), 1);
        assertEquals(e.getY(), 2);
    }

    /**
     * User Story #14.1 Player is killed upon collision with enemy when unarmed
     */
    @Test
    public void playerDies() {
        Dungeon d = new Dungeon(5, 5);
        Enemy e = new Enemy(1, 2);
        Player p = new Player(d, 1, 3);
        d.addEntity(e);
        d.addEntity(p);
        p.addObserver(e, "enemies");

        // Assert player is in NormalState
        assert(p.getState() instanceof NormalState);
        e.basicMovement(p);

        // Assert player is dead - removed from dungeon
        ArrayList<Entity> array = d.checkOccupied(1, 3);
        assertEquals(array.size(), 1);
        assertEquals(array.get(0).getName(), "enemies");

    }

    /**
     * User Story #14.2 Enemy moves towards player
     */
    @Test
    public void basicMovementTest() {
        Dungeon d = new Dungeon(3, 3);
        Enemy e = new Enemy(1, 0);
        Player p = new Player(d,1, 2);
        d.addEntity(e);
        d.addEntity(p);
        p.addObserver(e, "enemies");

        p.moveRight(); // enemy should move down
        assertEquals(e.getX(), 1);
        assertEquals(e.getY(), 1);

        p.setX(1);
        p.setY(0);
        e.setX(1);
        e.setY(2);
        p.moveLeft(); // enemy should move up
        assertEquals(e.getX(), 1);
        assertEquals(e.getY(), 1);

        p.setX(1);
        p.setY(1);
        e.setX(2);
        e.setY(1);
        p.moveLeft(); // enemy should move left
        assertEquals(e.getX(), 1);
        assertEquals(e.getY(), 1);

        p.setX(1);
        p.setY(1);
        e.setX(0);
        e.setY(1);
        p.moveRight(); // enemy should move right
        assertEquals(e.getX(), 1);
        assertEquals(e.getY(), 1);

        p.setX(2);
        p.setY(1);
        e.setX(0);
        e.setY(1);
        Wall w = new Wall(1, 1);
        d.addEntity(w);
        p.moveLeft(); // enemy shouldn't have moved
        assertEquals(e.getX(), 0);
        assertEquals(e.getY(), 1);

        p.setX(0);
        p.setY(1);
        e.setX(2);
        e.setY(1);
        p.moveRight(); // enemy shouldn't have moved
        assertEquals(e.getX(), 2);
        assertEquals(e.getY(), 1);

        p.setX(1);
        p.setY(2);
        e.setX(1);
        e.setY(0);
        p.moveRight(); // enemy should move right
        assertEquals(e.getX(), 2);
        assertEquals(e.getY(), 0);

        p.setX(1);
        p.setY(2);
        e.setX(1);
        e.setY(0);
        p.moveLeft(); // enemy should move left
        assertEquals(e.getX(), 0);
        assertEquals(e.getY(), 0);

        p.setX(1);
        p.setY(2);
        e.setX(1);
        e.setY(0);
        p.moveUp(); // enemy should move right
        assertEquals(e.getX(), 2);
        assertEquals(e.getY(), 0);

        p.setX(1);
        p.setY(2);
        e.setX(1);
        e.setY(0);
        Wall w2 = new Wall(2, 0);
        d.addEntity(w2);
        p.moveUp(); // enemy should move left
        assertEquals(e.getX(), 0);
        assertEquals(e.getY(), 0);
    }

    /**
     * User Story #14.3 Enemy moves away when player has consumed potion (while potion is active)
     */
    @Test
    public void runAwayTest() {
        Dungeon d = new Dungeon(3, 3);
        Enemy e = new Enemy(0, 1);
        Player p = new Player(d,1, 2);
        Potion potion = new Potion(2, 0);
        d.addEntity(e);
        d.addEntity(p);
        d.addEntity(potion);
        p.addObserver(e, "enemies");
        e.update(potion, "run");

        p.moveRight(); // enemy should move up
        assertEquals(e.getX(), 0);
        assertEquals(e.getY(), 0);

        p.setX(1);
        p.setY(0);
        e.setX(0);
        e.setY(1);
        p.moveLeft(); // enemy should move down
        assertEquals(e.getX(), 0);
        assertEquals(e.getY(), 2);

        p.setX(1);
        p.setY(2);
        e.setX(1);
        e.setY(1);
        Wall w = new Wall(1, 0);
        d.addEntity(w);
        p.moveRight(); // enemy should move left
        assertEquals(e.getX(), 0);
        assertEquals(e.getY(), 1);

        p.setX(1);
        p.setY(2);
        e.setX(1);
        e.setY(1);
        p.moveLeft(); // enemy should move right
        assertEquals(e.getX(), 2);
        assertEquals(e.getY(), 1);

        p.setX(1);
        p.setY(2);
        e.setX(0);
        e.setY(1);
        Wall w2 = new Wall(0, 0);
        d.addEntity(w2);
        p.moveRight(); // enemy should've stayed in place
        assertEquals(e.getX(), 0);
        assertEquals(e.getY(), 1);
    }

    /**
     * User Story #14.4 Enemy cannot walk through boulders or push them
     */
    @Test
    public void enemyAgainstBoulder() {
        Dungeon d = new Dungeon(4, 1);
        Enemy e = new Enemy(0, 0);
        Player p = new Player(d, 2, 0);
        Boulder b = new Boulder(1, 0);
        d.addEntity(e);
        d.addEntity(p);
        d.addEntity(b);
        p.addObserver(e, "enemies");

        p.moveRight(); // enemy should stay in place, boulder should stay in place
        assertEquals(e.getX(), 0);
        assertEquals(e.getY(), 0);
        assertEquals(b.getX(), 1);
        assertEquals(b.getY(), 0);
    }

    /**
     * User Story #14.5 Enemy cannot walk through portals
     */
    @Test
    public void enemyAgainstPortal() {
        Dungeon d = new Dungeon(4, 1);
        Enemy e = new Enemy(0, 0);
        Player p = new Player(d, 2, 0);
        Portal portal = new Portal(1, 1, 0);
        d.addEntity(e);
        d.addEntity(p);
        d.addEntity(portal);
        p.addObserver(e, "enemies");

        p.moveRight(); // enemy should stay in place
        assertEquals(e.getX(), 0);
        assertEquals(e.getY(), 0);
    }

    /**
     * User Story #14.6 Enemies will be killed by the player
     * upon collision if the player is currently holding a sword
     */
    @Test
    public void enemyDieBySword() {
        Dungeon d = new Dungeon(4, 1);
        Enemy e = new Enemy(0, 0);
        Player p = new Player(d, 2, 0);
        Sword s = new Sword(3, 0);
        d.addEntity(e);
        d.addEntity(p);
        d.addEntity(s);
        p.addObserver(e, "enemies");

        p.moveRight();
        e.moveOnX(p, 3, 0);
        ArrayList<Entity> list = d.checkOccupied(3, 0);
        int enemiesLeft = 0;
        for (Entity entity: list) {
            if (entity.getName().equals("enemies")) {
                enemiesLeft++;
            }
        }
        assertEquals(enemiesLeft, 0);
    }

    /**
     * User Story #14.7 Enemies will be killed by the player
     * upon collision if the potion the player drank is currently active
     */
    @Test
    public void enemyDieByPotion() {
        Dungeon d = new Dungeon(4, 1);
        Enemy e = new Enemy(0, 0);
        Player p = new Player(d, 2, 0);
        Potion potion = new Potion(3, 0);
        d.addEntity(e);
        d.addEntity(p);
        d.addEntity(potion);
        p.addObserver(e, "enemies");

        p.moveRight();
        p.moveLeft();
        p.moveLeft();
        p.moveLeft();
        ArrayList<Entity> list = d.checkOccupied(0, 0);
        int enemiesLeft = 0;
        for (Entity entity: list) {
            if (entity.getName().equals("enemies")) {
                enemiesLeft++;
            }
        }
        assertEquals(enemiesLeft, 0);
    }
}