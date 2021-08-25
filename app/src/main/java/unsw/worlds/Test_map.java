package unsw.worlds;

import unsw.goal.*;

public class Test_map implements Worlds {

    private final String worldFile;

    private Goal goal;

    public Test_map() {
        this.worldFile = "test_map.json";
        setGoal();
    }

    private void setGoal() {
        GoldGoal goldGoal = new GoldGoal(5000);
        ExperienceGoal expGoal = new ExperienceGoal(20000);
        CycleGoal cycleGoal = new CycleGoal(20);

        Or orGoal = new Or();
        orGoal.addGoal(goldGoal);
        orGoal.addGoal(expGoal);

        And andGoal = new And();
        andGoal.addGoal(orGoal);
        andGoal.addGoal(cycleGoal);

        this.goal = andGoal;
    }

    public String getWorldFile() {
        return worldFile;
    }

    public Goal getGoal() {
        return goal;
    }

}
