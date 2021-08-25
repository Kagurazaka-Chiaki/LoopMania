package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.Character;
import unsw.entity.Item;
import unsw.entity.building.HeroCastleBuilding;
import unsw.entity.item.Anduril;
import unsw.entity.item.TreeStump;
import unsw.entity.item.TheOneRing;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.mode.BerserkerMode;
import unsw.mode.SurvivalMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModeTest {
    private LoopManiaWorld world;
    private Character character;
    private List<Pair<Integer, Integer>> orderedPath = null;

    @BeforeEach
    public void setupWorld() {
        JFXPanel jfxPanel = new JFXPanel();

        this.orderedPath = Arrays.asList( //
            Pair.with(3, 4), Pair.with(4, 4), Pair.with(4, 5) //
        );

        this.world = new LoopManiaWorld(7, 7, this.orderedPath);
        this.character = new Character(new PathPosition(0, this.orderedPath), world);
        world.setCharacter(character);
        this.world.setHeroCastle(new HeroCastleBuilding(Pair.with(0, 0)));
    }

    @Test
    public void testRareItemInSurvivalMode() {
        // test rare item generate in survival mode
        SurvivalMode mode = new SurvivalMode();
        world.setMode(mode);

        GameRandom.random.setSeed(4102);
        // generate one anduril, and check its status
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Anduril.class.equals(item.getClass()));
        assertEquals(item.getStatus().getDefense(), 0);
        assertEquals(item.getStatus().getStrength(), 10.5);
        assertEquals(item.getStatus().getHealth(), 0);

        GameRandom.random.setSeed(4101);
        // generate one Tree stump, check its status
        item = world.getItemSystem().addUnequippedItem();
        assertTrue(TreeStump.class.equals(item.getClass()));
        assertEquals(item.getStatus().getDefense(), 10.5);
        assertEquals(item.getStatus().getStrength(), 0);
        assertEquals(item.getStatus().getHealth(), 0);

        GameRandom.random.setSeed(4113);
        // generate one ring, and check that its status
        item = world.getItemSystem().addUnequippedItem();
        assertTrue(TheOneRing.class.equals(item.getClass()));
        assertEquals(item.getStatus().getDefense(), 0);
        assertEquals(item.getStatus().getStrength(), 0);
        assertEquals(item.getStatus().getHealth(), 0);
    }

    @Test
    public void testRareItemInBerserkerMode() {
        // test rare item generate in berserker mode
        BerserkerMode mode = new BerserkerMode();
        world.setMode(mode);

        GameRandom.random.setSeed(4102);
        // generate one anduril, and check its status
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Anduril.class.equals(item.getClass()));
        assertEquals(item.getStatus().getDefense(), 0);
        assertEquals(item.getStatus().getStrength(), 10.5);
        assertEquals(item.getStatus().getHealth(), 0);

        GameRandom.random.setSeed(4101);
        // generate one Tree stump, check its status
        item = world.getItemSystem().addUnequippedItem();
        assertTrue(TreeStump.class.equals(item.getClass()));
        assertEquals(item.getStatus().getDefense(), 10.5);
        assertEquals(item.getStatus().getStrength(), 0);
        assertEquals(item.getStatus().getHealth(), 0);

        GameRandom.random.setSeed(4113);
        // generate one ring, and check that its status
        item = world.getItemSystem().addUnequippedItem();
        assertTrue(TheOneRing.class.equals(item.getClass()));
        assertEquals(item.getStatus().getDefense(), 0);
        assertEquals(item.getStatus().getStrength(), 0);
        assertEquals(item.getStatus().getHealth(), 0);
    }
}