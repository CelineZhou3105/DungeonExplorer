package unsw.dungeon;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Loads a dungeon from a .json file.
 *
 * By extending this class, a subclass can hook into entity creation. This is
 * useful for creating UI elements with corresponding entities.
 *
 * @author Robert Clifton-Everest
 *
 */
public abstract class DungeonLoader {

    private JSONObject json;

    public DungeonLoader(String filename) throws FileNotFoundException {
        json = new JSONObject(new JSONTokener(new FileReader("dungeons/" + filename)));
    }

    /**
     * Takes in a JSON object 
     * @param json the json object containing the entities and their coordinates
     */
    public DungeonLoader(JSONObject json) {
        this.json = json;
    }

    /**
     * Parses the JSON to create a dungeon.
     * @return
     */
    public Dungeon load() {
        int width = json.getInt("width");
        int height = json.getInt("height");

        Dungeon dungeon = new Dungeon(width, height);

        JSONArray jsonEntities = json.getJSONArray("entities");

        for (int i = 0; i < jsonEntities.length(); i++) {
            loadEntity(dungeon, jsonEntities.getJSONObject(i));
        }

        // Add enemies as observers to player
        for (Entity e: dungeon.getEntityList()) {
            if (e.getName().equals("enemies")) {
                dungeon.getPlayer().addObserver((Enemy)e, "enemies");
            }
        }

        // Construct the goal object
        JSONObject goalJson = json.getJSONObject("goal-condition");
        GoalComponent g = constructGoals(dungeon, goalJson);
        dungeon.setGoal(g);

        return dungeon;
    }

    private void loadEntity(Dungeon dungeon, JSONObject json) {
        String type = json.getString("type");
        int x = json.getInt("x");
        int y = json.getInt("y");

        Entity entity = null;
        switch (type) {
        case "player":
            Player player = new Player(dungeon, x, y);
            dungeon.setPlayer(player);
            onLoad(player);
            entity = player;
            break;
        case "wall":
            Wall wall = new Wall(x, y);
            onLoad(wall);
            entity = wall;
            break;
        // Handle other possible entities
        case "exit":
            Exit exit = new Exit(x,y);
            onLoad(exit);
            entity = exit;
            break;

        case "switch":
            FloorSwitch floorSwitch = new FloorSwitch(x,y);
            onLoad(floorSwitch);
            entity = floorSwitch;
            ArrayList<Entity> array = dungeon.checkOccupied(x, y);
            if (array.size() > 0) {
                floorSwitch.on();
            }
            break;

        case "sword":
            Sword sword = new Sword(x, y);
            onLoad(sword);
            entity = sword;
            break;

        case "invincibility":
            Potion potion = new Potion(x, y);
            onLoad(potion);
            entity = potion;
            break;

        case "treasure":
            Treasure treasure = new Treasure(x,y);
            onLoad(treasure);
            entity = treasure;
            break;

        case "door":
            int doorId = json.getInt("id");
            Door door = new Door(doorId, x,y);
            onLoad(door);
            entity = door;
            break;

        case "key":
            int keyId = json.getInt("id");
            Key key = new Key(keyId,x,y);
            onLoad(key);
            entity = key;
            break;
        
        case "portal":
            int portalId = json.getInt("id");
            Portal portal = new Portal(portalId,x,y);
            onLoad(portal);
            entity = portal;
            break;

        case "boulder":
            Boulder boulder = new Boulder(x,y);
            onLoad(boulder);
            entity = boulder;
            ArrayList<Entity> arrayBoulder = dungeon.checkOccupied(x, y);
            if (arrayBoulder.size() > 0) {
                FloorSwitch s = (FloorSwitch)arrayBoulder.get(0);
                s.on();
            }
            break;

        case "enemy":
            Enemy enemy = new Enemy(x,y);
            onLoad(enemy);
            entity = enemy;
            break;
        
        case "hound":
            int end_x = json.getInt("end_x");
            int end_y = json.getInt("end_y");
            Hound hound = new Hound(dungeon, x, y, end_x, end_y);
            onLoad(hound);
            entity = hound;
            break;
        }
        dungeon.addEntity(entity);
    }

    // Create additional abstract methods for the other entities
    public abstract void onLoad(Entity player);

    public abstract void onLoad(Wall wall);

    public abstract void onLoad(Exit exit);

    public abstract void onLoad(FloorSwitch floorSwitch);

    public abstract void onLoad(Sword sword);

    public abstract void onLoad(Potion potion);

    public abstract void onLoad(Treasure treasure);

    public abstract void onLoad(Door door);

    public abstract void onLoad(Key key);

    public abstract void onLoad(Portal portal);

    public abstract void onLoad(Boulder boulder);

    public abstract void onLoad(Enemy enemy);

    public abstract void onLoad(Hound hound);

    public GoalComponent constructGoals(Dungeon dungeon, JSONObject json) {
        // Can either be an "OR", "AND" or normal GoalLeaf
        String goalType = json.getString("goal");
        GoalComposite goalComp;
        if (goalType.equals("OR")) {
            goalComp = new GoalComposite("OR");
            // Run through each subgoal and create the objects
            JSONArray subgoals = json.getJSONArray("subgoals");
            for (int i = 0; i < subgoals.length(); i++) {
                JSONObject j = subgoals.getJSONObject(i);
                goalComp.addGoals(constructGoals(dungeon, j));
            }

        } else if (goalType.equals("AND")) {
            goalComp = new GoalComposite("AND");
            // Run through each subgoal and create the objects
            JSONArray subgoals = json.getJSONArray("subgoals");
            for (int i = 0; i < subgoals.length(); i++) {
                JSONObject j = subgoals.getJSONObject(i);
                goalComp.addGoals(constructGoals(dungeon, j));
            }

        } else {
            // Just a normal goal leaf
            GoalLeaf g;
            if (goalType.equals("enemies")) {
                g = new GoalLeaf(goalType, dungeon.countEnemies());
            } else if (goalType.equals("treasure")) {
                g = new GoalLeaf(goalType, dungeon.countTreasure());
            } else if (goalType.equals("boulders")) {
                g = new GoalLeaf(goalType, dungeon.countBoulders());
                for (Entity e: dungeon.getEntityList()) {
                    if (e instanceof FloorSwitch) {
                        if (((FloorSwitch) e).getState().equals("On")) {
                            g.decrementRemainingQuest();
                        }
                    }
                }
            } else { // Exit
                g = new GoalLeaf(goalType, 0);
            }
            dungeon.getPlayer().addObserver(g, "goals");
            return g;
        }

        return goalComp;
    }
}
