package unsw.worlds;

import unsw.goal.*;

/**
 * <p>
 * Class {@code  Chapter1}
 * </p>
 * <p>
 * chapter1
 *
 * @see unsw.worlds.Chapter1
 * @since 1.0
 **/
public class Chapter1 implements Worlds {

    private final String worldFile;

    private Goal goal;

    public Chapter1() {
        this.worldFile = "chapter_1.json";
        setGoal();
    }

    private void setGoal() {
        GoldGoal goldGoal = new GoldGoal(10000);
        ExperienceGoal expGoal = new ExperienceGoal(7000);
        CycleGoal cycleGoal = new CycleGoal(20);

        Or orGoal = new Or();
        orGoal.addGoal(goldGoal);
        orGoal.addGoal(cycleGoal);
        orGoal.addGoal(expGoal);

        this.goal = orGoal;
    }

    public String getWorldFile() {
        return worldFile;
    }

    public Goal getGoal() {
        return goal;
    }
}
