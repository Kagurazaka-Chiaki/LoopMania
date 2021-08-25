package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.BasicEnemy;
import unsw.entity.Building;
import unsw.entity.Character;
import unsw.entity.Item;
import unsw.entity.building.HeroCastleBuilding;
import unsw.entity.enemy.Slug;
import unsw.entity.enemy.Thief;
import unsw.entity.enemy.Vampire;
import unsw.entity.enemy.Zombie;
import unsw.entity.item.Stake;
import unsw.loopmania.BattleSystem;
import unsw.loopmania.BuildingSystem;
import unsw.type.BuildingType;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.mode.BerserkerMode;
import unsw.mode.ConfusingMode;
import unsw.mode.StandardMode;
import unsw.mode.SurvivalMode;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class is a dummy class demonstrating how to setup tests for the project
 * you should setup additional test classes in a similar fashion, aiming to achieve high coverage.
 * A clickable "Run Test" link should appear if you have installed the Java Extension Pack properly.
 */
public class BasicEnemyTest {

    @BeforeEach
    public void createPane() {
        JFXPanel jfxPanel = new JFXPanel();
    }

    @Test
    public void enemyUpgradeWithDifferentMode() {
        // test the enemy will upgrade differently for different mode
        // where standard has difficult constant of 1.0
        // while other will be 1.05
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(3, 4);
        Pair<Integer, Integer> p2 = new Pair<Integer, Integer>(4, 4);
        Pair<Integer, Integer> p3 = new Pair<Integer, Integer>(4, 5);
        path.add(p1);
        path.add(p2);
        path.add(p3);

        LoopManiaWorld world = new LoopManiaWorld(7, 7, path);

        // Test for standardMode
        StandardMode stdMode = new StandardMode();
        world.setMode(stdMode);
        PathPosition newPosition = new PathPosition(1, path);
        Slug slug = new Slug(newPosition, world);
        assertEquals(20.0, round(slug.getStatus().getCurrentHealthValue(), 2));

        // Test for SurvivalMode
        world.setMode(new SurvivalMode());
        Zombie zombie = new Zombie(newPosition, world);
        assertEquals(42.0, zombie.getStatus().getHealth());

        // Test for BerserkerMode
        world.setMode(new BerserkerMode());
        Vampire vampire = new Vampire(newPosition, world);
        assertEquals(63, vampire.getStatus().getHealth());

        // Test for Confusing Mode
        world.setMode(new ConfusingMode());
        vampire = new Vampire(newPosition, world);
        assertEquals(63, vampire.getStatus().getHealth());

    }

    @Test
    public void EnemyNormalAttack() {
        // test the enemy's normal attack to character
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(3, 4);
        Pair<Integer, Integer> p2 = new Pair<Integer, Integer>(4, 4);
        Pair<Integer, Integer> p3 = new Pair<Integer, Integer>(4, 5);
        path.add(p1);
        path.add(p2);
        path.add(p3);

        LoopManiaWorld world = new LoopManiaWorld(7, 7, path);
        StandardMode stdMode = new StandardMode();
        world.setMode(stdMode);
        PathPosition newPosition = new PathPosition(1, path);
        Zombie zombie = new Zombie(newPosition, world);

        // test the normal attack of zombie to character, zombie deal 12 damage
        GameRandom.random.setSeed(2);
        Character character = new Character(newPosition, world);
        world.setCharacter(character);
        zombie.ChooseAttack(character);
        double characterMaxHealth = character.getStatus().getHealth();
        double zombieDamage = zombie.getStatus().getStrength() - character.getStatus().getDefense();
        double currHealth = characterMaxHealth - zombieDamage;
        assertEquals(currHealth, character.getStatus().getCurrentHealthValue());
        assertEquals(238, character.getStatus().getCurrentHealthValue());

        // test the normal attack of slug to character, zombie deal 3 damage
        Slug slug = new Slug(newPosition, world);
        slug.ChooseAttack(character);
        double slugDamage = slug.getStatus().getStrength() - character.getStatus().getDefense();
        assertEquals(235, character.getStatus().getCurrentHealthValue());
        assertEquals(currHealth - slugDamage, character.getStatus().getCurrentHealthValue());

        // test the normal attack of vampire to character, vampire deals 8 damage
        currHealth = character.getStatus().getCurrentHealthValue();
        Vampire vampire = new Vampire(newPosition, world);
        vampire.ChooseAttack(character);
        double vampireDamage = vampire.getStatus().getStrength() - character.getStatus().getDefense();
        currHealth = currHealth - vampireDamage;
        assertEquals(227, character.getStatus().getCurrentHealthValue());
        assertEquals(currHealth, character.getStatus().getCurrentHealthValue());

    }

    @Test
    public void EnemySpecialAttack() {
        // test the special attack damage of vampire and zombie
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(3, 4);
        Pair<Integer, Integer> p2 = new Pair<Integer, Integer>(4, 4);
        Pair<Integer, Integer> p3 = new Pair<Integer, Integer>(4, 5);
        path.add(p1);
        path.add(p2);
        path.add(p3);

        LoopManiaWorld world = new LoopManiaWorld(7, 7, path);
        StandardMode stdMode = new StandardMode();
        world.setMode(stdMode);
        PathPosition newPosition = new PathPosition(1, path);

        Character character = new Character(newPosition, world);
        world.setCharacter(character);
        Vampire vampire = new Vampire(newPosition, world);
        GameRandom.random.setSeed(3);
        // First attack would be special, deals 12.8 damage
        vampire.ChooseAttack(character);
        assertEquals(237.2, round(character.getStatus().getCurrentHealthValue(), 1));
    }

    @Test
    public void TestEnemyGeneration() {
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(3, 4);
        Pair<Integer, Integer> p2 = new Pair<Integer, Integer>(4, 4);
        Pair<Integer, Integer> p3 = new Pair<Integer, Integer>(4, 5);
        Pair<Integer, Integer> p4 = new Pair<Integer, Integer>(4, 6);
        Pair<Integer, Integer> p5 = new Pair<Integer, Integer>(4, 7);
        Pair<Integer, Integer> p6 = new Pair<Integer, Integer>(4, 8);
        Pair<Integer, Integer> p7 = new Pair<Integer, Integer>(4, 9);
        path.add(p1);
        path.add(p2);
        path.add(p3);
        path.add(p4);
        path.add(p5);
        path.add(p6);
        path.add(p7);

        LoopManiaWorld world = new LoopManiaWorld(7, 7, path);
        PathPosition newPosition = new PathPosition(1, path);
        StandardMode stdMode = new StandardMode();
        world.setMode(stdMode);
        GameRandom.random.setSeed(2);
        // next int will return 1, then 0, then 1

        Character character = new Character(newPosition, world);
        world.setCharacter(character);

        // random will returns 1, so no enemy is generated
        List<BasicEnemy> enemies = world.getBattleSystem().possiblySpawnEnemies();
        assertEquals(0, enemies.size());

        // test that enemy can be generated
        enemies = world.getBattleSystem().possiblySpawnEnemies();
        assertNotEquals(0, enemies.size());
        assertEquals(1, enemies.size());

        // check that slug is generated.
        BasicEnemy currEnemy = enemies.get(0);
        Slug slug = new Slug(newPosition, world);
        assertEquals(slug.getClass(), currEnemy.getClass());
    }

    @Test
    public void TestMoveEnemy() {
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(3, 4);
        Pair<Integer, Integer> p2 = new Pair<Integer, Integer>(4, 4);
        Pair<Integer, Integer> p3 = new Pair<Integer, Integer>(4, 5);
        Pair<Integer, Integer> p4 = new Pair<Integer, Integer>(4, 6);
        Pair<Integer, Integer> p5 = new Pair<Integer, Integer>(4, 7);
        Pair<Integer, Integer> p6 = new Pair<Integer, Integer>(4, 8);
        Pair<Integer, Integer> p7 = new Pair<Integer, Integer>(4, 9);
        path.add(p1);
        path.add(p2);
        path.add(p3);
        path.add(p4);
        path.add(p5);
        path.add(p6);
        path.add(p7);

        LoopManiaWorld world = new LoopManiaWorld(7, 7, path);
        PathPosition newPosition = new PathPosition(1, path);
        StandardMode stdMode = new StandardMode();
        world.setMode(stdMode);
        GameRandom.random.setSeed(2);
        // next int will return 1, then 0, then 1

        Character character = new Character(newPosition, world);
        world.setCharacter(character);
        HeroCastleBuilding heroCastle = new HeroCastleBuilding(p7);
        world.setHeroCastle(heroCastle);

        world.getBattleSystem().possiblySpawnEnemies();
        List<BasicEnemy> enemies = world.getBattleSystem().possiblySpawnEnemies();
        BasicEnemy currEnemy = enemies.get(0);

        // seed one, nextint(4) returns 2, then 1, then 3
        GameRandom.random.setSeed(1);
        PathPosition currpPosition = currEnemy.getPathPosition();
        int numOfPosition = currpPosition.getCurrentPositionInPath();
        // first Tick, the enemy would not move since nextint(4) is bigger than 1
        world.runTickMoves();
        assertEquals(numOfPosition, currEnemy.getPathPosition().getCurrentPositionInPath());

        // second Tick, the enemy would move down
        world.runTickMoves();
        assertNotEquals(numOfPosition, currEnemy.getPathPosition().getCurrentPositionInPath());
    }

    @Test
    public void CharacterAttack() {
        // test on character attack vampire with stake
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(3, 4);
        Pair<Integer, Integer> p2 = new Pair<Integer, Integer>(4, 4);
        Pair<Integer, Integer> p3 = new Pair<Integer, Integer>(4, 5);
        path.add(p1);
        path.add(p2);
        path.add(p3);

        LoopManiaWorld world = new LoopManiaWorld(7, 7, path);
        StandardMode stdMode = new StandardMode();
        world.setMode(stdMode);
        PathPosition newPosition = new PathPosition(1, path);

        // generate a vampire
        Character character = new Character(newPosition, world);
        world.setCharacter(character);
        Vampire vampire = new Vampire(newPosition, world);
        assertEquals(60, vampire.getStatus().getHealth());

        GameRandom.random.setSeed(8);

        // check that chracter equip stake
        Item stake = world.getItemSystem().addUnequippedItem();
        assertTrue(stake instanceof Stake);
        world.getItemSystem().equipItemByCoordinates(0, 0, 0, 0);
        assertEquals(20, character.getStatus().getStrength());

        // check that character do the correct damge to vampire,
        // where stake can ignore vampire's defense
        world.getBattleSystem().getBattleEnemies().add(vampire);
        character.chooseAttack();
        assertEquals(40, vampire.getStatus().getCurrentHealthValue());
    }

    @Test
    public void possibleSpawnEnemiesTest() {
        List<Pair<Integer, Integer>> path = new ArrayList<>();

        Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(3, 4);
        Pair<Integer, Integer> p2 = new Pair<Integer, Integer>(4, 4);
        Pair<Integer, Integer> p3 = new Pair<Integer, Integer>(4, 5);
        Pair<Integer, Integer> p4 = new Pair<Integer, Integer>(4, 6);
        Pair<Integer, Integer> p5 = new Pair<Integer, Integer>(4, 7);
        Pair<Integer, Integer> p6 = new Pair<Integer, Integer>(4, 8);
        Pair<Integer, Integer> p7 = new Pair<Integer, Integer>(4, 9);
        path.add(p1);
        path.add(p2);
        path.add(p3);
        path.add(p4);
        path.add(p5);
        path.add(p6);
        path.add(p7);

        LoopManiaWorld world = new LoopManiaWorld(7, 7, path);
        StandardMode stdMode = new StandardMode();
        world.setMode(stdMode);
        PathPosition pathPosition = new PathPosition(0, path);
        world.setCharacter(new Character(pathPosition, world));
        HeroCastleBuilding heroCastle = new HeroCastleBuilding(p7);
        world.setHeroCastle(heroCastle);

        GameRandom.random.setSeed(1);
        // check that only 3 slug can be spawn on map
        world.getBattleSystem().possiblySpawnEnemies();
        assertEquals(0, world.getBattleSystem().getEnemies().size());
        world.getBattleSystem().possiblySpawnEnemies();
        assertEquals(1, world.getBattleSystem().getEnemies().size());
        world.getBattleSystem().possiblySpawnEnemies();
        assertEquals(2, world.getBattleSystem().getEnemies().size());
        world.getBattleSystem().possiblySpawnEnemies();
        world.getBattleSystem().possiblySpawnEnemies();
        world.getBattleSystem().possiblySpawnEnemies();
        world.getBattleSystem().possiblySpawnEnemies();
        assertEquals(3, world.getBattleSystem().getEnemies().size());
        world.getBattleSystem().possiblySpawnEnemies();
        world.getBattleSystem().possiblySpawnEnemies();
        assertEquals(3, world.getBattleSystem().getEnemies().size());

        // check that character can defeat enemy
        world.runTickMoves();
        world.runTickMoves();
        List<BasicEnemy> defeatedEnemies = world.getBattleSystem().runBattles();
        assertEquals(defeatedEnemies.size(), 1);

        // check that character can defeat enemy
        world.runTickMoves();
        List<BasicEnemy> anotherDefeatedEnemies = world.getBattleSystem().runBattles();
        assertEquals(anotherDefeatedEnemies.size(), 2);
        assertTrue(Slug.class.equals(defeatedEnemies.get(0).getClass()));
        // all slug is killed
        assertEquals(0, world.getBattleSystem().getEnemies().size());
    }


    @Test
    public void generateBoss() {
        List<Pair<Integer, Integer>> path = new ArrayList<>();

        Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(3, 4);
        Pair<Integer, Integer> p2 = new Pair<Integer, Integer>(4, 4);
        Pair<Integer, Integer> p3 = new Pair<Integer, Integer>(4, 5);
        Pair<Integer, Integer> p4 = new Pair<Integer, Integer>(4, 6);
        Pair<Integer, Integer> p5 = new Pair<Integer, Integer>(4, 7);
        Pair<Integer, Integer> p6 = new Pair<Integer, Integer>(4, 8);
        Pair<Integer, Integer> p7 = new Pair<Integer, Integer>(4, 9);
        path.add(p1);
        path.add(p2);
        path.add(p3);
        path.add(p4);
        path.add(p5);
        path.add(p6);
        path.add(p7);

        LoopManiaWorld world = new LoopManiaWorld(7, 7, path);
        StandardMode stdMode = new StandardMode();
        world.setMode(stdMode);
        PathPosition pathPosition = new PathPosition(0, path);
        world.setCharacter(new Character(pathPosition, world));
        HeroCastleBuilding heroCastle = new HeroCastleBuilding(p7);
        world.setHeroCastle(heroCastle);
        
        BattleSystem btSys = world.getBattleSystem();
        List<BasicEnemy> boss = btSys.generateBoss();
        assertEquals(boss.size(), 0);
        world.getCycle().set(21);
        btSys.generateBoss();
        assertEquals(btSys.getEnemies().size(), 1);

        world.getCharacter().upgrade();
        world.getCharacter().getEXP().set(10000);
        world.getCycle().set(41);
        btSys.generateBoss();
        assertEquals(btSys.getEnemies().size(), 2);

        world.getCycle().set(44);
        boss = btSys.generateBoss();
        assertEquals(btSys.getEnemies().size(), 2);

    }

    
    @Test
    public void thiefTest() {
        // test thief stealing behaviour
        List<Pair<Integer, Integer>> path = new ArrayList<>();

        Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(3, 4);
        Pair<Integer, Integer> p2 = new Pair<Integer, Integer>(4, 4);
        Pair<Integer, Integer> p3 = new Pair<Integer, Integer>(4, 5);
        Pair<Integer, Integer> p4 = new Pair<Integer, Integer>(4, 6);
        Pair<Integer, Integer> p5 = new Pair<Integer, Integer>(4, 7);
        Pair<Integer, Integer> p6 = new Pair<Integer, Integer>(4, 8);
        Pair<Integer, Integer> p7 = new Pair<Integer, Integer>(4, 9);
        path.add(p1);
        path.add(p2);
        path.add(p3);
        path.add(p4);
        path.add(p5);
        path.add(p6);
        path.add(p7);

        LoopManiaWorld world = new LoopManiaWorld(7, 7, path);
        world.setCycle(10);
        StandardMode stdMode = new StandardMode();
        world.setMode(stdMode);
        GameRandom.random.setSeed(1);
        PathPosition pathPosition = new PathPosition(0, path);
        Character character = new Character(pathPosition, world);
        world.setCharacter(character);
        HeroCastleBuilding heroCastle = new HeroCastleBuilding(p7);
        world.setHeroCastle(heroCastle);



        // character have 1000 gold and two item
        character.setGold(1000);
        world.getItemSystem().addUnequippedItem();
        world.getItemSystem().addUnequippedItem();
        assertEquals(2, world.getItemSystem().getUnequippedInventory().size());

        // spawn a thief
        world.getBattleSystem().possiblySpawnEnemies();
        assertEquals(1, world.getBattleSystem().getEnemies().size());
        assertTrue(Thief.class.equals(world.getBattleSystem().getEnemies().get(0).getClass()));

        world.runTickMoves();
        world.runTickMoves();

        // character lose to thief and two item with 111 gold got stolen
        List<BasicEnemy> defeatedEnemies = world.getBattleSystem().runBattles();
        assertFalse(character.getStatus().isAlive());
        assertEquals(0, world.getItemSystem().getUnequippedInventory().size());
        assertEquals(858, character.getGoldValue());
        assertEquals(defeatedEnemies.size(), 0);
    }

    @Test
    public void generateStrongEnemiesTest() {
        // test spawning vampire and zombie

        // initialize world
        List<Pair<Integer, Integer>> path = new ArrayList<>();
        Pair<Integer, Integer> p1 = new Pair<Integer, Integer>(0, 0);
        Pair<Integer, Integer> p2 = new Pair<Integer, Integer>(0, 1);
        path.add(p1);
        path.add(p2);
        LoopManiaWorld world = new LoopManiaWorld(2, 2, path);
        StandardMode stdMode = new StandardMode();
        world.setMode(stdMode);
        GameRandom.random.setSeed(1);
        PathPosition pathPosition = new PathPosition(0, path);
        world.setCharacter(new Character(pathPosition, world));
        HeroCastleBuilding heroCastle = new HeroCastleBuilding(p2);
        world.setHeroCastle(heroCastle);
        assertEquals(1, world.getCycle().get());
        world.runTickMoves();
        assertEquals(2, world.getCycle().get());

        // check that no enemy was spawned
        world.getBattleSystem().gengrateStrongEnemies();
        assertEquals(0, world.getBattleSystem().getEnemies().size());

        world.runTickMoves();
        world.runTickMoves();
        assertEquals(3, world.getCycle().get());

        GameRandom.random.setSeed(2);
        // the card is zombie pit
        world.loadCard();

        // convert zombie card to building
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 1, 0);
        List<Building> buildingList = buildingSystem.getBuildingEntities();

        // check that building is place on map
        assertEquals(BuildingType.ZombiePitBuilding, buildingList.get(0).getBuildingType());

        // zombie will not spawn because is not past 3 cycles yet
        world.getBattleSystem().gengrateStrongEnemies();
        assertEquals(0, world.getBattleSystem().getEnemies().size());

        world.runTickMoves();
        world.runTickMoves();
        assertEquals(4, world.getCycle().get());

        // check that it spawn two zombie
        world.getBattleSystem().gengrateStrongEnemies();
        assertEquals(1, world.getBattleSystem().getEnemies().size());
        world.getBattleSystem().gengrateStrongEnemies();
        assertEquals(2, world.getBattleSystem().getEnemies().size());

        GameRandom.random.setSeed(12);
        // the card is vampire castle
        world.loadCard();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 1, 1);

        // check that vampire building is place
        buildingList = buildingSystem.getBuildingEntities();
        assertEquals(BuildingType.VampireCastleBuilding, buildingList.get(1).getBuildingType());

        // since zombie pit is still here, so one zombie is spawned
        // but not vampire spawn because is not at 5 cycles yet
        world.getBattleSystem().gengrateStrongEnemies();
        assertEquals(3, world.getBattleSystem().getEnemies().size());

        world.runTickMoves();
        world.runTickMoves();
        assertEquals(5, world.getCycle().get());

        // check that a vampire is spawned at cycle 5
        world.getBattleSystem().gengrateStrongEnemies();
        assertEquals(5, world.getBattleSystem().getEnemies().size());
        BasicEnemy lastEnemy = world.getBattleSystem().getEnemies().get(4);
        assertTrue(Vampire.class.equals(lastEnemy.getClass()));
    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
