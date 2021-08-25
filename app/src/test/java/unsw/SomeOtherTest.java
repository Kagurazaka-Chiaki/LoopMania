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
import unsw.entity.item.Staff;
import unsw.entity.item.Stake;
import unsw.loopmania.BuildingSystem;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.mode.StandardMode;
import unsw.type.BuildingType;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SomeOtherTest {
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
    public void testThiefSteal() {
        // test that thief can not steal character's gold to negative amount
        // spawn a thief
        world.setCycle(10);
        PathPosition newPosition = new PathPosition(0, orderedPath);
        Thief thief = new Thief(newPosition, world);
        world.getBattleSystem().getEnemies().add(thief);
        assertEquals(1, world.getBattleSystem().getEnemies().size());
        assertTrue(Thief.class.equals(world.getBattleSystem().getEnemies().get(0).getClass()));

        GameRandom.random.setSeed(2);
        world.getItemSystem().addUnequippedItem();
        world.getItemSystem().addUnequippedItem();
        assertEquals(2, world.getItemSystem().getUnequippedInventory().size());

        // character lose to thief and two item with 111 gold got stolen
        List<BasicEnemy> defeatedEnemies = world.getBattleSystem().runBattles();
        assertEquals(0, world.getItemSystem().getUnequippedInventory().size());
        assertEquals(0, character.getGoldValue());
        assertEquals(defeatedEnemies.size(), 0);
        assertFalse(character.getStatus().isAlive());
    }

    @Test
    public void testBattleNearCampFire() {
        // test the battle near campFire is working
        // spawn enemy
        PathPosition newPosition = new PathPosition(0, orderedPath);
        Zombie zombie = new Zombie(newPosition, world);
        world.getBattleSystem().getEnemies().add(zombie);
        zombie = new Zombie(newPosition, world);
        world.getBattleSystem().getEnemies().add(zombie);
        zombie = new Zombie(newPosition, world);
        world.getBattleSystem().getEnemies().add(zombie);
        Vampire vampire = new Vampire(newPosition, world);
        world.getBattleSystem().getEnemies().add(vampire);
        vampire = new Vampire(newPosition, world);
        world.getBattleSystem().getEnemies().add(vampire);
        assertEquals(5, world.getBattleSystem().getEnemies().size());

        // make character stronger so it can win
        character.getStatus().setStrength(43);
        character.getStatus().setDefense(15);
        character.getStatus().getCurrentHealth().set(120);

        // give character equip a staff
        GameRandom.random.setSeed(25);
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Staff.class.equals(item.getClass()));
        world.getItemSystem().equipItemByCoordinates(0, 0, 0, 0);

        // the card is campfire
        GameRandom.random.setSeed(17);
        world.loadCard();
        BuildingSystem buildingSystem = world.getBuildingSystem();
        buildingSystem.convertCardToBuildingByCoordinates(0, 0, 2, 2);
        List<Building> buildingList = buildingSystem.getBuildingEntities();
        assertEquals(BuildingType.CampfireBuilding, buildingList.get(0).getBuildingType());
        buildingSystem.setBuff();
        // test that character is buff
        assertTrue(character.getIfBuff());

        // test that all enemy is killed, normally character shouldnt win
        // this fight with this status
        GameRandom.random.setSeed(17);
        List<BasicEnemy> defeatedEnemies = world.getBattleSystem().runBattles();
        assertTrue(character.getStatus().isAlive());
        assertEquals(5, defeatedEnemies.size());
    }

    @Test
    public void testStake() {
        // test that stake ability is invalid to other enemy
        // spawn enemy
        PathPosition newPosition = new PathPosition(0, orderedPath);
        Zombie zombie = new Zombie(newPosition, world);
        Slug slug = new Slug(newPosition, world);

        // give character equip a stake
        GameRandom.random.setSeed(17);
        Item item = world.getItemSystem().addUnequippedItem();
        assertTrue(Stake.class.equals(item.getClass()));
        world.getItemSystem().equipItemByCoordinates(0, 0, 0, 0);

        // test that ability did not trigger
        assertFalse(item.useAbility(character, zombie));
        assertFalse(item.useAbility(character, slug));
    }
}