package unsw.entity.behaviour;


import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.enemy.AlliedSoldier;
import unsw.loopmania.BattleSystem;
import unsw.loopmania.LoopManiaWorld;

public class NormalAttack implements AttackBehaviour {
    private final BattleSystem battleSystem;
    private BasicEnemy enemy;

    public NormalAttack(LoopManiaWorld world) {
        this.battleSystem = world.getBattleSystem();
    }

    public NormalAttack(LoopManiaWorld world, BasicEnemy enemy) {
        this.battleSystem = world.getBattleSystem();
        this.enemy = enemy;
    }

    /**
     * <p>
     * Method {@code NormalAttack}
     * </p >
     * <p>
     * Implement the attack method with given parameters
     *
     * @param strength //
     * @param target   //
     **/
    public int attack(double strength, Character target) {
        target.BeAttacked(strength);
        getBattleInfo().append(enemy.getType()).append(" ---- attack ----> Character Current HP : ") //
                    .append(mathRound(target.getStatus().getCurrentHealthValue(), 2)) //
                    .append("\n");
        if (!target.getStatus().isAlive()) {
            getBattleInfo().append("\n ---------------- Character is killed !!! ---------------- \n\n");
        }
        return 0;
    }

    /**
     * process attack to enemy
     */
    public int attack(double strength, BasicEnemy target) {
        target.BeAttacked(strength);
        return 0;
    }

    /**
     * Proccess attack to allied soldier
     */
    public int attack(double strength, AlliedSoldier target, int sequence) {
        target.BeAttacked(strength);
        if (!target.getStatus().isAlive()) {
            getBattleInfo().append(target.getName()).append(" is defeated by ").append(enemy.getType())
                .append(" Current HP: ")
                .append(mathRound(target.getStatus().getCurrentHealthValue(), 2))
                .append("\n");
            battleSystem.removeSoldier(sequence);
        } else {
            getBattleInfo().append(enemy.getType()).append(" ---- attack ----> ")
                .append(target.getName()).append(" Current HP :") //
                .append(mathRound(target.getStatus().getCurrentHealthValue(), 2)) //
                .append("\n");
        }
        return 0;
    }

    private static double mathRound(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Getter for battle system
     * @return
     */
    public BattleSystem getBattleSystem() {
        return this.battleSystem;
    }

    /**
     * Getter for battle info
     * @return
     */
    private StringBuilder getBattleInfo() {
        return battleSystem.getBattleInfo();
    }
}
