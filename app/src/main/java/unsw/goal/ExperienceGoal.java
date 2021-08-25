package unsw.goal;

import unsw.loopmania.LoopManiaWorld;

/**
 * <p>
 * Class {@code ExperienceGoal}
 * </p >
 *
 * @see unsw.goal.ExperienceGoal
 * @since 1.0
 **/
public class ExperienceGoal implements Goal {

    private final int experience;

    public ExperienceGoal(int experience) {
        this.experience = experience;
    }

    /**
     * show experience goal
     */
    public String showGoalDetail() {
        return "Experience: " + experience;
    }

    /**
     * @param world check if the exp goal is achieved
     */
    public boolean goalAchieve(LoopManiaWorld world) {
        return world.getCharacter().getExpValue() >= this.experience;
    }
}
