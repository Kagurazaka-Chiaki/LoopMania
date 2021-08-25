package unsw.goal;

import java.util.ArrayList;
import java.util.List;

import unsw.loopmania.LoopManiaWorld;

/**
 * <p>
 * Class {@code Or}
 * </p >
 *
 * @see unsw.goal.Or
 * @since 1.0
 **/
public class Or implements Goal {

    private final List<Goal> goal;

    public Or() {
        this.goal = new ArrayList<>();
    }

    /**
     * show the or goals
     */
    public String showGoalDetail() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < goal.size(); i++) {
            if (i == 0) {
                text.append("( ").append(goal.get(i).showGoalDetail());
            } else if (i == (goal.size() - 1)) {
                text.append(" OR ").append(goal.get(i).showGoalDetail()).append(" )");
            } else {
                text.append(" OR ").append(goal.get(i).showGoalDetail());
            }
        }
        return text.toString();
    }

    /***
     *
     * @param requirement //
     */
    public void addGoal(Goal requirement) {
        goal.add(requirement);
    }

    /**
     * @param world //
     */
    public boolean goalAchieve(LoopManiaWorld world) {
        for (Goal g : goal) {
            if (g.goalAchieve(world)) {
                return true;
            }
        }
        return false;
    }

}
