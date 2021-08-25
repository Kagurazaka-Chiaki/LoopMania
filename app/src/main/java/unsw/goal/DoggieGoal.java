package unsw.goal;

import unsw.loopmania.LoopManiaWorld;

public class DoggieGoal implements Goal {
    private final int doggie;

    public DoggieGoal(int doggie) {
        this.doggie = doggie;
    }

    /**
     * show the number of Doggie needs to be killed to win
     */
    public String showGoalDetail() {
        return "Kill Doggie: " + doggie;
    }

    /**
     * check if the Doggie goal is achieved
     *
     * @param world //
     */
    public boolean goalAchieve(LoopManiaWorld world) {
        return world.getDoggieKilledValue() >= this.doggie;
    }
}
