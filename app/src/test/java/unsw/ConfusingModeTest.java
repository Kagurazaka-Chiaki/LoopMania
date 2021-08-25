package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.Character;
import unsw.entity.Item;
import unsw.entity.ability.AndurilAbility;
import unsw.entity.ability.ItemAbility;
import unsw.entity.ability.RingAbility;
import unsw.entity.ability.StumpAbility;
import unsw.entity.building.HeroCastleBuilding;
import unsw.entity.enemy.Vampire;
import unsw.entity.enemy.Zombie;
import unsw.entity.item.Anduril;
import unsw.entity.item.TreeStump;
import unsw.entity.item.TheOneRing;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.mode.ConfusingMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConfusingModeTest {
    private LoopManiaWorld world;
    private Character character;
    private List<Pair<Integer, Integer>> orderedPath = null;

    @BeforeEach
    public void setupWorld() {
        JFXPanel jfxPanel = new JFXPanel();

        this.orderedPath = Arrays.asList( //
            Pair.with(3, 4), Pair.with(4, 4), Pair.with(4, 5) //
        );

        this.world = new LoopManiaWorld(7, 7, this.orderedPath);
        this.character = new Character(new PathPosition(1, this.orderedPath), world);
        world.setCharacter(character);
        ConfusingMode mode = new ConfusingMode();
        world.setMode(mode);
        this.world.setHeroCastle(new HeroCastleBuilding(Pair.with(0, 0)));
    }

    @Test
    public void testRareItem() {
        // test rare item generate in confusing mode can have two ability
        GameRandom.random.setSeed(4102);

        // generate one anduril, and check that it has two ability
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Anduril.class.equals(item.getClass()));
        assertEquals(item.getAbility().size(), 2);
        
        // the extra ring ability is added
        ItemAbility ability = item.getAbility().get(1);
        assertTrue(RingAbility.class.equals(ability.getClass()));

        // check that confusing mode does not restrict amount item can buy
        assertEquals(world.getMode().amountOfItemCanBeBought(), -1);
        assertFalse(world.getMode().restrictedAmount(item));

        GameRandom.random.setSeed(4099);
        // generate one anduril, and check that it can also be produce with one ability
        item = world.getItemSystem().addUnequippedItem();
        assertTrue(Anduril.class.equals(item.getClass()));
        assertEquals(item.getAbility().size(), 1);
    }

    @Test
    public void testRareItem2() {
        // test rare item generate in confusing mode can have two ability
        GameRandom.random.setSeed(4119);

        // generate one anduril, and check that it has two ability
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Anduril.class.equals(item.getClass()));
        assertEquals(item.getAbility().size(), 2);
        
        // the extra stumpAbility ability is added
        ItemAbility ability = item.getAbility().get(1);
        assertTrue(StumpAbility.class.equals(ability.getClass()));

        // and this rare item will be added the property of tree stump aswell,
        // so it will have extra defense status
        assertEquals(item.getStatus().getDefense(), 10.5);
        assertEquals(item.getStatus().getStrength(), 10.5);
        assertEquals(item.getStatus().getHealth(), 0);
    }

    @Test
    public void testRareItem3() {
        // test rare item generate in confusing mode can have two ability
        GameRandom.random.setSeed(4101);

        // generate one Tree stump, and check that it has two ability
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(TreeStump.class.equals(item.getClass()));
        assertEquals(item.getAbility().size(), 2);
        
        // the extra AndurilAbility ability is added
        ItemAbility ability = item.getAbility().get(1);
        assertTrue(AndurilAbility.class.equals(ability.getClass()));

        // and this rare item will be added the property of anduril aswell,
        // so it will have extra defense status
        assertEquals(item.getStatus().getDefense(), 10.5);
        assertEquals(item.getStatus().getStrength(), 10.5);
        assertEquals(item.getStatus().getHealth(), 0);
    }

    @Test
    public void testRareItem4() {
        // test rare item generate in confusing mode can have two ability
        GameRandom.random.setSeed(4139);

        // generate one Tree stump, and check that it has two ability
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(TreeStump.class.equals(item.getClass()));
        assertEquals(item.getAbility().size(), 2);
        
        // the extra RingAbility ability is added
        ItemAbility ability = item.getAbility().get(1);
        assertTrue(RingAbility.class.equals(ability.getClass()));
    }

    @Test
    public void testRareItem5() {
        // test rare item generate in confusing mode can have two ability
        GameRandom.random.setSeed(4113);

        // generate one ring, and check that it has two ability
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(TheOneRing.class.equals(item.getClass()));
        assertEquals(item.getAbility().size(), 2);
        
        // the extra RingAbility ability is added
        ItemAbility ability = item.getAbility().get(1);
        assertTrue(AndurilAbility.class.equals(ability.getClass()));

        // and this rare item will be added the property of anduril aswell,
        // so it will have extra strength status
        assertEquals(item.getStatus().getDefense(), 0);
        assertEquals(item.getStatus().getStrength(), 10.5);
        assertEquals(item.getStatus().getHealth(), 0);

        // check that ring now can be equip since it have anduril ability now
        assertEquals(item.getValidPosX(), 0);
        assertEquals(item.getValidPosY(), 0);
    }

    @Test
    public void testRareItem6() {
        // test rare item generate in confusing mode can have two ability
        GameRandom.random.setSeed(4117);

        // generate one ring, and check that it has two ability
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(TheOneRing.class.equals(item.getClass()));
        assertEquals(item.getAbility().size(), 2);
        
        // the extra RingAbility ability is added
        ItemAbility ability = item.getAbility().get(1);
        assertTrue(StumpAbility.class.equals(ability.getClass()));

        // and this rare item will be added the property of tree stump aswell,
        // so it will have extra strength status
        assertEquals(item.getStatus().getDefense(), 10.5);
        assertEquals(item.getStatus().getStrength(), 0);
        assertEquals(item.getStatus().getHealth(), 0);

        // check that ring now can be equip since it have anduril ability now
        assertEquals(item.getValidPosX(), 3);
        assertEquals(item.getValidPosY(), 0);
    }

    @Test
    public void reviveOfAnduril() {
        // test the revive ability for anduril is work normal when equipped
        PathPosition newPosition = new PathPosition(1, this.orderedPath);
        GameRandom.random.setSeed(4102);

        // generate a the one anduril
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Anduril.class.equals(item.getClass()));
        // character equipped the anduril
        world.getItemSystem().equipItemByCoordinates(0, 0, 0, 0);

        // make character low hp
        character.getStatus().setCurrentHealth(5);
        assertTrue(character.getStatus().isAlive());

        // enemy kill the character but revive using anduril, and
        // anduril should be destory, status of character should back to normal
        Zombie zombie = new Zombie(newPosition, world);
        world.getBattleSystem().getEnemies().add(zombie);
        world.getBattleSystem().runBattles();
        assertTrue(character.getStatus().isAlive());
        assertEquals(world.getItemSystem().getEquippedItem().size(), 0);
        // check that character's weapon is back to null, because anduril is destroy
        assertNull(character.getWeapon());
        assertEquals(character.getStatus().getDefense(), 8);
        assertEquals(character.getStatus().getStrength(), 15);
        assertEquals(character.getStatus().getHealth(), 250);

    }

    @Test
    public void reviveOfTreeStump() {
        // test the revive ability for tree stump is work normal when equipped
        PathPosition newPosition = new PathPosition(1, this.orderedPath);
        GameRandom.random.setSeed(4139);

        // generate a the one tree stump
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(TreeStump.class.equals(item.getClass()));
        // character equipped the tree stump
        world.getItemSystem().equipItemByCoordinates(0, 0, 3, 0);

        // make character low hp
        character.getStatus().setCurrentHealth(5);
        assertTrue(character.getStatus().isAlive());

        // enemy kill the character but revive using tree stump, and
        // tree stump should be destory, status of character should back to normal
        Zombie zombie = new Zombie(newPosition, world);
        world.getBattleSystem().getEnemies().add(zombie);
        world.getBattleSystem().runBattles();
        assertTrue(character.getStatus().isAlive());
        assertEquals(world.getItemSystem().getEquippedItem().size(), 0);
        // check that character's weapon is back to null, because tree stump is destroy
        assertNull(character.getWeapon());
        assertEquals(character.getStatus().getDefense(), 8);
        assertEquals(character.getStatus().getStrength(), 15);
        assertEquals(character.getStatus().getHealth(), 250);
    }

    @Test
    public void testRevive() {
        // test the tree stump without ring ability will not revive
        PathPosition newPosition = new PathPosition(1, this.orderedPath);
        GameRandom.random.setSeed(4101);

        // generate a the one tree stump
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(TreeStump.class.equals(item.getClass()));
        // character equipped the tree stump
        world.getItemSystem().equipItemByCoordinates(0, 0, 3, 0);

        // make character low hp
        character.getStatus().setCurrentHealth(5);
        assertTrue(character.getStatus().isAlive());

        // enemy kill the character and will not revive
        Zombie zombie = new Zombie(newPosition, world);
        world.getBattleSystem().getEnemies().add(zombie);
        world.getBattleSystem().runBattles();
        assertFalse(character.getStatus().isAlive());
        // check that shield did not destroy
        assertEquals(world.getItemSystem().getEquippedItem().size(), 1);
        assertNotNull(character.getShield());
    }

    @Test
    public void testRevive2() {
        // test the anduril without ring ability will not revive
        PathPosition newPosition = new PathPosition(1, this.orderedPath);
        GameRandom.random.setSeed(4119);

        // generate a the one anduril
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Anduril.class.equals(item.getClass()));
        // character equipped the anduril
        world.getItemSystem().equipItemByCoordinates(0, 0, 0, 0);

        // make character low hp
        character.getStatus().setCurrentHealth(5);
        assertTrue(character.getStatus().isAlive());

        // enemy kill the character and will not revive
        Vampire vampire = new Vampire(newPosition, world);
        world.getBattleSystem().getEnemies().add(vampire);
        world.getBattleSystem().runBattles();
        assertFalse(character.getStatus().isAlive());
        // check that weapon did not destroy
        assertEquals(world.getItemSystem().getEquippedItem().size(), 1);
        assertNotNull(character.getWeapon());
    }
}
