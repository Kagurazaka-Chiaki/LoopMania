package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.Character;
import unsw.entity.Item;
import unsw.entity.building.HeroCastleBuilding;
import unsw.entity.item.HealthPotion;
import unsw.entity.item.Sword;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.mode.BerserkerMode;
import unsw.mode.Mode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemSystemTest {

    private LoopManiaWorld world;
    private Character character;

    @BeforeEach
    public void setupWorld() {
        JFXPanel jfxPanel = new JFXPanel();
        List<Pair<Integer, Integer>> orderedPath = Arrays.asList( //
            Pair.with(0, 0), Pair.with(1, 0), Pair.with(2, 0), Pair.with(3, 0), Pair.with(4, 0), //
            Pair.with(0, 1), Pair.with(4, 1),//
            Pair.with(0, 2), Pair.with(4, 2),//
            Pair.with(0, 3), Pair.with(4, 3),//
            Pair.with(0, 4), Pair.with(1, 4), Pair.with(2, 4), Pair.with(3, 4), Pair.with(4, 4) //
        );

        this.world = new LoopManiaWorld(7, 7, orderedPath);
        this.world.setHeroCastle(new HeroCastleBuilding(Pair.with(0, 0)));

        // Test for standardMode
        Mode mode = new BerserkerMode();
        world.setMode(mode);
        PathPosition newPosition = new PathPosition(0, orderedPath);
        this.character = new Character(newPosition, world);
        world.setCharacter(character);
    }

    @Test
    public void attributeOfItemIncreaseByCycle() {
        // assert that cycle will increase as character move to heroCastle
        // since there is 16 valid path, so loop 32 times will gets character back to
        // heroCastle twice, so cycle now should be 3
        for (int i = 0; i < 32; i++) {
            world.runTickMoves();
        }
        assertEquals(3, world.getCycle().get());

        GameRandom.random.setSeed(7);

        // assert that the item status is increased,
        // 10 * 3 * (1 + (3 - 1) * 0.03) * 1.05 = 33.39
        Item item = world.getItemSystem().addUnequippedItem();
        assertEquals(33.39, item.getStatus().getStrength());
        assertEquals(0, item.getStatus().getHealth());
        assertEquals(0, item.getStatus().getDefense());
    }

    @Test
    public void unequippedInventory() {
        // check that item is added to unequipped inventory
        GameRandom.random.setSeed(7);

        // if item is added, then the list will have one object
        world.getItemSystem().addUnequippedItem();
        assertEquals(1, world.getItemSystem().getUnequippedInventory().size());
    }

    @Test
    public void fullUnequippedInventory() {
        // check that item will automatically sell and destroyed when inventory
        // is full
        GameRandom.random.setSeed(7);

        // unequippedInventory.get(0) = sword.
        world.getItemSystem().addUnequippedItem();
        Item item = world.getItemSystem().getUnequippedInventory().get(0);
        assertTrue(Sword.class.equals(item.getClass()));

        // inventory can have 16 items
        for (int i = 0; i < 16; i++) {
            world.getItemSystem().addUnequippedItem();
        }

        item = world.getItemSystem().getUnequippedInventory().get(0);
        assertFalse(Sword.class.equals(item.getClass()));
        // sword is sell, so character got 50 golds now
        assertEquals(50, character.getGoldValue());
    }

    @Test
    public void spawnGold() {
        // check that Gold will spawn on path
        GameRandom.random.setSeed(18);
        for (int i = 0; i < 100; i++) {
            world.getItemSystem().possiblySpawnGold();
        }
        // check that only 3 gold can be spawn on path
        assertEquals(3, world.getItemSystem().getGoldOnPath().size());

        for (int i = 0; i < 6; i++) {
            world.runTickMoves();
            world.getItemSystem().pickGold();
        }

        // test that character will pickup Gold
        assertEquals(10, character.getGoldValue());
        // since character has pick up one gold, so gold on path will decrease by one
        assertEquals(2, world.getItemSystem().getGoldOnPath().size());
    }

    @Test
    public void spawnPotion() {
        GameRandom.random.setSeed(130);
        for (int i = 0; i < 100; i++) {
            world.getItemSystem().possiblySpawnPotion();
        }

        // check that only 4 Potion can be spawn
        assertEquals(4, world.getItemSystem().getPotionOnPath().size());

        world.runTickMoves();
        List<HealthPotion> potion = world.getItemSystem().pickPotion();
        world.runTickMoves();
        potion = world.getItemSystem().pickPotion();
        world.runTickMoves();
        potion = world.getItemSystem().pickPotion();
        // test that character will pickup Potion, and potion on path will be destory after pick
        assertEquals(1, potion.size());
        // since character has pick up one potion, so potion on path will decrease by one
        assertEquals(3, world.getItemSystem().getPotionOnPath().size());
    }

    @Test
    public void usePotion() {
        // check that potion can be used
        world.getItemSystem().addHealthPotion();
        // check that is the potion added to inventory
        assertEquals(1, world.getItemSystem().getUnequippedInventory().size());

        // make character low health
        character.getStatus().setCurrentHealth(20);

        // test that character can use potion and heals
        world.getItemSystem().consumeFirstPotion();
        assertEquals(95, character.getStatus().getCurrentHealthValue());

        world.getItemSystem().addUnequippedItem();

        // check that potion is remove and no more Potion can be used
        world.getItemSystem().consumeFirstPotion();
        assertEquals(95, character.getStatus().getCurrentHealthValue());
    }

}
