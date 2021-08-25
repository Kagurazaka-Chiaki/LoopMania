package unsw.loopmania;

import org.javatuples.Pair;

import java.util.Arrays;
import java.util.List;

public class Position {

    /**
     * width of the world in GridPane cells
     */
    private final int width;

    /**
     * height of the world in GridPane cells
     */
    private final int height;

    private final LoopManiaWorld world;

    public Position(LoopManiaWorld world) {
        this.world = world;
        this.width = world.getWidth();
        this.height = world.getHeight();
    }

    /**
     * return north pair integer (position)
     *
     * @param x //
     * @param y //
     **/
    public Pair<Integer, Integer> getN(int x, int y) {
        y = y - 1;
        if (y < 0) { y = 0; }
        return Pair.with(x, y);
    }

    /**
     * return south pair integer (position)
     *
     * @param x //
     * @param y //
     **/
    public Pair<Integer, Integer> getS(int x, int y) {
        y = y + 1;
        if (y > this.width) { y = this.width; }
        return Pair.with(x, y);
    }

    /**
     * return east pair integer (position)
     *
     * @param x //
     * @param y //
     **/
    public Pair<Integer, Integer> getE(int x, int y) {
        x = x + 1;
        if (x > this.height) { x = this.height; }
        return Pair.with(x, y);
    }

    /**
     * return west pair integer (position)
     *
     * @param x //
     * @param y //
     **/
    public Pair<Integer, Integer> getW(int x, int y) {
        x = x - 1;
        if (x < 0) { x = 0; }
        return Pair.with(x, y);
    }

    /**
     * return northwest pair integer (position)
     *
     * @param x //
     * @param y //
     **/
    public Pair<Integer, Integer> getNW(int x, int y) {
        x = x - 1;
        y = y - 1;
        if (x < 0) { x = 0; }
        if (y < 0) { y = 0; }
        return Pair.with(x, y);

    }

    /**
     * return northeast pair integer (position)
     *
     * @param x //
     * @param y //
     **/
    public Pair<Integer, Integer> getNE(int x, int y) {
        y = y - 1;
        x = x + 1;
        if (y < 0) { y = 0; }
        if (x > this.height) { x = this.height; }
        return Pair.with(x, y);
    }

    /**
     * return southwest pair integer (position)
     *
     * @param x //
     * @param y //
     **/
    public Pair<Integer, Integer> getSW(int x, int y) {
        y = y + 1;
        x = x - 1;
        if (y > this.width) { y = this.width; }
        if (x < 0) { x = this.height; }
        return Pair.with(x, y);
    }

    /**
     * return southeast pair integer (position)
     *
     * @param x //
     * @param y //
     **/
    public Pair<Integer, Integer> getSE(int x, int y) {
        x = x + 1;
        y = y + 1;
        if (x > this.width) { x = this.width; }
        if (y > this.height) { y = this.height; }
        return Pair.with(x, y);
    }

    /**
     * return whether current position is nearby
     *
     * @param currentPath //
     **/
    public Boolean isNearby(Pair<Integer, Integer> currentPath) {
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        Integer x = currentPath.getValue0();
        Integer y = currentPath.getValue1();

        List<Pair<Integer, Integer>> nearby = Arrays.asList( //
            getN(x, y), getS(x, y), getW(x, y), getE(x, y) //
        );

        boolean result = Boolean.FALSE;

        for (Pair<Integer, Integer> i : nearby) {
            if (orderedPath.contains(i)) {
                result = Boolean.TRUE;
                break;
            }
        }

        return result && !orderedPath.contains(currentPath);
    }

    /**
     * return whether current position is path
     *
     * @param currentPath //
     **/
    public Boolean isPath(Pair<Integer, Integer> currentPath) {
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        return orderedPath.contains(currentPath);
    }

}
