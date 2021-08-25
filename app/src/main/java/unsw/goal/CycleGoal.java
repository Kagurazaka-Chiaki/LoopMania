package unsw.goal;

import unsw.loopmania.LoopManiaWorld;

/**
 * <p>
 * Class {@code CycleGoal}
 * </p >
 *
 * @see unsw.goal.CycleGoal
 * @since 1.0
 **/
public class CycleGoal implements Goal {

    private final int cycle;

    public CycleGoal(int cycle) {
        this.cycle = cycle;
    }

    /**
     * print out the number of cycle
     */
    public String showGoalDetail() {
        return "Cycle: " + cycle;
    }

    /**
     * @param world check if the cycle goal is achieved
     */
    public boolean goalAchieve(LoopManiaWorld world) {
        return world.getCycle().get() >= this.cycle;
    }
}
