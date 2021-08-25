package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.building.HeroCastleBuilding;
import unsw.loopmania.LoopManiaWorld;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    private LoopManiaWorld world;

    private List<Pair<Integer, Integer>> orderedPath = null;
    private List<Pair<Integer, Integer>> invalidPath = null;

    private final Random random = GameRandom.random;

    @BeforeEach
    public void setupWorld() {

        JFXPanel jfxPanel = new JFXPanel();

        this.orderedPath = Arrays.asList( //
            Pair.with(0, 0), Pair.with(1, 0), Pair.with(2, 0), Pair.with(3, 0), Pair.with(4, 0), //
            Pair.with(0, 1), Pair.with(4, 1),//
            Pair.with(0, 2), Pair.with(4, 2),//
            Pair.with(0, 3), Pair.with(4, 3),//
            Pair.with(0, 4), Pair.with(1, 4), Pair.with(2, 4), Pair.with(3, 4), Pair.with(4, 4) //
        );

        this.invalidPath = Arrays.asList( //
            Pair.with(1, 1), Pair.with(2, 1), Pair.with(3, 1),  //
            Pair.with(1, 2), Pair.with(2, 2), Pair.with(3, 2),  //
            Pair.with(1, 3), Pair.with(2, 3), Pair.with(3, 3)   //
        );

        this.world = new LoopManiaWorld(5, 5, orderedPath);
        this.world.setHeroCastle(new HeroCastleBuilding(Pair.with(0, 0)));
    }

    @Test
    public void testGetN() {
        // System.err.println(world.getPosition().getN(2, 2));

        Integer x = world.getPosition()
                         .getN(2, 2)
                         .getValue0();
        Integer y = world.getPosition()
                         .getN(2, 2)
                         .getValue1();

        assertEquals(2, (int) x);
        assertEquals(1, (int) y);
    }

    @Test
    public void testGetS() {
        // System.err.println(world.getPosition().getN(2, 2));

        Integer x = world.getPosition()
                         .getS(2, 2)
                         .getValue0();
        Integer y = world.getPosition()
                         .getS(2, 2)
                         .getValue1();

        assertEquals(2, (int) x);
        assertEquals(3, (int) y);
    }

    @Test
    public void testGetW() {
        // System.err.println(world.getPosition().getN(2, 2));

        Integer x = world.getPosition()
                         .getW(2, 2)
                         .getValue0();
        Integer y = world.getPosition()
                         .getW(2, 2)
                         .getValue1();

        assertEquals(1, (int) x);
        assertEquals(2, (int) y);
    }

    @Test
    public void testGetE() {
        // System.err.println(world.getPosition().getN(2, 2));

        Integer x = world.getPosition()
                         .getE(2, 2)
                         .getValue0();
        Integer y = world.getPosition()
                         .getE(2, 2)
                         .getValue1();

        assertEquals(3, (int) x);
        assertEquals(2, (int) y);
    }

    @Test
    public void testGetNW() {
        // System.err.println(world.getPosition().getN(2, 2));

        Integer x = world.getPosition()
                         .getNW(2, 2)
                         .getValue0();
        Integer y = world.getPosition()
                         .getNW(2, 2)
                         .getValue1();

        assertEquals(1, (int) x);
        assertEquals(1, (int) y);
    }

    @Test
    public void testGetSW() {
        // System.err.println(world.getPosition().getN(2, 2));

        Integer x = world.getPosition()
                         .getSW(2, 2)
                         .getValue0();
        Integer y = world.getPosition()
                         .getSW(2, 2)
                         .getValue1();

        assertEquals(1, (int) x);
        assertEquals(3, (int) y);
    }

    @Test
    public void testGetNE() {
        // System.err.println(world.getPosition().getN(2, 2));

        Integer x = world.getPosition()
                         .getNE(2, 2)
                         .getValue0();
        Integer y = world.getPosition()
                         .getNE(2, 2)
                         .getValue1();

        assertEquals(3, (int) x);
        assertEquals(1, (int) y);
    }

    @Test
    public void testGetSE() {
        // System.err.println(world.getPosition().getN(2, 2));

        Integer x = world.getPosition()
                         .getSE(2, 2)
                         .getValue0();
        Integer y = world.getPosition()
                         .getSE(2, 2)
                         .getValue1();

        assertEquals(3, (int) x);
        assertEquals(3, (int) y);
    }

    @Test
    public void testIsPath() {
        assertTrue(world.getPosition()
                        .isPath(this.orderedPath.get(random.nextInt(this.orderedPath.size()))));
    }

    @Test
    public void testIsNearBy() {
        Pair<Integer, Integer> path = this.orderedPath.get(random.nextInt(this.orderedPath.size()));
        assertFalse(world.getPosition()
                         .isNearby(path));

        List<Pair<Integer, Integer>> near = this.invalidPath.stream()
                                                            .filter(x -> !x.equals(Pair.with(2, 2)))
                                                            .collect(Collectors.toList());

        assertTrue(world.getPosition()
                        .isNearby(near.get(random.nextInt(near.size()))));
    }

}
