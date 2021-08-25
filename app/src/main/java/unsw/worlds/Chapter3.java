package unsw.worlds;

import unsw.goal.*;

/**
 * <p>
 * Class {@code Chapter3}
 * </p>
 * <p>
 * chapter3
 *
 * @see unsw.worlds.Chapter3
 * @since 1.0
 **/
public class Chapter3 implements Worlds {

    private final String worldFile;
    private Goal goal;

    public Chapter3() {
        this.worldFile = "chapter_3.json";
        setGoal();
    }

    private void setGoal() {
        ElanMuskeGoal elanGoal = new ElanMuskeGoal(3);
        GoldGoal goldGoal = new GoldGoal(50000);
        DoggieGoal doggieGoal = new DoggieGoal(10);
        CycleGoal cycleGoal = new CycleGoal(100);

        And andGoal1 = new And();
        andGoal1.addGoal(cycleGoal);
        andGoal1.addGoal(doggieGoal);

        And andGoal2 = new And();
        andGoal2.addGoal(elanGoal);
        andGoal2.addGoal(goldGoal);

        Or orGoal = new Or();
        orGoal.addGoal(andGoal1);
        orGoal.addGoal(andGoal2);

        this.goal = orGoal;
    }

    public String getWorldFile() {
        return worldFile;
    }

    public Goal getGoal() {
        return goal;
    }
}
