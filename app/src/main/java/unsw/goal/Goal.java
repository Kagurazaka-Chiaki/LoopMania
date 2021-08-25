package unsw.goal;

import unsw.loopmania.LoopManiaWorld;

/**
 * <p>
 * Class {@code Goal}
 * </p >
 *
 * @see unsw.goal.Goal
 * @since 1.0
 **/
public interface Goal {

    boolean goalAchieve(LoopManiaWorld world);

    String showGoalDetail();

}
