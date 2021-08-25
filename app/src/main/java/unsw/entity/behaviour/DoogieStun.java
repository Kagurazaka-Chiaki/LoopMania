package unsw.entity.behaviour;

import unsw.GameRandom;
import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.enemy.AlliedSoldier;
import unsw.loopmania.BattleSystem;
import unsw.loopmania.LoopManiaWorld;

public class DoogieStun implements AttackBehaviour {
    private final BattleSystem battleSystem;

    /**
     * constructor for doggie stun
     *
     * @param world //
     */
    public DoogieStun(LoopManiaWorld world) {
        this.battleSystem = world.getBattleSystem();
    }

    /**
     * proccess attack to character
     */
    public int attack(double strength, Character target) {
        int stunDuration = GameRandom.random.nextInt(2) + 1;
        target.BeAttacked(strength);
        if (target.getStunCount() == 0) {
            target.addStunCount(stunDuration);
            getBattleInfo().append("------------ “Are you winning, son?” Doogie asked, you can not move for ")
                           .append(stunDuration).append(" rounds ------------") //
                           .append("\n");
        }
        return 3;
    }

    /**
     * Proccess attack to allied soldier
     */
    public int attack(double strength, AlliedSoldier target, int sequence) {
        target.BeAttacked(strength);
        return 3;
    }

    /**
     * process attack to enemy
     */
    public int attack(double strength, BasicEnemy target) {
        return 3;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Getter for battle info
     *
     * @return //
     */
    private StringBuilder getBattleInfo() {
        return battleSystem.getBattleInfo();
    }

    /**
     * Getter for battle system
     *
     * @return //
     */
    public BattleSystem getBattleSystem() {
        return this.battleSystem;
    }

}
