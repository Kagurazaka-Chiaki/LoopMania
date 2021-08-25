package unsw.worlds;

import unsw.goal.*;

/**
 * <p>
 * Class {@code Chapter2}
 * </p>
 * <p>
 * chapter2
 *
 * @see unsw.worlds.Chapter2
 * @since 1.0
 **/
public class Chapter2 implements Worlds {

    private final String worldFile;

    private Goal goal;

    public Chapter2() {
        this.worldFile = "chapter_2.json";
        setGoal();
    }

    private void setGoal() {
        GoldGoal goldGoal = new GoldGoal(7000);
        ExperienceGoal expGoal = new ExperienceGoal(10000);

        And andGoal = new And();
        andGoal.addGoal(goldGoal);
        andGoal.addGoal(expGoal);

        this.goal = andGoal;
    }

    public String getWorldFile() {
        return worldFile;
    }

    public Goal getGoal() {
        return goal;
    }
}
