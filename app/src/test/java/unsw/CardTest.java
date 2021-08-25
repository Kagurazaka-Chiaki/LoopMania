package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.Building;
import unsw.entity.Card;
import unsw.entity.Character;
import unsw.entity.building.HeroCastleBuilding;
import unsw.loopmania.BuildingSystem;
import unsw.loopmania.LoopManiaWorld;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardTest {

    private LoopManiaWorld world;
    private Character character;

    private List<Pair<Integer, Integer>> orderedPath = null;
    private List<Pair<Integer, Integer>> invalidPath = null;

    // @BeforeEach
    // public void createPane() {
    //     JFXPanel jfxPanel = new JFXPanel();
    // }

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
        this.character = new Character(null, world);
        this.world.setHeroCastle(new HeroCastleBuilding(Pair.with(0, 0)));

        // System.err.println(world.getHeroCastle());
        // Pair<Integer, Integer> invalidPos = this.invalidPath.get(random.nextInt(invalidPath.size()));
        // Pair<Integer, Integer> validPos = this.orderedPath.get(random.nextInt(orderedPath.size()));
        // Card invalidCard = new ZombiePitCard(invalidPos);
        // Card validCard = new ZombiePitCard(validPos);

    }

    @Test
    public void testZombieCard() {
        // Zombie Card
        GameRandom.random.setSeed(2);
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        // Pair<Integer, Integer> invalidPos = this.invalidPath.get(random.nextInt(invalidPath.size()));
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 2, 2);

        List<Building> buildingList = buildingSystem.getBuildingEntities();
        assertTrue(buildingList.isEmpty());

        // Pair<Integer, Integer> validPos = this.orderedPath.get(random.nextInt(orderedPath.size()));
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 1, 2);

        List<Card> cardList = world.getCardEntities();
        assertTrue(cardList.isEmpty());
    }

    @Test
    public void testTowerCard() {
        // TowerCard
        GameRandom.random.setSeed(4);
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        // Pair<Integer, Integer> invalidPos = this.invalidPath.get(random.nextInt(invalidPath.size()));
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 2, 2);

        List<Building> buildingList = buildingSystem.getBuildingEntities();
        assertTrue(buildingList.isEmpty());

        // Pair<Integer, Integer> validPos = this.orderedPath.get(random.nextInt(orderedPath.size()));
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 1, 2);

        List<Card> cardList = world.getCardEntities();
        assertTrue(cardList.isEmpty());
    }

    @Test
    public void testBarracksCard() {
        // BarracksCard
        GameRandom.random.setSeed(5);
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        // Pair<Integer, Integer> invalidPos = this.invalidPath.get(random.nextInt(invalidPath.size()));
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 2, 2);

        List<Building> buildingList = buildingSystem.getBuildingEntities();
        assertTrue(buildingList.isEmpty());

        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 1, 2);

        buildingList = buildingSystem.getBuildingEntities();
        assertTrue(buildingList.isEmpty());

        // Pair<Integer, Integer> validPos = this.orderedPath.get(random.nextInt(orderedPath.size()));
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 4, 4);

        List<Card> cardList = world.getCardEntities();
        assertTrue(cardList.isEmpty());
    }
    //
    //    @Test
    //    public void testHeroCastleCard() {
    //        Card heroCastleCard = new HeroCastleCard(Pair.with(0, 0));
    //
    //        world.getCardEntities().add(heroCastleCard);
    //
    //        // Pair<Integer, Integer> invalidPos = this.invalidPath.get(random.nextInt(invalidPath.size()));
    //        world.convertCardToBuildingByCoordinates(0, 0, 2, 2);
    //
    //        List<Building> buildingList = world.getBuildingEntities();
    //        assertTrue(buildingList.isEmpty());
    //
    //        // Pair<Integer, Integer> validPos = this.orderedPath.get(random.nextInt(orderedPath.size()));
    //        world.convertCardToBuildingByCoordinates(0, 0, 0, 0);
    //
    //        List<Card> cardList = world.getCardEntities();
    //        assertFalse(cardList.isEmpty());
    //    }

    //    @Test
    //    public void testDefaultError() {
    //        Card card = new DefaultCard(Pair.with(0, 0));
    //
    //        world.getCardEntities().add(card);
    //
    //        // Pair<Integer, Integer> invalidPos = this.invalidPath.get(random.nextInt(invalidPath.size()));
    //        world.convertCardToBuildingByCoordinates(0, 0, 2, 2);
    //
    //        List<Building> buildingList = world.getBuildingEntities();
    //        assertTrue(buildingList.isEmpty());
    //
    //        // Pair<Integer, Integer> validPos = this.orderedPath.get(random.nextInt(orderedPath.size()));
    //        world.convertCardToBuildingByCoordinates(0, 0, 1, 2);
    //
    //        List<Card> cardList = world.getCardEntities();
    //        assertFalse(cardList.isEmpty());
    //
    //    }

}
