package unsw.entity.behaviour;

import unsw.GameRandom;
import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.enemy.AlliedSoldier;
import unsw.loopmania.BattleSystem;
import unsw.loopmania.LoopManiaWorld;

public class VampireCritialBite implements AttackBehaviour {
    private final BattleSystem battleSystem;

    /**
     * Constructor for vampire critical bite
     * @param world
     */
    public VampireCritialBite(LoopManiaWorld world) {
        this.battleSystem = world.getBattleSystem();
    }

    /**
     * * <p>
     * Method {@code VampireCritialBite}
     * </p >
     * <p>
     * Implement the special attack method for Vampire with given parameters.
     *
     * @param strength //
     * @param target   //
     **/
    public int attack(double strength, Character target) {
        double extraRatio = (double) (GameRandom.random.nextInt(6) + 1) / 10.0;
        double damage = (1 + extraRatio) * strength;

        int AttackNumber = GameRandom.random.nextInt(3) + 1;

        for (int i = 0; i < AttackNumber; i++) {
            target.BeAttacked(damage);
        }
        getBattleInfo().append("Vampire ---- Bite ----> Character Current HP : ") //
                    .append(mathRound(target.getStatus().getCurrentHealthValue(), 2)) //
                    .append("\n");
        if (!target.getStatus().isAlive()) {
            getBattleInfo().append("\n ---------------- Character is killed !!! ---------------- \n\n");
        }
        return 1;
    }

    /**
     * Proccess attack to allied soldier
     */
    public int attack(double strength, AlliedSoldier target, int sequence) {
        double extraRatio = (double) (GameRandom.random.nextInt(6) + 1) / 10.0;
        double damage = (1 + extraRatio) * strength;

        int AttackNumber = GameRandom.random.nextInt(3) + 1;

        for (int i = 0; i < AttackNumber; i++) {
            target.BeAttacked(damage);
        }
        return 1;
    }

    /**
     * process attack to enemy
     */
    public int attack(double strength, BasicEnemy target) {
        return 1;
    }

    private static double mathRound(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Getter for battle info
     * @return
     */
    private StringBuilder getBattleInfo() {
        return battleSystem.getBattleInfo();
    }

}
