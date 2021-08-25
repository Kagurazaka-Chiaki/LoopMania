package unsw.goal;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.LoopManiaWorld;

/**
 * <p>
 * Class {@code And}
 * </p >
 *
 * @see unsw.goal.And
 * @since 1.0
 **/
public class And implements Goal {

    private final List<Goal> goal;

    public And() {
        this.goal = new ArrayList<>();
    }

    public String showGoalDetail() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < goal.size(); i++) {
            if (i == 0) {
                text.append("( ").append(goal.get(i).showGoalDetail());
            } else if (i == (goal.size() - 1)) {
                text.append(" AND ").append(goal.get(i).showGoalDetail()).append(" )");
            } else {
                text.append(" AND ").append(goal.get(i).showGoalDetail());
            }
        }
        return text.toString();
    }

    /**
     * @param requirement add goal to the goal list
     */
    public void addGoal(Goal requirement) {
        goal.add(requirement);
    }

    /**
     * @param world //
     */
    public boolean goalAchieve(LoopManiaWorld world) {
        for (Goal g : goal) {
            if (!g.goalAchieve(world)) {
                return false;
            }
        }
        return true;
    }

}
