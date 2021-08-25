package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.Item;
import unsw.entity.building.HeroCastleBuilding;
import unsw.entity.enemy.Vampire;
import unsw.entity.enemy.Zombie;
import unsw.entity.item.Anduril;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.mode.StandardMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LevelTest {
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
        this.character = new Character(new PathPosition(0, this.orderedPath), world);
        world.setCharacter(character);
        StandardMode stdMode = new StandardMode();
        world.setMode(stdMode);
        this.world.setHeroCastle(new HeroCastleBuilding(Pair.with(0, 0)));
    }

    @Test
    public void characterUpgradTest() {
        // let character upgrade to level 11 so it can win
        for (int i = 0; i < 10; i++) {
            character.upgrade();
        }
        PathPosition enemyPathPosition = new PathPosition(2, this.orderedPath);
        BasicEnemy enemy_1 = new Zombie(enemyPathPosition, world);
        BasicEnemy enemy_2 = new Zombie(enemyPathPosition, world);
        BasicEnemy enemy_3 = new Vampire(enemyPathPosition, world);
        BasicEnemy enemy_4 = new Vampire(enemyPathPosition, world);

        world.getBattleSystem().getEnemies().add(enemy_1);
        world.getBattleSystem().getEnemies().add(enemy_2);
        world.getBattleSystem().getEnemies().add(enemy_3);
        world.getBattleSystem().getEnemies().add(enemy_4);

        // load five soldier to character, so it can win
        for (int i = 0; i < 5; i++) {
            world.getBattleSystem().loadSoldier();
        }

        // set random seed = 0
        GameRandom.random.setSeed(0);
        world.runTickMoves();

        GameRandom.random.setSeed(4099);
        // generate an Anduril for character to equip
        Item item = world.getItemSystem().addUnequippedItem();
        assertEquals(Anduril.class, item.getClass());
        world.getItemSystem().equipItemByCoordinates(0, 0, 0, 0);

        // check that is 4 enemy killed
        List<BasicEnemy> defeatedEnemies = world.getBattleSystem().runBattles();
        assertEquals(defeatedEnemies.size(), 4);
        assertTrue(character.getStatus().isAlive());

        // two zombie give 100 exp, and two vampire gives 200exp, so now character
        // will be level 12, since we upgrade character to level 11 before.
        assertEquals(character.getLevelValue(), 12);
    }

    @Test
    public void checkStatusGainByUpgrade() {
        // check character's initial status
        assertEquals(character.getStatus().getStrength(), 15);
        assertEquals(character.getStatus().getHealth(), 250);
        assertEquals(character.getStatus().getDefense(), 8);
            
        character.upgrade();

        // after character upgrade, character should gain 3 defense, 8 strength
        // and 50 hp
        assertEquals(character.getStatus().getStrength(), 23);
        assertEquals(character.getStatus().getHealth(), 300);
        assertEquals(character.getStatus().getDefense(), 11);
    }

    @Test
    public void checkRewards() {
        // check the reward function will return either gold or item
        GameRandom.random.setSeed(1);
        Item item = world.rewards();
        // so reward of 50 gold is given, but no item
        assertNull(item);
        assertEquals(character.getGoldValue(), 50);

        GameRandom.random.setSeed(2);
        item = world.rewards();
        // so reward of item is given, but no gold
        assertNotNull(item);
        assertEquals(character.getGoldValue(), 50);
    }
}
