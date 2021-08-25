package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.building.HeroCastleBuilding;
import unsw.entity.enemy.AlliedSoldier;
import unsw.entity.enemy.Slug;
import unsw.entity.enemy.Zombie;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.mode.StandardMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlliedSoldierTest {

    private LoopManiaWorld world;

    @BeforeEach
    public void setupWorld() {
        JFXPanel jfxPanel = new JFXPanel();

        List<Pair<Integer, Integer>> orderedPath = Arrays.asList( //
            Pair.with(3, 4), Pair.with(4, 4), Pair.with(4, 5) //
        );

        this.world = new LoopManiaWorld(7, 7, orderedPath);
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
    public void loadSoldierTest() {
        // test spawning allied soldier to character, and maximum is five allied soldier
        for (int i = 0; i < 6; i++) {
            world.getBattleSystem().loadSoldier();
        }
        List<AlliedSoldier> soldiers = world.getBattleSystem().getSoldiers();
        assertTrue(soldiers.size() == 5);
    }

    @Test
    public void chooseAttackTest() {
        // test that allied soldier can attack enemy
        List<BasicEnemy> enemies = world.getBattleSystem().getEnemies();
        world.getBattleSystem().loadSoldier();
        AlliedSoldier s = world.getBattleSystem().getSoldiers().get(0);
        BasicEnemy target = s.chooseAttack(enemies);
        assertTrue(enemies.contains(target));
    }

    @Test
    public void beAttackedTest() {
        // test that allied soldier can be attack by enemy
        world.getBattleSystem().loadSoldier();
        AlliedSoldier s = world.getBattleSystem().getSoldiers().get(0);
        double previousHp = s.getStatus().getCurrentHealthValue();
        s.BeAttacked(20);
        double currentHp = s.getStatus().getCurrentHealthValue();
        assertTrue(previousHp > currentHp);
        assertTrue(s.getStatus().isAlive());
    }

}
