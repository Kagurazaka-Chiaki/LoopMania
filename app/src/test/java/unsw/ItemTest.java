package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.Character;
import unsw.entity.Item;
import unsw.entity.building.HeroCastleBuilding;
import unsw.entity.enemy.Vampire;
import unsw.entity.item.*;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.mode.StandardMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ItemTest {
    private LoopManiaWorld world;
    private List<Pair<Integer, Integer>> orderedPath = null;

    @BeforeEach
    public void setupWorld() {
        JFXPanel jfxPanel = new JFXPanel();
        this.orderedPath = Arrays.asList( //
            Pair.with(3, 4), Pair.with(4, 4), Pair.with(4, 5) //
        );

        this.world = new LoopManiaWorld(7, 7, this.orderedPath);
        this.world.setMode(new StandardMode());
        this.world.setHeroCastle(new HeroCastleBuilding(Pair.with(0, 0)));
    }

    @Test
    public void armourTest() {
        // test the status of armour
        GameRandom.random.setSeed(3);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate an armour
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Armour.class.equals(item.getClass()));

        // assert that the item has the correct status
        assertEquals(item.getStatus().getStrength(), 0);
        assertEquals(item.getStatus().getHealth(), 50);
        assertEquals(item.getStatus().getDefense(), 0);

    }

    @Test
    public void helmetTest() {
        // test the status of helmet
        GameRandom.random.setSeed(0);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a helmet
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Helmet.class.equals(item.getClass()));

        // assert that the item has the correct status
        assertEquals(item.getStatus().getStrength(), 0);
        assertEquals(item.getStatus().getHealth(), 25);
        assertEquals(item.getStatus().getDefense(), 5);

    }

    @Test
    public void shieldTest() {
        // test the status of shield
        GameRandom.random.setSeed(2);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a shield
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Shield.class.equals(item.getClass()));

        // assert that the item has the correct status
        assertEquals(item.getStatus().getStrength(), 0);
        assertEquals(item.getStatus().getHealth(), 0);
        assertEquals(item.getStatus().getDefense(), 10);

    }

    @Test
    public void staffTest() {
        // test the status of staff
        GameRandom.random.setSeed(25);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a staff
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Staff.class.equals(item.getClass()));

        // assert that the item has the correct status
        assertEquals(item.getStatus().getStrength(), 2);
        assertEquals(item.getStatus().getHealth(), 0);
        assertEquals(item.getStatus().getDefense(), 0);

    }

    @Test
    public void stakeTest() {
        // test the status of stake
        GameRandom.random.setSeed(17);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a stake
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Stake.class.equals(item.getClass()));

        // assert that the item has the correct status
        assertEquals(item.getStatus().getStrength(), 5);
        assertEquals(item.getStatus().getHealth(), 0);
        assertEquals(item.getStatus().getDefense(), 0);

    }

    @Test
    public void swordTest() {
        // test the status of sword
        GameRandom.random.setSeed(7);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a sword
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Sword.class.equals(item.getClass()));

        // assert that the item has the correct status
        assertEquals(item.getStatus().getStrength(), 10);
        assertEquals(item.getStatus().getHealth(), 0);
        assertEquals(item.getStatus().getDefense(), 0);

    }

    @Test
    public void theOneRingTest() {
        // test the status of the one ring
        GameRandom.random.setSeed(4096);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a the one ring
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(TheOneRing.class.equals(item.getClass()));

        // assert that the item has the correct status
        assertEquals(item.getStatus().getStrength(), 0);
        assertEquals(item.getStatus().getHealth(), 0);
        assertEquals(item.getStatus().getDefense(), 0);

    }

    @Test
    public void treeStumpTest() {
        // test the status of treestump
        GameRandom.random.setSeed(4098);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a treestump
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(TreeStump.class.equals(item.getClass()));

        // assert that the item has the correct status
        assertEquals(item.getStatus().getStrength(), 0);
        assertEquals(item.getStatus().getHealth(), 0);
        assertEquals(item.getStatus().getDefense(), 10);

    }

    @Test
    public void andurilTest() {
        // test the status of anduril
        GameRandom.random.setSeed(4099);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a the one anduril
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Anduril.class.equals(item.getClass()));

        // assert that the item has the correct status
        assertEquals(item.getStatus().getStrength(), 10);
        assertEquals(item.getStatus().getHealth(), 0);
        assertEquals(item.getStatus().getDefense(), 0);

    }

    @Test
    public void reviveTest() {
        // test the function revive character
        // first make character equip to full item with no revive ability
        PathPosition newPosition = new PathPosition(1, this.orderedPath);
        GameRandom.random.setSeed(4099);
        Character character = new Character(newPosition, world);
        world.setCharacter(character);

        // generate a the one anduril
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Anduril.class.equals(item.getClass()));
        world.getItemSystem().equipItemByCoordinates(0, 0, 0, 0);
        // make item in character unequipped inventory full.
        GameRandom.random.setSeed(3);
        for (int i = 0; i < 16; i++) {
            world.getItemSystem().addUnequippedItem();
        }
        // and character equip to full item
        world.getItemSystem().equipItemByCoordinates(0, 0, 1, 0);
        world.getItemSystem().equipItemByCoordinates(0, 3, 2, 0);
        world.getItemSystem().equipItemByCoordinates(1, 2, 3, 0);
        // let character die
        character.getStatus().setCurrentHealth(0);
        assertFalse(character.getStatus().isAlive());

        // enemy kill the character but there is no ring to revive, so character still die
        Vampire vampire = new Vampire(newPosition, world);
        world.getBattleSystem().getEnemies().add(vampire);
        world.getBattleSystem().runBattles();
        assertFalse(character.getStatus().isAlive());
    }
}
