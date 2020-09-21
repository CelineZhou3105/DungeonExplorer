package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.*;

public class PotionTest {
    /**
     * Tests whether a potion is created correctly.
     */
    @Test
    public void potionCreation(){
        Potion p = new Potion(1, 2);
        assertEquals(p.getX(), 1);
        assertEquals(p.getY(), 2);
    }

    /**
     * User Story #16.1 + 16.3 Invincibility Potion - Player is able to pick up potion, it is indicated to them they are invincible
     */
    @Test
    public void playerPickupPotion() {
        Dungeon d = new Dungeon(5, 5);
        Player p = new Player(d, 1, 3);
        Potion potion = new Potion(2, 3);
        d.addEntity(p);
        d.addEntity(potion);
        p.moveRight();

        // Assert player now has potion in inventory
        assertEquals(p.checkIfInInventory(potion), true);
        // Assert player is in potion state
        assert(p.getState() instanceof PotionState);
    }

    /**
     * User Story #16.2 Invincibility Potion - Player is made invincible for 10 seconds
     */
    @Test
    public void playerInvincible() {
        Dungeon d = new Dungeon(10, 10);
        Player p = new Player(d, 1, 1);
        Potion pot = new Potion(2, 1);
        Enemy e = new Enemy(5,5);
        Enemy e2 = new Enemy(8,8);
        d.addEntity(p);
        d.addEntity(pot);
        d.addEntity(e);
        p.addObserver(e, "enemies");
        p.addObserver(e2, "enemies");

        // Player picks up potion, enemies should run
        p.moveRight();
        assertEquals(e.getState(), "run");
        assertEquals(e2.getState(), "run");
        assert(p.getState() instanceof PotionState);

        try {
            Thread.sleep(11000);
        } catch (Exception exception) {
            System.out.println("Couldn't wait 11 seconds. Got interrupted.");
        }
        assertEquals(e.getState(), "chase");
        assertEquals(e2.getState(), "chase");
        assert(p.getState() instanceof NormalState);
    }
    /**
     * User Story #16.5 If player has sword in possession,
     * the potion will override the sword and the player will use
     * the potion to kill enemies rather than the sword
     */
    @Test
    public void playerPickUpPotionAndSword() {
        Dungeon d = new Dungeon(5, 1);
        Enemy e = new Enemy(0, 0);
        Player p = new Player(d, 2, 0);
        Potion potion = new Potion(4, 0);
        Sword s = new Sword(3, 0);

        d.addEntity(e);
        d.addEntity(p);
        d.addEntity(potion);
        d.addEntity(s);
        p.addObserver(e, "enemies");

        p.moveRight();
        p.moveRight();
        p.moveLeft();
        p.moveLeft();
        p.moveLeft();
        p.moveLeft();
        assertEquals(s.getHits(), 5);
    }
}