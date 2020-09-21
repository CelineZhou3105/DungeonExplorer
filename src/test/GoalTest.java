package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import unsw.dungeon.*;

public class GoalTest {

    /**
     * User Story #23 Goal(Exit)
     */
    @Test
    public void goalExitOnly() {
        Dungeon d = new Dungeon(3, 3);
        Player p = new Player(d, 0, 1);
        Exit e = new Exit(2, 1);
        GoalLeaf goal = new GoalLeaf("exit", 0);
        d.addEntity(p);
        d.addEntity(e);
        d.setGoal(goal);
        d.setPlayer(p);

        assertEquals(d.isLevelComplete(), true); // since its exit only

        assertEquals(e.getState(), "Closed"); // exit should be closed
        p.moveRight();
        assertEquals(e.getState(), "Closed"); // exit should be closed
        p.moveRight();
        assertEquals(e.getState(), "Open"); // exit should be open
    }

    /**
     * User Story #24 Goal(Enemy)
     */
    @Test
    public void goalEnemyOnly() {
        Dungeon d = new Dungeon(3, 3);
        Player p = new Player(d, 0, 0);
        Enemy e1 = new Enemy(0, 2);
        Enemy e2 = new Enemy(1, 2);
        Sword s = new Sword(1, 0);
        GoalLeaf goal = new GoalLeaf("enemies", 2);
        d.addEntity(p);
        d.addEntity(e1);
        d.addEntity(e2);
        d.addEntity(s);
        d.setGoal(goal);
        d.setPlayer(p);
        p.addObserver(e1, "enemies");
        p.addObserver(e2, "enemies");
        p.addObserver(goal, "goals");


        assertEquals(d.isLevelComplete(), false);
        p.moveRight();
        assertEquals(d.isLevelComplete(), false);
        p.moveDown();
        assertEquals(d.isLevelComplete(), false);
        p.moveLeft();
        assertEquals(d.isLevelComplete(), true);
    }

    /**
     * User Story #25 Goal(Boulders and Switches)
     */
    @Test
    public void goalBouldersSwitchesOnly() {
        Dungeon d = new Dungeon(3, 3);
        Player p = new Player(d, 0, 0);
        Boulder b1 = new Boulder(1, 0);
        Boulder b2 = new Boulder(1, 1);
        Boulder b3 = new Boulder(1, 2);
        FloorSwitch f1 = new FloorSwitch(2, 0);
        FloorSwitch f2 = new FloorSwitch(2, 1);
        FloorSwitch f3 = new FloorSwitch(2, 2);
        GoalLeaf goal = new GoalLeaf("boulders", 3);
        d.addEntity(p);
        d.addEntity(b1);
        d.addEntity(b2);
        d.addEntity(b3);
        d.addEntity(f1);
        d.addEntity(f2);
        d.addEntity(f3);
        d.setPlayer(p);
        d.setGoal(goal);
        p.addObserver(goal, "goals");

        assertEquals(d.isLevelComplete(), false);
        p.moveRight();
        assertEquals(d.isLevelComplete(), false);
        p.moveLeft();
        p.moveDown();
        p.moveRight();
        assertEquals(d.isLevelComplete(), false);
        p.moveLeft();
        p.moveDown();
        p.moveRight();
        assertEquals(d.isLevelComplete(), true);
    }

    /**
     * User Story #26 Goal(Collect All Treasure)
     */
    @Test
    public void goalTreasureOnly() {
        Dungeon d = new Dungeon(3, 3);
        Player p = new Player(d, 0, 0);
        Treasure t1 = new Treasure(1, 0);
        Treasure t2 = new Treasure(1, 1);
        GoalLeaf goal = new GoalLeaf("treasure", 2);
        d.addEntity(p);
        d.addEntity(t1);
        d.addEntity(t2);
        d.setPlayer(p);
        d.setGoal(goal);
        p.addObserver(goal, "goals");

        assertEquals(d.isLevelComplete(), false);
        p.moveRight();
        assertEquals(d.isLevelComplete(), false);
        p.moveDown();
        assertEquals(d.isLevelComplete(), true);
    }

    /**
     * User Story #27 Sub-goals(AND)
     */
    @Test
    public void subGoalsAND() {
        Dungeon d = new Dungeon(3, 3);
        Player p = new Player(d, 0, 0);
        Treasure t1 = new Treasure(1, 1);
        Treasure t2 = new Treasure(1, 2);
        Exit e = new Exit(2, 0);
        GoalLeaf treasure = new GoalLeaf("treasure", 2);
        GoalLeaf exit = new GoalLeaf("exit", 0);
        GoalComposite goals = new GoalComposite("AND");
        goals.addGoals(treasure);
        goals.addGoals(exit);
        d.addEntity(p);
        d.addEntity(t1);
        d.addEntity(t2);
        d.addEntity(e);
        d.setPlayer(p);
        d.setGoal(goals);
        p.addObserver(treasure, "goals");

        assertEquals(d.isLevelComplete(), false);
        p.moveRight();
        p.moveRight();
        assertEquals(d.isLevelComplete(), false);
        p.moveDown();
        p.moveDown();
        assertEquals(d.isLevelComplete(), true);
        assertEquals(e.getState(), "Closed");
        p.moveUp();
        p.moveUp();
        p.moveRight();
        assertEquals(e.getState(), "Open");
        assertEquals(d.isLevelComplete(), true);
    }

    /**
     * User Story #28 Sub-goals(OR)
     */
    @Test
    public void subGoalsOR() {
        Dungeon d = new Dungeon(3, 3);
        Player p = new Player(d, 0, 0);
        Treasure t = new Treasure(0, 1);
        Sword s = new Sword(1, 0);
        Enemy e = new Enemy(2, 2);
        GoalLeaf treasure = new GoalLeaf("treasure", 1);
        GoalLeaf enemy = new GoalLeaf("enemies", 1);
        GoalComposite goals = new GoalComposite("OR");
        goals.addGoals(treasure);
        goals.addGoals(enemy);
        d.addEntity(p);
        d.addEntity(t);
        d.addEntity(s);
        d.addEntity(e);
        d.setPlayer(p);
        d.setGoal(goals);
        p.addObserver(treasure, "goals");
        p.addObserver(enemy, "goals");
        p.addObserver(e, "enemies");

        assertEquals(d.isLevelComplete(), false);
        p.moveDown();
        assertEquals(d.isLevelComplete(), true);
    }

    /**
     * Testing a complex goal structure with both AND and OR
     */
    @Test
    public void subGoalsANDOR() {
        Dungeon d = new Dungeon(3, 3);
        Player p = new Player(d, 0, 0);
        Treasure t = new Treasure(0, 1);
        Boulder b = new Boulder(1, 0);
        FloorSwitch f = new FloorSwitch(2, 0);
        Exit e = new Exit(1, 2);
        GoalLeaf treasure = new GoalLeaf("treasure", 1);
        GoalLeaf boulders = new GoalLeaf("boulders", 1);
        GoalLeaf exit = new GoalLeaf("exit", 0);
        GoalComposite goalOr = new GoalComposite("OR");
        goalOr.addGoals(treasure);
        goalOr.addGoals(boulders);
        GoalComposite goalAnd = new GoalComposite("AND");
        goalAnd.addGoals(exit);
        goalAnd.addGoals(goalOr);
        d.addEntity(p);
        d.addEntity(t);
        d.addEntity(b);
        d.addEntity(f);
        d.addEntity(e);
        d.setPlayer(p);
        d.setGoal(goalAnd);
        p.addObserver(treasure, "goals");
        p.addObserver(boulders, "goals");

        assertEquals(d.isLevelComplete(), false);
        p.moveRight();
        assertEquals(d.isLevelComplete(), true);
        assertEquals(e.getState(), "Closed");
        p.moveDown();
        p.moveDown();
        assertEquals(e.getState(), "Open");
    }
}
