package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.Card;
import unsw.entity.Character;
import unsw.entity.building.HeroCastleBuilding;
import unsw.entity.enemy.Slug;
import unsw.entity.enemy.Zombie;
import unsw.loopmania.BuildingSystem;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.mode.StandardMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoopManiaWorldTest {

    private LoopManiaWorld world;

    private List<Pair<Integer, Integer>> orderedPath = null;
    private List<Pair<Integer, Integer>> invalidPath = null;
    //    private List<Pair<Integer, Integer>> invalidPath = null;

    @BeforeEach
    public void setupWorld() {
        JFXPanel jfxPanel = new JFXPanel();

        this.orderedPath = Arrays.asList( //
            Pair.with(3, 4), Pair.with(4, 4), Pair.with(4, 5) //
        );

        this.invalidPath = Arrays.asList( //
            Pair.with(1, 1), Pair.with(2, 1), Pair.with(3, 1),  //
            Pair.with(1, 2), Pair.with(2, 2), Pair.with(3, 2),  //
            Pair.with(1, 3), Pair.with(2, 3), Pair.with(3, 3)   //
        );
        this.world = new LoopManiaWorld(7, 7, this.orderedPath);
        Character character = new Character(new PathPosition(0, orderedPath), world);
        world.setCharacter(character);
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
    public void removeCardTest() {

        for (int i = 0; i < world.getWidth() + 1; i++) {
            world.loadCard();
        }
        List<Card> cardentities = world.getCardEntities();
        assertTrue(cardentities.size() == world.getWidth());
    }

    @Test
    public void checkValidPosTest() {

        GameRandom.random.setSeed(10);
        // when seed is 10, first card will be tower
        // the card is tower
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 3, 3);

        assertFalse(buildingSystem.checkValidPos(0, 0, 3, 3));
    }

}
