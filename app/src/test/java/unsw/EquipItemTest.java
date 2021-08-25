package unsw;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.Character;
import unsw.entity.Item;
import unsw.entity.item.*;
import unsw.loopmania.LoopManiaWorld;
import unsw.mode.StandardMode;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class EquipItemTest {

    private LoopManiaWorld world;

    @BeforeEach
    public void setupWorld() {
        JFXPanel jfxPanel = new JFXPanel();
        this.world = new LoopManiaWorld(2, 2, new ArrayList<>());
        this.world.setMode(new StandardMode());
    }

    @Test
    public void equipItem() {
        Random rnd = GameRandom.random;
        rnd.setSeed(3);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // item is a Armour
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Armour.class.equals(item.getClass()));
        world.getItemSystem().equipItemByCoordinates(0, 0, 1, 0);

        // assert that the character has successfully wear the armour
        assertEquals(character.getStatus().getStrength(), 15 + item.getStatus().getStrength());
        assertEquals(character.getStatus().getHealth(), 250 + item.getStatus().getHealth());
        assertEquals(character.getStatus().getDefense(), 8 + item.getStatus().getDefense());

        // item will still be equip even through cell is not correct because
        // equip on valid cell is check on another fuction
        world.getItemSystem().addUnequippedItem();
        world.getItemSystem().equipItemByCoordinates(0, 0, 3, 0);
        world.getItemSystem().addUnequippedItem();
        world.getItemSystem().equipItemByCoordinates(0, 0, 3, 1);
        assertEquals(world.getItemSystem().getEquippedItem().size(), 3);
    }

    @Test
    public void changeEquippedItem() {
        Random rnd = GameRandom.random;
        rnd.setSeed(5);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // item1 is a shield
        Item item1 = world.getItemSystem().addUnequippedItem();
        assertTrue(Shield.class.equals(item1.getClass()));
        world.getItemSystem().equipItemByCoordinates(0, 0, 1, 0);

        // item2 is a staff and character change the equipped item from shield to staff
        Item item2 = world.getItemSystem().addUnequippedItem();
        assertTrue(Staff.class.equals(item2.getClass()));
        world.getItemSystem().equipItemByCoordinates(0, 0, 1, 0);

        // assert that the character has succeed in wearing new Item
        assertEquals(character.getStatus().getStrength(), 15 + item2.getStatus().getStrength());
        assertEquals(character.getStatus().getHealth(), 250 + item2.getStatus().getHealth());
        assertEquals(character.getStatus().getDefense(), 8 + item2.getStatus().getDefense());
    }

    @Test
    public void Equipsword() {
        // test that when equip weapon, the weapon object will also be store in character
        GameRandom.random.setSeed(7);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // weapon was not equipped so it should be null for now
        assertNull(character.getWeapon());

        // item1 is a sword
        Item item1 = world.getItemSystem().addUnequippedItem();
        assertTrue(Sword.class.equals(item1.getClass()));
        world.getItemSystem().equipItemByCoordinates(0, 0, 0, 0);

        // weapon sword is equipped so a object will be exit in character aswell
        assertNotNull(character.getWeapon());
    }

    @Test
    public void Equipshield() {
        // test that when equip shield, the shield object will also be store in character
        GameRandom.random.setSeed(2);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // shield was not equipped so it should be null for now
        assertNull(character.getShield());

        // item1 is a sword
        Item item1 = world.getItemSystem().addUnequippedItem();
        assertTrue(Shield.class.equals(item1.getClass()));
        world.getItemSystem().equipItemByCoordinates(0, 0, 3, 0);

        // weapon sword is equipped so a object will be exit in character aswell
        assertNotNull(character.getShield());
    }

    @Test
    public void checkValidCellSword() {
        // check that sword can only be put in 0,0
        GameRandom.random.setSeed(7);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a sword
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Sword.class.equals(item.getClass()));
        // sword can only be equip on slot(0,0)
        assertTrue(world.getItemSystem().checkEquipValidCell(0, 0, 0, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 1, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 3, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 2, 0));
    }

    @Test
    public void checkValidCellStaff() {
        // check that staff can only be put in 0,0
        GameRandom.random.setSeed(25);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a staff
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Staff.class.equals(item.getClass()));
        // staff can only be equip on slot(0,0)
        assertTrue(world.getItemSystem().checkEquipValidCell(0, 0, 0, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 1, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 3, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 2, 0));
    }

    @Test
    public void checkValidCellStake() {
        // check that stake can only be put in 0,0
        GameRandom.random.setSeed(17);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a stake
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Stake.class.equals(item.getClass()));
        // stake can only be equip on slot(0,0)
        assertTrue(world.getItemSystem().checkEquipValidCell(0, 0, 0, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 1, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 3, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 2, 0));
    }

    @Test
    public void checkValidCellArmour() {
        // check that armour can only be put in 2,0
        GameRandom.random.setSeed(3);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a armour
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Armour.class.equals(item.getClass()));
        // armour can only be equip on slot(2,0)
        assertTrue(world.getItemSystem().checkEquipValidCell(0, 0, 2, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 1, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 3, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 0, 0));
    }

    @Test
    public void checkValidCellHelmet() {
        // check that helmet can only be put in 2,0
        GameRandom.random.setSeed(0);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a helmet
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Helmet.class.equals(item.getClass()));
        // helmet can only be equip on slot(1,0)
        assertTrue(world.getItemSystem().checkEquipValidCell(0, 0, 1, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 2, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 3, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 0, 0));
    }

    @Test
    public void checkValidCellShield() {
        // check that helmet can only be put in 3,0
        GameRandom.random.setSeed(2);
        Character character = new Character(null, world);
        world.setCharacter(character);

        // generate a helmet
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Shield.class.equals(item.getClass()));
        // shield can only be equip on slot(3,0)
        assertTrue(world.getItemSystem().checkEquipValidCell(0, 0, 3, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 2, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 1, 0));
        assertFalse(world.getItemSystem().checkEquipValidCell(0, 0, 3, 1));
    }
}
