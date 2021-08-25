package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.Character;
import unsw.entity.building.HeroCastleBuilding;
import unsw.entity.enemy.Doggie;
import unsw.entity.enemy.ElanMuske;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.mode.StandardMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoggieCoinTest {
    private LoopManiaWorld world;
    private Character character;
    private List<Pair<Integer, Integer>> orderedPath = null;

    @BeforeEach
    public void setupWorld() {
        JFXPanel jfxPanel = new JFXPanel();
        this.orderedPath = Arrays.asList( //
            Pair.with(3, 4), Pair.with(4, 4), Pair.with(4, 5) //
        );

        PathPosition newPosition = new PathPosition(0, orderedPath);
        this.world = new LoopManiaWorld(7, 7, this.orderedPath);
        this.world.setMode(new StandardMode());
        this.character = new Character(newPosition, world);
        world.setCharacter(character);
        this.world.setHeroCastle(new HeroCastleBuilding(Pair.with(0, 0)));
    }

    @Test
    public void testDoggieCoinPriceWithoutElan() {
        // test the doggie coin price is always in the range of [250,500)
        // when elan muske is not spawned
        GameRandom.random.setSeed(4);

        assertEquals(character.getDoggieCoinValue(), 0);
        // add one doggie Coin to character
        character.addDoggieCoin();
        // check that coin is successfully added
        assertEquals(character.getDoggieCoinValue(), 1);

        // test 3 times and check is it in the range, and check will the price change
        world.runTickMoves();
        assertEquals(character.getDoggiePriceValue(), 362);

        world.runTickMoves();
        assertEquals(character.getDoggiePriceValue(), 452);

        world.runTickMoves();
        assertEquals(character.getDoggiePriceValue(), 303);
    }

    @Test
    public void testDoggieCoinPriceWithElan() {
        // test that doggie coin price will increase in the range of (0, 500]
        // when elan muske is spawned and alive
        
        // spawn elan and add elan to the world
        PathPosition newPosition = new PathPosition(0, orderedPath);
        ElanMuske elan = new ElanMuske(newPosition, world);
        world.getBattleSystem().getEnemies().add(elan);
        // add one doggie Coin to character
        character.addDoggieCoin();
        // check that coin is successfully added
        assertEquals(character.getDoggieCoinValue(), 1);

        // seed the random to 4, and the movement of enemy will use one random seed number
        // so the price should increase by 453, 59, 106
        GameRandom.random.setSeed(4);
        // so after the first move the coin should worth 703 gold
        world.runTickMoves();
        assertEquals(character.getDoggiePriceValue(), 703);
        // after the second move the coin should now worth 762 gold
        world.runTickMoves();
        assertEquals(character.getDoggiePriceValue(), 762);
        // after the third move the coin should now worth 868 gold
        world.runTickMoves();
        assertEquals(character.getDoggiePriceValue(), 868);
    }

    @Test
    public void testDoggieCoinPriceWhenElanDie() {
        // test when elan muske is killed, the doggie coin price will drop
        // to the range of (0, 250] until the next Elan Muske spawned
        
        // spawn elan and add elan to the world
        PathPosition newPosition = new PathPosition(0, orderedPath);
        ElanMuske elan = new ElanMuske(newPosition, world);
        world.getBattleSystem().getEnemies().add(elan);
        // add one doggie Coin to character
        character.addDoggieCoin();
        // check that coin is successfully added
        assertEquals(character.getDoggieCoinValue(), 1);

        GameRandom.random.setSeed(4);
        // upgrade character so it can win over elan
        for (int i = 0; i < 10; i++) {
            character.upgrade();
        }
        world.getBattleSystem().runBattles();
        // check that elan muske is killed
        assertEquals(world.getElanMuskeKilledValue(), 1);

        // seed the random to 4, and check that the price is always
        // in the range of (0, 250]
        world.runTickMoves();
        assertEquals(character.getDoggiePriceValue(), 106);
        // after the second move the coin should now worth 762 gold
        world.runTickMoves();
        assertEquals(character.getDoggiePriceValue(), 162);
        // after the third move the coin should now worth 868 gold
        world.runTickMoves();
        assertEquals(character.getDoggiePriceValue(), 97);
    }

    @Test
    public void testKillingDoggieGiveDoggieCoin() {
        // test that when character killed doggie it will give doggie coin
        
        // spawn doggie and add doggie to the world
        PathPosition newPosition = new PathPosition(0, orderedPath);
        Doggie doggie = new Doggie(newPosition, world);
        world.getBattleSystem().getEnemies().add(doggie);
        // check that character have no doggie coin
        assertEquals(character.getDoggieCoinValue(), 0);

        GameRandom.random.setSeed(4);
        // upgrade character so it can win over doggie
        for (int i = 0; i < 10; i++) {
            character.upgrade();
        }
        world.getBattleSystem().runBattles();
        // check that doggie is killed
        assertEquals(world.getDoggieKilledValue(), 1);
        // check that character receive a doggie coin
        assertEquals(character.getDoggieCoinValue(), 1);

    }
}
