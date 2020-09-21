package unsw.dungeon;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;

public class GoalLeaf implements GoalComponent, Observer {
    private String goal;
    private IntegerProperty remainingQuest;

    public GoalLeaf(String goal, int remainingQuest) {
        this.goal = goal;
        this.remainingQuest = new SimpleIntegerProperty();
        this.remainingQuest.setValue(remainingQuest);
    }

    /**
     * Returns the goal 
     * @return goal
     */
    public String getGoal() {
        return goal;
    }

    /**
     * Gets the number of remaining enemies, treasures, switches left for the player to collect/trigger 
     * @return number of remaining quests
     */
    public IntegerProperty getRemainingQuest() {
        return remainingQuest;
    }

    public int getRemainingQuestAsInt() {
        return remainingQuest.getValue();
    }

    /**
     * Decrements the counter for the remaining quests needed to complete the goal
     */
    public void decrementRemainingQuest() {
        remainingQuest.setValue(remainingQuest.getValue() - 1);
    }

    /**
     * Increments the counter for the remaining quests needed to complete the goal
     */
    public void incrementRemainingQuest() {
        remainingQuest.setValue(remainingQuest.getValue() + 1);
    }

    /**
     * Checks whether the level is complete
     * @return true or false
     */
    @Override
    public boolean isLevelComplete() {
        if (remainingQuest.getValue() == 0) {
            return true;
        }
        return false;
    }

    /**
     * When the goal is called for an update, either decrement or increment the remaining quests
     * @param e the entity the update call is related to
     * @param request the action to be made by the update call
     */
    @Override
    public void update(Entity e, String request) {
        if ((goal.equals(e.getName()) || (goal.equals("enemies") && e.getName().equals("hound"))) && request.equals("decrement")){
            decrementRemainingQuest();
        } else if (goal.equals(e.getName()) && request.equals("increment")) {
            incrementRemainingQuest();
        }
    }
}
