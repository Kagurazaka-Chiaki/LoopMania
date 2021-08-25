package unsw.worlds;

import unsw.goal.*;

public class Mini_map implements Worlds {

    private final String worldFile;

    private Goal goal;

    public Mini_map() {
        this.worldFile = "mini_map.json";
        setGoal();
    }

    private void setGoal() {
        this.goal = new CycleGoal(10);
    }

    public String getWorldFile() {
        return worldFile;
    }

    public Goal getGoal() {
        return goal;
    }

}
