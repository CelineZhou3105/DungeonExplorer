package unsw.dungeon;

import java.util.ArrayList;

public class GoalComposite implements GoalComponent {
    private String type; // "AND", "OR"
    private ArrayList<GoalComponent> goalList;

    public GoalComposite(String type) {
        this.type = type;
        goalList = new ArrayList<GoalComponent>();
    }

    /**
     * Adds goals to the list of goals
     * @param goal the goal to be added
     */
    public void addGoals(GoalComponent goal) {
        goalList.add(goal);
    }

    /**
     * Gets the list of goals in this composite object.
     * @return an ArrayList of this goal's children goals.
     */
    public ArrayList<GoalComponent> getGoalList() {
        return goalList;
    }

    /**
     * Gets the type of goal
     * @return "OR" or "AND"
     */
    public String getType() {
        return type;
    }

    /**
     * Checks whether the level is complete
     * @return true or false
     */
    @Override
    public boolean isLevelComplete() {
        boolean pass = true;
        if (type.equals("AND")) { // if goal is AND
            for (GoalComponent g: goalList) {
                if (!g.isLevelComplete()) { // if any goals is not complete
                    pass = false; // return false
                    break;
                }
            }
        } else if (type.equals("OR")) { // if goal is OR
            int count = 0;
            for (GoalComponent g: goalList) {
                if (g.isLevelComplete()) {
                    count++;
                }
            }
            if (count == 0) {
                pass = false;
            }
        }
        return pass;
    }
}
