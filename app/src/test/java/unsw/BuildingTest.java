package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.BasicEnemy;
import unsw.entity.Building;
import unsw.entity.Character;
import unsw.entity.building.HeroCastleBuilding;
import unsw.entity.building.TowerBuilding;
import unsw.entity.building.TrapBuilding;
import unsw.entity.enemy.Slug;
import unsw.entity.enemy.Zombie;
import unsw.loopmania.BuildingSystem;
import unsw.type.BuildingType;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.mode.StandardMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BuildingTest {

    private LoopManiaWorld world;
    private Character character;

    private List<Pair<Integer, Integer>> orderedPath = null;
    private List<Pair<Integer, Integer>> invalidPath = null;

    @BeforeEach
    public void setupWorld() {
        // set up some initial condition for each test
        JFXPanel jfxPanel = new JFXPanel();

        this.orderedPath = Arrays.asList( //
            Pair.with(3, 4), Pair.with(4, 4), Pair.with(4, 5) //
        );

        this.invalidPath = Arrays.asList( //
            Pair.with(1, 1), Pair.with(2, 1), Pair.with(3, 1), //
            Pair.with(1, 2), Pair.with(2, 2), Pair.with(3, 2), //
            Pair.with(1, 3), Pair.with(2, 3), Pair.with(3, 3) //
        );
        this.world = new LoopManiaWorld(7, 7, this.orderedPath);
        StandardMode stdMode = new StandardMode();
        world.setMode(stdMode);
        PathPosition Position1 = new PathPosition(0, orderedPath);
        PathPosition Position2 = new PathPosition(1, orderedPath);
        Slug slug = new Slug(Position1, world);
        Zombie zombie = new Zombie(Position2, world);
        this.world.getBattleSystem().getEnemies().add(slug);
        this.world.getBattleSystem().getEnemies().add(zombie);
        this.world.setHeroCastle(new HeroCastleBuilding(Pair.with(0, 0)));

    }

    @Test
    public void villageExistenceTest() {

        GameRandom.random.setSeed(33);

        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);

        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(BuildingType.VillageBuilding, buildingList.get(0).getBuildingType());
    }

    @Test
    public void villageOnlyOnPathTest() {

        GameRandom.random.setSeed(33);
        // the card is village
        world.loadCard();
        // nearPath situation
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 5);
        // wild situation
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 2, 2);
        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(0, buildingList.toArray().length);
        // set building to another position
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);

        assertEquals(1, buildingList.toArray().length);
    }

    @Test
    public void villageIncreaseHPTest() {

        GameRandom.random.setSeed(33);

        PathPosition pathPosition = new PathPosition(0, orderedPath);
        Character character = new Character(pathPosition, world);
        world.setCharacter(character);
        character.getStatus().setCurrentHealth(200);

        // the card is village
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);
        // After being healed 10% health, current health should be 225
        buildingSystem.checkCharacterPassVillage();

        assertEquals(225, character.getStatus().getCurrentHealthValue());
    }

    @Test
    public void villageIncreaseHPTestFullHp() {

        GameRandom.random.setSeed(33);

        PathPosition pathPosition = new PathPosition(0, orderedPath);
        Character character = new Character(pathPosition, world);
        world.setCharacter(character);
        character.getStatus().setCurrentHealth(249);

        // the card is village
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);
        // After being healed 10% health, current health should be 225
        buildingSystem.checkCharacterPassVillage();

        assertEquals(250, character.getStatus().getCurrentHealthValue());
    }

    @Test
    public void campFireExistenceTest() {
        // when seed is 17, first card will be campfire
        GameRandom.random.setSeed(17);
        // the card is campfire
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 2, 2);

        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(BuildingType.CampfireBuilding, buildingList.get(0).getBuildingType());
    }

    @Test
    public void campFireNotOnPathTest() {
        // when seed is 17, first card will be campfire
        GameRandom.random.setSeed(17);
        // the card is campfire
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);

        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(0, buildingList.toArray().length);

        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 3);
        assertEquals(1, buildingList.toArray().length);
    }

    @Test
    public void campFireBuffTest() {
        // when seed is 17, first card will be campfire
        GameRandom.random.setSeed(17);

        PathPosition pathPosition = new PathPosition(0, this.orderedPath);
        Character character = new Character(pathPosition, world);
        world.setCharacter(character);

        assertFalse(character.getIfBuff());

        // the card is campfire
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 2, 2);
        List<Building> buildingList = buildingSystem.getBuildingEntities();
        // make sure campfire spawned
        assertEquals(BuildingType.CampfireBuilding, buildingList.get(0).getBuildingType());

        // character is in the buff range of the campfire
        buildingSystem.setBuff();

        assertTrue(character.getIfBuff());
    }

    @Test
    public void trapExistenceTest() {
        // when seed is 7, first card will be trap
        GameRandom.random.setSeed(7);

        // the card is trap
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);

        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(BuildingType.TrapBuilding, buildingList.get(0).getBuildingType());
    }

    @Test
    public void trapOnlyOnPathTest() {
        // when seed is 7, first card will be trap
        GameRandom.random.setSeed(7);
        // the card is trap
        world.loadCard();
        // nearPath situation
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 5);
        // wild situation
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 2, 2);
        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(0, buildingList.toArray().length);

        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);

        assertEquals(1, buildingList.toArray().length);
    }

    @Test
    public void trapDamageEnemyTest() {

        GameRandom.random.setSeed(7);
        // when seed is 7, first card will be trap
        // the card is trap
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        TrapBuilding trap = (TrapBuilding) buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);
        BasicEnemy slug = world.getBattleSystem().getEnemies().get(0);

        double previousHp = slug.getStatus().getCurrentHealthValue();
        trap.damageEnemy(slug);
        double currentHp = slug.getStatus().getCurrentHealthValue();
        assertTrue(previousHp > currentHp);
    }

    @Test
    public void trapDamageTest() {

        GameRandom.random.setSeed(7);
        // when seed is 7, first card will be trap
        // the card is trap
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);
        BasicEnemy slug = world.getBattleSystem().getEnemies().get(0);

        slug.getStatus().setCurrentHealth(2);
        List<BasicEnemy> defeatedByTrap = world.getBattleSystem().trapDamage();

        assertEquals(1, defeatedByTrap.size());
    }

    @Test
    public void towerExistenceTest() {

        GameRandom.random.setSeed(4);
        // when seed is 4, first card will be tower
        // the card is tower
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 3);

        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(BuildingType.TowerBuilding, buildingList.get(0).getBuildingType());
    }

    @Test
    public void towerOnlyOnPathNearTest() {

        GameRandom.random.setSeed(4);
        // when seed is 4, first card will be tower
        // the card is tower
        world.loadCard();
        // Path situation
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);
        // wild situation
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 6, 6);
        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(0, buildingList.toArray().length);

        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 3);

        assertEquals(1, buildingList.toArray().length);
    }

    @Test
    public void towerDamageTest() {

        GameRandom.random.setSeed(4);

        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        TowerBuilding tower = (TowerBuilding) buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 3);

        StandardMode stdMode = new StandardMode();
        world.setMode(stdMode);
        PathPosition newPosition = new PathPosition(0, orderedPath);
        Slug slug = new Slug(newPosition, world);

        double previousHp = slug.getStatus().getCurrentHealthValue();
        tower.attackEnemy(slug);
        double currentHp = slug.getStatus().getCurrentHealthValue();
        assertTrue(previousHp > currentHp);
    }

    @Test
    public void towerGetPosTest() {

        GameRandom.random.setSeed(4);

        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        TowerBuilding tower = (TowerBuilding) buildingSystem.convertCardToBuildingByCoordinates(0, 0, 4, 3);
        List<Pair<Integer, Integer>> posList = tower.attackPos(orderedPath);
        List<Pair<Integer, Integer>> ansList = Arrays.asList(Pair.with(4, 4), Pair.with(3, 4), Pair.with(4, 5));

        assertEquals(ansList, posList);

    }

    @Test
    public void towerBattleTest() {

        GameRandom.random.setSeed(4);

        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 3);
        BasicEnemy slug = world.getBattleSystem().getEnemies().get(0);

        slug.getStatus().setCurrentHealth(0.5);
        List<BasicEnemy> defeatedByTower = world.getBattleSystem().towerBattle();
        assertEquals(1, defeatedByTower.size());
    }

    @Test
    public void barracksExistenceTest() {

        GameRandom.random.setSeed(5);
        world.loadCard();

        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);

        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(BuildingType.BarracksBuilding, buildingList.get(0).getBuildingType());
    }

    @Test
    public void barracksOnlyOnPathTest() {

        GameRandom.random.setSeed(5);
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        // Path situation
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 3);
        // wild situation
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 6, 6);
        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(0, buildingList.toArray().length);

        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);

        assertEquals(1, buildingList.toArray().length);
    }

    @Test
    public void zombiePitExistenceTest() {
        // when seed is 8, first card will be zombie pit
        GameRandom.random.setSeed(2);

        // the card is zombie pit
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 2, 4);

        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(BuildingType.ZombiePitBuilding, buildingList.get(0).getBuildingType());
    }

    @Test
    public void zombiePitOnlyNearPathTest() {
        // when seed is 8, first card will be zombie pit
        GameRandom.random.setSeed(2);
        // the card is zombie pit
        world.loadCard();
        // onPath situation
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);
        // wild situation
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 2, 2);
        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(0, buildingList.toArray().length);

        // nearPath situation
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 5);
        assertEquals(1, buildingList.toArray().length);
    }

    @Test
    public void vampireCastleExistenceTest() {
        // when seed is 12, first card will be vampire castle
        GameRandom.random.setSeed(12);

        // the card is vampire castle
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 2, 4);

        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(BuildingType.VampireCastleBuilding, buildingList.get(0).getBuildingType());
    }

    @Test
    public void vampireCastleOnlyNearPathTest() {
        // when seed is 12, first card will be vampire castle
        GameRandom.random.setSeed(12);
        // the card is vampire castle
        world.loadCard();
        // onPath situation
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);
        // wild situation
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 2, 2);
        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(0, buildingList.toArray().length);

        // nearPath situation
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 5);
        assertEquals(1, buildingList.toArray().length);
    }

    @Test
    public void heroCastleInvalidTest() {
        // when seed is 7, first card will be trap
        GameRandom.random.setSeed(7);
        // the card is trap
        world.loadCard();
        // nearPath situation
        BuildingSystem buildingSystem = world.getBuildingSystem();
        List<Building> buildingList = buildingSystem.getBuildingEntities();
        Building heroCastle = world.getHeroCastle();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, heroCastle.getX(), heroCastle.getY());

        assertEquals(0, buildingList.toArray().length);
    }

//    @Test
//    public void seedTest() {
//        // when seed is 4, first card will be campfire
////        List<Card> cardList = world.getCardEntities();
//
//        List<Card> cardList = null;
//        int i = 0;
//
//        while (i < 1000) {
////            this.world = new LoopManiaWorld(7, 7, this.orderedPath);
//            GameRandom.random.setSeed(i);
//            world.loadCard();
//            cardList = world.getCardEntities();
//            if (!cardList.get(0).getClass().equals(VampireCastleCard.class)) {
//                cardList.remove(0);
//            } else {
//                break;
//            }
//            i++;
//        }
//        assertEquals(1, i);
//    }

    @Test
    public void magicShopExistenceTest() {
        // when seed is 4, first card will be campfire
        GameRandom.random.setSeed(53);

        // the card is village
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);

        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(BuildingType.MagicShopBuilding, buildingList.get(0).getBuildingType());
    }

    @Test
    public void magicShopOnlyOnPathTest() {
        // when seed is 4, first card will be campfire
        GameRandom.random.setSeed(53);
        // the card is village
        world.loadCard();
        // nearPath situation
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 5);
        // wild situation
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 2, 2);
        List<Building> buildingList = buildingSystem.getBuildingEntities();

        assertEquals(0, buildingList.toArray().length);
        // set building to another position
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);

        assertEquals(1, buildingList.toArray().length);
    }

    @Test
    public void magicShopFuncTest() {
        // when seed is 53, first card will be campfire
        GameRandom.random.setSeed(53);

        PathPosition pathPosition = new PathPosition(0, orderedPath);
        Character character = new Character(pathPosition, world);
        world.setCharacter(character);

        // the card is magic shop
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        assertFalse(buildingSystem.checkCharacterPassMagicShop());
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 4);
        // it should return true because hero is in the magic shop
        assertTrue(buildingSystem.checkCharacterPassMagicShop());
    }

}
