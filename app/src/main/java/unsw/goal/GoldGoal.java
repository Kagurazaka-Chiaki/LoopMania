package unsw.goal;

import unsw.loopmania.LoopManiaWorld;

/**
 * <p>
 * Class {@code GoldGoal}
 * </p >
 *
 * @see unsw.goal.GoldGoal
 * @since 1.0
 **/
public class GoldGoal implements Goal {

    private final int gold;

    public GoldGoal(int gold) {
        this.gold = gold;
    }

    /**
     * show gold goal
     */
    public String showGoalDetail() {
        return "Gold: " + gold;
    }

    /**
     * @param world check if the gold goal is achieved
     */
    public boolean goalAchieve(LoopManiaWorld world) {
        return world.getCharacter().getGoldValue() >= this.gold;
    }

}
