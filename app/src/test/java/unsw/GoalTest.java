package unsw;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.javatuples.Pair;

import unsw.entity.building.HeroCastleBuilding;
import unsw.entity.Character;
import unsw.goal.And;
import unsw.goal.ElanMuskeGoal;
import unsw.goal.ExperienceGoal;
import unsw.goal.GoldGoal;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.LoopManiaWorldControllerLoader;
import unsw.loopmania.LoopManiaWorldLoader;
import unsw.mode.Mode;
import unsw.mode.StandardMode;
import unsw.worlds.*;

import java.io.FileNotFoundException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GoalTest {

    private LoopManiaWorld world;

    private final Random random = GameRandom.random;

    @BeforeEach
    public void createPane() {
        JFXPanel jfxPanel = new JFXPanel();
    }

    @Test
    public void testTrivialGoal() {

        Worlds miniWorld = new Mini_map();
        Mode standardMode = new StandardMode();
        LoopManiaWorldLoader worldLoader = null;
        try {
            worldLoader = new LoopManiaWorldControllerLoader(miniWorld, standardMode);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert worldLoader != null;
        this.world = worldLoader.load();

        assertEquals("Cycle: 10", this.world.getGoal()
                                            .showGoalDetail());

        for (int i = 0; i < 5; i++) {
            world.getCycle().set(random.nextInt(10));
            world.getCharacter().setGold(random.nextInt(100));
            world.getCharacter().getEXP().set(random.nextInt(50) + random.nextInt(100));
            assertFalse(this.world.getGoal().goalAchieve(world));
        }

        for (int i = 0; i < 5; i++) {
            world.getCycle().set(random.nextInt(10) + 10 + 1);
            world.getCharacter().setGold(random.nextInt(100));
            world.getCharacter().getEXP().set(random.nextInt(25) + random.nextInt(100));
            assertTrue(this.world.getGoal().goalAchieve(world));
        }

    }

    @Test
    public void testChapterGoal_1() {

        Worlds miniWorld = new Chapter1();
        Mode standardMode = new StandardMode();
        LoopManiaWorldLoader worldLoader = null;

        try {
            worldLoader = new LoopManiaWorldControllerLoader(miniWorld, standardMode);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert worldLoader != null;
        this.world = worldLoader.load();

        assertEquals("( Gold: 10000 OR Cycle: 20 OR Experience: 7000 )", this.world.getGoal()
                                                                                   .showGoalDetail());

        for (int i = 0; i < 5; i++) {
            world.getCycle().set(random.nextInt(20));
            world.getCharacter().setGold(random.nextInt(10000));
            world.getCharacter().getEXP().set(random.nextInt(7000));
            assertFalse(this.world.getGoal().goalAchieve(world));
        }

        for (int i = 0; i < 5; i++) {
            world.getCycle().set(random.nextInt(20));
            world.getCharacter().setGold(random.nextInt(10000) + 10000 + 1);
            world.getCharacter().getEXP().set(random.nextInt(7000));
            assertTrue(this.world.getGoal().goalAchieve(world));
        }

        for (int i = 0; i < 5; i++) {
            world.getCycle().set(random.nextInt(20) + 20 + 1);
            world.getCharacter().setGold(random.nextInt(10000));
            world.getCharacter().getEXP().set(random.nextInt(7000));
            assertTrue(this.world.getGoal().goalAchieve(world));
        }

        for (int i = 0; i < 5; i++) {
            world.getCycle().set(random.nextInt(20));
            world.getCharacter().setGold(random.nextInt(10000));
            world.getCharacter().getEXP().set(random.nextInt(7000) + 7000 + 1);
            assertTrue(this.world.getGoal().goalAchieve(world));
        }

    }

    @Test
    public void testChapterGoal_2() {

        Worlds miniWorld = new Chapter2();
        Mode standardMode = new StandardMode();
        LoopManiaWorldLoader worldLoader = null;

        try {
            worldLoader = new LoopManiaWorldControllerLoader(miniWorld, standardMode);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert worldLoader != null;
        this.world = worldLoader.load();

        assertEquals("( Gold: 7000 AND Experience: 10000 )", this.world.getGoal()
                                                                       .showGoalDetail());

        for (int i = 0; i < 5; i++) {
            world.getCycle().set(random.nextInt(20));
            world.getCharacter().setGold(random.nextInt(7000));
            world.getCharacter().getEXP().set(random.nextInt(10000));
            assertFalse(this.world.getGoal().goalAchieve(world));
        }

        for (int i = 0; i < 5; i++) {
            world.getCycle().set(random.nextInt(20));
            world.getCharacter().setGold(random.nextInt(7000) + 7000 + 1);
            world.getCharacter().getEXP().set(random.nextInt(10000) + 10000 + 1);
            assertTrue(this.world.getGoal().goalAchieve(world));
        }

    }

    @Test
    public void testChapterGoal_3() {

        Worlds miniWorld = new Chapter3();
        Mode standardMode = new StandardMode();
        LoopManiaWorldLoader worldLoader = null;

        try {
            worldLoader = new LoopManiaWorldControllerLoader(miniWorld, standardMode);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert worldLoader != null;
        this.world = worldLoader.load();

        assertEquals("( ( Cycle: 100 AND Kill Doggie: 10 ) OR ( Kill Elan Muske: 3 AND Gold: 50000 ) )",
            this.world.getGoal().showGoalDetail());

        // check that gold and doggie achieved and it will still not passed goal
        world.getCycle().set(90);
        world.getCharacter().setGold(50000);
        world.getDoggieKilled().set(10);
        world.getElanMuskeKilled().set(2);
        assertFalse(this.world.getGoal().goalAchieve(world));

        // check that cycle and elan muske achieved and it will still not passed goal
        world.getCycle().set(100);
        world.getCharacter().setGold(4000);
        world.getDoggieKilled().set(9);
        world.getElanMuskeKilled().set(3);
        assertFalse(this.world.getGoal().goalAchieve(world));

        // check cycle and doggie achived and passed.
        world.getCycle().set(110);
        world.getCharacter().setGold(4000);
        world.getDoggieKilled().set(11);
        world.getElanMuskeKilled().set(2);
        assertTrue(this.world.getGoal().goalAchieve(world));

        // check elan muske and gold achived and passed.
        world.getCycle().set(90);
        world.getCharacter().setGold(60000);
        world.getDoggieKilled().set(9);
        world.getElanMuskeKilled().set(4);
        assertTrue(this.world.getGoal().goalAchieve(world));

        // check all gold achived and passed.
        world.getCycle().set(100);
        world.getCharacter().setGold(50000);
        world.getDoggieKilled().set(10);
        world.getElanMuskeKilled().set(3);
        assertTrue(this.world.getGoal().goalAchieve(world));
    }

    @Test
    public void testTestMapGoal() {

        Worlds miniWorld = new Test_map();
        Mode standardMode = new StandardMode();
        LoopManiaWorldLoader worldLoader = null;

        try {
            worldLoader = new LoopManiaWorldControllerLoader(miniWorld, standardMode);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert worldLoader != null;
        this.world = worldLoader.load();

        assertEquals("( ( Gold: 5000 OR Experience: 20000 ) AND Cycle: 20 )", this.world.getGoal()
                                                                                        .showGoalDetail());

        for (int i = 0; i < 5; i++) {
            world.getCycle().set(random.nextInt(20));
            world.getCharacter().setGold(random.nextInt(5000));
            world.getCharacter().getEXP().set(random.nextInt(20000));
            assertFalse(this.world.getGoal().goalAchieve(world));
        }

        for (int i = 0; i < 5; i++) {
            world.getCycle().set(random.nextInt(20) + 20 + 1);
            world.getCharacter().setGold(random.nextInt(5000));
            world.getCharacter().getEXP().set(random.nextInt(20000));
            assertFalse(this.world.getGoal().goalAchieve(world));
        }

        for (int i = 0; i < 5; i++) {
            world.getCycle().set(random.nextInt(20) + 20 + 1);
            world.getCharacter().setGold(random.nextInt(5000) + 5000 + 1);
            world.getCharacter().getEXP().set(random.nextInt(20000));
            assertTrue(this.world.getGoal().goalAchieve(world));
        }

        for (int i = 0; i < 5; i++) {
            world.getCycle().set(random.nextInt(20) + 20 + 1);                   // cycle [21, 41)
            world.getCharacter().setGold(random.nextInt(5000));         // gold  [0, 5000)
            world.getCharacter().getEXP().set(random.nextInt(20000) + 20000 + 1);   // exp   [20001, 40001)
            assertTrue(this.world.getGoal().goalAchieve(world));
        }

    }

    @Test
    public void testTrivialAndGoal() {
        // test that 3 and composite goal works

        ElanMuskeGoal elanGoal = new ElanMuskeGoal(3);
        GoldGoal goldGoal = new GoldGoal(50000);
        ExperienceGoal expGoal = new ExperienceGoal(1000);

        And andGoal = new And();
        andGoal.addGoal(elanGoal);
        andGoal.addGoal(goldGoal);
        andGoal.addGoal(expGoal);

        assertEquals("( Kill Elan Muske: 3 AND Gold: 50000 AND Experience: 1000 )",
            andGoal.showGoalDetail());

        world = new LoopManiaWorld(7, 7, null);
        world.setMode(new StandardMode());
        world.setGoal(andGoal);
        world.setHeroCastle(new HeroCastleBuilding(Pair.with(0, 0)));
        Character character = new Character(null, world);
        world.setCharacter(character);


        // check that elan muske achieved and it will still not passed goal
        world.getCharacter().setGold(40000);
        character.getEXP().set(900);
        world.getElanMuskeKilled().set(3);
        assertFalse(this.world.getGoal().goalAchieve(world));

        // check all gold achived and passed.
        world.getCharacter().setGold(50000);
        character.getEXP().set(1000);
        world.getElanMuskeKilled().set(3);
        assertTrue(this.world.getGoal().goalAchieve(world));
    }

}
