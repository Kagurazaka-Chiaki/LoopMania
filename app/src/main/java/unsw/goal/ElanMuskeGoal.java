package unsw.goal;

import unsw.loopmania.LoopManiaWorld;

public class ElanMuskeGoal implements Goal {
    private final int elanMuske;

    public ElanMuskeGoal(int elanMuske) {
        this.elanMuske = elanMuske;
    }

    /**
     * show the number of Elan Muske needs to be killed to win
     */
    public String showGoalDetail() {
        return "Kill Elan Muske: " + elanMuske;
    }

    /**
     * check if the Elan Muske goal is achieved
     *
     * @param world //
     */
    public boolean goalAchieve(LoopManiaWorld world) {
        return world.getElanMuskeKilledValue() >= this.elanMuske;
    }
}
