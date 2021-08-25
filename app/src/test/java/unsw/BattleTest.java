package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.Item;
import unsw.entity.enemy.AlliedSoldier;
import unsw.entity.enemy.Doggie;
import unsw.entity.enemy.ElanMuske;
import unsw.entity.enemy.Slug;
import unsw.entity.enemy.Thief;
import unsw.entity.enemy.Vampire;
import unsw.entity.enemy.Zombie;
import unsw.entity.item.Anduril;
import unsw.entity.item.Shield;
import unsw.entity.item.Staff;
import unsw.entity.item.TheOneRing;
import unsw.entity.item.TreeStump;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.LoopManiaWorldControllerLoader;
import unsw.loopmania.LoopManiaWorldLoader;
import unsw.loopmania.PathPosition;
import unsw.mode.Mode;
import unsw.mode.StandardMode;
import unsw.worlds.Mini_map;
import unsw.worlds.Worlds;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class BattleTest {

    private LoopManiaWorld world;
    private Character character;

    private List<Pair<Integer, Integer>> orderedPath = null;

    private final Random random = GameRandom.random;

    @BeforeEach
    public void setupWorld() {

        JFXPanel jfxPanel = new JFXPanel();

        this.orderedPath = Arrays.asList( //
            Pair.with(0, 0), Pair.with(1, 0), Pair.with(2, 0), Pair.with(3, 0), Pair.with(4, 0), //
            Pair.with(0, 1), Pair.with(4, 1), //
            Pair.with(0, 2), Pair.with(4, 2), //
            Pair.with(0, 3), Pair.with(4, 3), //
            Pair.with(0, 4), Pair.with(1, 4), Pair.with(2, 4), Pair.with(3, 4), Pair.with(4, 4) //
        );

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
        character = world.getCharacter();

    }

    @Test
    public void testTrivialBattle() {
        // Pair<Integer, Integer> enemyPath = Pair.with(4, 0);
        PathPosition enemyPathPosition = new PathPosition(2, this.orderedPath);

        // set the cycle to higher to make slug stronger
        world.getCycle().set(2);
        BasicEnemy enemy = new Slug(enemyPathPosition, world);

        world.getBattleSystem().getEnemies().add(enemy);
        assertFalse(world.getBattleSystem().getEnemies().isEmpty());
        // no enemy defeated because character is not at slug's battle radius
        List<BasicEnemy> defeatedEnemies = world.getBattleSystem().runBattles();
        assertTrue(defeatedEnemies.isEmpty());

        // set cycle back to normal
        world.getCycle().set(1);
        // load one allied soldier
        world.getBattleSystem().loadSoldier();
        List<AlliedSoldier> soldiers = world.getBattleSystem().getSoldiers();
        assertEquals(1, soldiers.size());
        AlliedSoldier currSoldier = soldiers.get(0);

        // set random seed = 0
        random.setSeed(0);
        world.runTickMoves();

        // set random seed to let the enemy to attack soldier
        random.setSeed(5);
        defeatedEnemies = world.getBattleSystem().runBattles();
        // the slug is defeated
        assertEquals(defeatedEnemies.size(), 1);
        // and current soldier is being attacked, so health decreased
        assertEquals(round(currSoldier.getStatus().getCurrentHealthValue(), 1), 14.7);
    }

    @Test
    public void testInfectedBattle() {
        // test zombie can infect allied soldier in battle
        PathPosition enemyPathPosition = new PathPosition(3, this.orderedPath);

        BasicEnemy enemy = new Zombie(enemyPathPosition, world);

        world.getBattleSystem().getEnemies().add(enemy);
        assertFalse(world.getBattleSystem().getEnemies().isEmpty());

        // no enemy defeated because character is not at zombie's battle radius
        List<BasicEnemy> defeatedEnemies = world.getBattleSystem().runBattles();
        assertTrue(defeatedEnemies.isEmpty());

        world.getBattleSystem().loadSoldier();
        List<AlliedSoldier> soldiers = world.getBattleSystem().getSoldiers();
        assertEquals(1, soldiers.size());

        random.setSeed(0);
        world.runTickMoves();

        // only one enemy exist in world
        assertEquals(world.getBattleSystem().getEnemies().size(), 1);
        // set seed to let zombie use special attacks
        random.setSeed(5);
        defeatedEnemies = world.getBattleSystem().runBattles();

        // zombie is killed and currsolder is die due to the infection
        // therefore two enemy was defeated.
        assertEquals(defeatedEnemies.size(), 2);
        assertTrue(character.getStatus().isAlive());
        // test the allied soldier no longer exist.
        assertEquals(0, soldiers.size());
    }

    @Test
    public void testGetKilled() {
        // test character getting killed by enemy
        PathPosition enemyPathPosition = new PathPosition(3, this.orderedPath);

        // set the cycle to higher to make slug stronger
        world.getCycle().set(3);
        BasicEnemy enemy = new Zombie(enemyPathPosition, world);

        world.getBattleSystem().getEnemies().add(enemy);
        assertFalse(world.getBattleSystem().getEnemies().isEmpty());

        // no enemy defeated because character is not at zombie's battle radius
        List<BasicEnemy> defeatedEnemies = world.getBattleSystem().runBattles();
        assertTrue(defeatedEnemies.isEmpty());

        // set random seed = 0
        random.setSeed(0);
        world.runTickMoves();

        random.setSeed(4096);
        // generate a the one ring
        Item item = world.getItemSystem().addUnequippedItem();
        assertEquals(world.getItemSystem().getUnequippedInventory().size(), 1);
        assertEquals(TheOneRing.class, item.getClass());

        // character will lose the fight and one ring will revive character
        // but after revive character still lose to the enemy
        defeatedEnemies = world.getBattleSystem().runBattles();
        assertEquals(defeatedEnemies.size(), 0);
        assertFalse(character.getStatus().isAlive());
        // the one ring is used and destroy
        assertEquals(world.getItemSystem().getUnequippedInventory().size(), 0);
    }

    @Test
    public void testKillSoldier() {
        // test allied soldier was killed by enemy
        PathPosition enemyPathPosition = new PathPosition(3, this.orderedPath);

        world.getCycle().set(20);
        BasicEnemy enemy = new Zombie(enemyPathPosition, world);

        world.getBattleSystem().getEnemies().add(enemy);
        assertFalse(world.getBattleSystem().getEnemies().isEmpty());

        // load allied soldier
        world.getCycle().set(1);
        world.getBattleSystem().loadSoldier();
        List<AlliedSoldier> soldiers = world.getBattleSystem().getSoldiers();
        AlliedSoldier soldier = soldiers.get(0);
        assertEquals(1, soldiers.size());

        // no enemy defeated because character is not at zombie's battle radius
        List<BasicEnemy> defeatedEnemies = world.getBattleSystem().runBattles();
        assertTrue(defeatedEnemies.isEmpty());

        // set random seed = 0
        random.setSeed(0);
        world.runTickMoves();

        random.setSeed(0);
        defeatedEnemies = world.getBattleSystem().runBattles();

        // both soldier and character is killed by zombie
        assertEquals(defeatedEnemies.size(), 0);
        assertFalse(character.getStatus().isAlive());
        assertFalse(soldier.getStatus().isAlive());

    }

    @Test
    public void testSoldierKill() {
        // test soldier killed enemy
        PathPosition enemyPathPosition = new PathPosition(2, this.orderedPath);

        BasicEnemy enemy = new Slug(enemyPathPosition, world);

        world.getBattleSystem().getEnemies().add(enemy);
        assertFalse(world.getBattleSystem().getEnemies().isEmpty());

        // load five soldier to character
        for (int i = 0; i < 5; i++) {
            world.getBattleSystem().loadSoldier();
        }
        List<AlliedSoldier> soldiers = world.getBattleSystem().getSoldiers();
        assertEquals(5, soldiers.size());
        List<BasicEnemy> defeatedEnemies = world.getBattleSystem().runBattles();
        assertTrue(defeatedEnemies.isEmpty());

        // set random seed = 0
        random.setSeed(0);
        world.runTickMoves();

        // set character to no damage
        character.getStatus().setStrength(0);
        // slug is killed by soldier, since character have 0 strength and can not
        // deals damage to soldier
        defeatedEnemies = world.getBattleSystem().runBattles();
        assertEquals(defeatedEnemies.size(), 1);
        assertTrue(character.getStatus().isAlive());

    }

    @Test
    public void testStaffAttack() {
        // test item staff attack to zombie
        PathPosition enemyPathPosition = new PathPosition(3, this.orderedPath);

        // load a very strong zombie to the world
        BasicEnemy enemy = new Zombie(enemyPathPosition, world);
        world.getBattleSystem().getEnemies().add(enemy);
        assertFalse(world.getBattleSystem().getEnemies().isEmpty());

        List<BasicEnemy> defeatedEnemies = world.getBattleSystem().runBattles();
        assertTrue(defeatedEnemies.isEmpty());

        // set random seed = 0
        random.setSeed(0);
        world.runTickMoves();

        random.setSeed(25);
        // generate a staff
        Item item = world.getItemSystem().addUnequippedItem();
        assertEquals(Staff.class, item.getClass());
        world.getItemSystem().equipItemByCoordinates(0, 0, 0, 0);

        // set seed so to trigger staff special attack
        random.setSeed(1);
        defeatedEnemies = world.getBattleSystem().runBattles();

        // it is succeed, because normally character can not kill this zombie, because
        // its status is the status of cycle 11, and character still the startings status.
        // but due to the staff tranced is successfully killed the zombie.
        assertEquals(defeatedEnemies.size(), 1);
        assertTrue(character.getStatus().isAlive());

    }

    @Test
    public void TempSoldierAttack() {
        // test enemy that got trance to soldier's attack
        PathPosition enemyPathPosition = new PathPosition(2, this.orderedPath);

        BasicEnemy enemy_1 = new Zombie(enemyPathPosition, world);
        BasicEnemy enemy_2 = new Zombie(enemyPathPosition, world);
        BasicEnemy enemy_3 = new Slug(enemyPathPosition, world);
        BasicEnemy enemy_4 = new Slug(enemyPathPosition, world);

        world.getBattleSystem().getEnemies().add(enemy_1);
        world.getBattleSystem().getEnemies().add(enemy_2);
        world.getBattleSystem().getEnemies().add(enemy_3);
        world.getBattleSystem().getEnemies().add(enemy_4);

        // set random seed = 0
        random.setSeed(0);
        world.runTickMoves();

        random.setSeed(25);
        // generate a staff and equip staff weapon
        Item item = world.getItemSystem().addUnequippedItem();
        assertEquals(Staff.class, item.getClass());
        world.getItemSystem().equipItemByCoordinates(0, 0, 0, 0);

        // set seed so to trigger staff special attack once and set character to no damage
        GameRandom.random.setSeed(15);
        character.getStatus().setStrength(0);
        List<BasicEnemy> defeatedEnemies = world.getBattleSystem().runBattles();

        // test that all enemy is killed by the temp soldier, since character can
        // only trance enemy but cant deal damage
        assertEquals(defeatedEnemies.size(), 4);
        assertTrue(character.getStatus().isAlive());

    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    @Test 
    public void testElan() {
        PathPosition enemyPathPosition1 = new PathPosition(1, this.orderedPath);
        BasicEnemy elan = new ElanMuske(enemyPathPosition1, world);
        random.setSeed(4099);
        Item item = world.getItemSystem().addUnequippedItem();
        assertEquals(Anduril.class, item.getClass());
        world.getItemSystem().equipItemByCoordinates(0, 0, 0, 0);
        world.getBattleSystem().getEnemies().add(elan);
        



        world.runTickMoves();
        character.getStatus().setCurrentHealth(1000);
        List<BasicEnemy> ElanEscaped =  world.getBattleSystem().runBattles();
        assertEquals(0, ElanEscaped.size());

        
        for (int i = 0; i < 100; i++) {
            world.runTickMoves();
            world.getBattleSystem().runBattles();
        }

        assertEquals(0, world.getBattleSystem().getEnemies().size());
    }

    @Test
    public void testAttackSoldier() {
        for (int i = 0; i < 6; i++) {
            world.getBattleSystem().loadSoldier();
        }
        List<AlliedSoldier> soldiers = world.getBattleSystem().getSoldiers();
        assertTrue(soldiers.size() == 5);
        soldiers.get(0).getStatus().setCurrentHealth(4000);
        PathPosition enemyPathPosition1 = new PathPosition(1, this.orderedPath);
        BasicEnemy vampire1 = new Vampire(enemyPathPosition1, world);
        BasicEnemy vampire2 = new Vampire(enemyPathPosition1, world);
        BasicEnemy doggie = new Doggie(enemyPathPosition1, world);
        BasicEnemy thief = new Thief(enemyPathPosition1, world);
        vampire1.getStatus().setCurrentHealth(1500);
        world.getBattleSystem().getEnemies().add(vampire1);
        world.getBattleSystem().getEnemies().add(vampire2);
        world.getBattleSystem().getEnemies().add(doggie);
        world.getBattleSystem().getEnemies().add(thief);
        thief.getStatus().setCurrentHealth(1500);
        doggie.getStatus().setCurrentHealth(1500);
        assertEquals(world.getBattleSystem().getEnemies().size(), 4);

        character.getStatus().setCurrentHealth(100000);
        random.setSeed(0);
        world.runTickMoves();
        world.getBattleSystem().runBattles();
        assertEquals(0, world.getBattleSystem().getEnemies().size());

    }
    @Test
    public void testSoldierAndElan() {
        for (int i = 0; i < 6; i++) {
            world.getBattleSystem().loadSoldier();
        }
        List<AlliedSoldier> soldiers = world.getBattleSystem().getSoldiers();
        assertTrue(soldiers.size() == 5);
        soldiers.get(0).getStatus().setCurrentHealth(10000);
        

        PathPosition enemyPathPosition1 = new PathPosition(1, this.orderedPath);
        BasicEnemy elan = new ElanMuske(enemyPathPosition1, world);
        elan.getStatus().setCurrentHealth(1500);
        world.getBattleSystem().getEnemies().add(elan);
        character.getStatus().setCurrentHealth(30000);

        BasicEnemy slug1 = new Slug(enemyPathPosition1, world);
        BasicEnemy slug2 = new Slug(enemyPathPosition1, world);
        BasicEnemy slug3 = new Slug(enemyPathPosition1, world);
        BasicEnemy slug4 = new Slug(enemyPathPosition1, world);
        slug4.getStatus().setCurrentHealth(4000);

        BasicEnemy zombie1 = new Zombie(enemyPathPosition1, world);
        world.getBattleSystem().getEnemies().add(slug1);
        world.getBattleSystem().getEnemies().add(slug2);
        world.getBattleSystem().getEnemies().add(slug3);
        world.getBattleSystem().getEnemies().add(slug4);
        world.getBattleSystem().getEnemies().add(zombie1);
        assertEquals(world.getBattleSystem().getEnemies().size(), 6);

        for (int i = 0; i < 100; i++) {
            elan.ChooseAttack(soldiers.get(0), 0);
        }

        random.setSeed(0);
        world.runTickMoves();
        world.getBattleSystem().runBattles();

        assertEquals(0, world.getBattleSystem().getEnemies().size());
    }

    @Test 
    public void testElanSupport() {
        PathPosition enemyPathPosition1 = new PathPosition(1, this.orderedPath);
        BasicEnemy enemy1 = new Slug(enemyPathPosition1, world);
        BasicEnemy enemy2 = new ElanMuske(enemyPathPosition1, world);
        world.getBattleSystem().getEnemies().add(enemy1);
        world.getBattleSystem().getEnemies().add(enemy2);

        character.getStatus().setCurrentHealth(1000);
        world.getBattleSystem().runBattles();
        assertEquals(world.getBattleSystem().getEnemies().size(), 1);
    }



    @Test
    public void testTreeStumpDef() {

        PathPosition enemyPathPosition1 = new PathPosition(1, this.orderedPath);
        PathPosition enemyPathPosition2 = new PathPosition(4, this.orderedPath);

        BasicEnemy enemy1 = new Doggie(enemyPathPosition1, world);
        BasicEnemy enemy2 = new Doggie(enemyPathPosition2, world);

        //equip anduril sword
        random.setSeed(4099);
        Item item = world.getItemSystem().addUnequippedItem();
        assertEquals(Anduril.class, item.getClass());
        world.getItemSystem().equipItemByCoordinates(0, 0, 0, 0);
        world.getBattleSystem().getEnemies().add(enemy1);


        //equip normal shield and set character currentHP to 1000, ensure he can kill doggie
        random.setSeed(2);
        item = world.getItemSystem().addUnequippedItem();
        assertEquals(Shield.class, item.getClass());
        world.getItemSystem().equipItemByCoordinates(0, 0, 3, 0);
        world.runTickMoves();
        character.getStatus().setCurrentHealth(1000);
        world.getBattleSystem().runBattles();
        double health1 = character.getStatus().getCurrentHealthValue(); //mark down character HP "health1" after the battle is finished 

        world.runTickMoves();
        world.runTickMoves();//go to enemy position and battle with enemy

        //equip treeStump and set character currentHP to 1000, ensure he can kill doggie
        world.getBattleSystem().getEnemies().add(enemy2);
        random.setSeed(4098);
        item = world.getItemSystem().addUnequippedItem();
        assertEquals(TreeStump.class, item.getClass());
        world.getItemSystem().equipItemByCoordinates(0, 0, 3, 0);
        world.runTickMoves();        

        character.getStatus().setCurrentHealth(1000);
        world.getBattleSystem().runBattles();
        double health2 = character.getStatus().getCurrentHealthValue();//mark down character HP "health2" after the battle is finished 
        assertTrue(health1<health2);

    }

}
