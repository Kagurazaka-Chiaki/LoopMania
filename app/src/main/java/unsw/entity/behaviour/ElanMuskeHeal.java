package unsw.entity.behaviour;

import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.enemy.AlliedSoldier;
import unsw.loopmania.BattleSystem;
import unsw.loopmania.LoopManiaWorld;

import java.util.List;

public class ElanMuskeHeal implements AttackBehaviour {
    private final BattleSystem battleSystem;

    /**
     * Constructor for Elan Muske ability
     *
     * @param world
     */
    public ElanMuskeHeal(LoopManiaWorld world) {
        this.battleSystem = world.getBattleSystem();
    }

    /**
     * proccess attack to character
     */
    public int attack(double strength, Character target) {
        List<BasicEnemy> battleEnemies = getBattleEnemies();
        for (BasicEnemy e : battleEnemies) {
            if (e.getType() != "ElanMuske") {
                e.getStatus().setCurrentHealth(e.getStatus().getHealth());
            }
        }
        getBattleInfo().append("------------ feel the love of ElanMuske!!!").append(" ------------") //
                       .append("\n");

        getBattleInfo().append("------------ all the enemies in the battle are recovered!!!").append(" ------------") //
                       .append("\n");
        return 3;
    }

    /**
     * Proccess attack to allied soldier
     */
    public int attack(double strength, AlliedSoldier target, int sequence) {
        List<BasicEnemy> battleEnemies = getBattleEnemies();
        for (BasicEnemy e : battleEnemies) {
            if (e.getType() != "ElanMuske") {
                e.getStatus().setCurrentHealth(e.getStatus().getHealth());
            }
        }
        getBattleInfo().append("------------ feel the love of ElanMuske!!!").append(" ------------") //
                       .append("\n");

        getBattleInfo().append("------------ all the enemies in the battle are recovered!!!").append(" ------------") //
                       .append("\n");
        return 3;
    }

    /**
     * process attack to enemy
     */
    public int attack(double strength, BasicEnemy target) {
        return 0;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Getter for battle system
     *
     * @return
     */
    public BattleSystem getBattleSystem() {
        return this.battleSystem;
    }

    /**
     * Getter for battle enemies
     *
     * @return
     */
    public List<BasicEnemy> getBattleEnemies() {
        return this.battleSystem.getBattleEnemies();
    }

    /**
     * Getter for battle info
     *
     * @return
     */
    private StringBuilder getBattleInfo() {
        return battleSystem.getBattleInfo();
    }
}
