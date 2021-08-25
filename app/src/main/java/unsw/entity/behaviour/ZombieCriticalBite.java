package unsw.entity.behaviour;

import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.enemy.AlliedSoldier;
import unsw.loopmania.BattleSystem;
import unsw.loopmania.LoopManiaWorld;

public class ZombieCriticalBite implements AttackBehaviour {
    private final BattleSystem battleSystem;

    /**
     * Constructor for zombie critical bite
     * @param world
     */
    public ZombieCriticalBite(LoopManiaWorld world) {
        this.battleSystem = world.getBattleSystem();
    }

    /**
     * * <p>
     * Method {@code ZombieCriticalBite}
     * </p >
     * <p>
     * Implement the special attack method for Zombie with given parameters.
     *
     * @param strength //
     * @param target   //
     **/
    public int attack(double strength, Character target) {
        target.BeAttacked(strength);
        getBattleInfo().append("Zombie ---- Bite ----> Character Current HP : ") //
                    .append(mathRound(target.getStatus().getCurrentHealthValue(), 2)) //
                    .append("\n");
        if (!target.getStatus().isAlive()) {
            getBattleInfo().append("\n ---------------- Character is killed !!! ---------------- \n\n");
        }
        return 2;
    }

    /**
     * Proccess attack to allied soldier
     */
    public int attack(double strength, AlliedSoldier target, int sequence) {
        getBattleInfo().append("------------Zombie Infect ").append(target.getName())
        .append("------------") //
        .append("\n");
        getBattleInfo().append(target.getName()).append(" is infected by ").append("Zombie")
                .append("\n");

        battleSystem.removeSoldier(sequence);
        battleSystem.getInfectedSoldiers().add(target.BeInfected( //
        battleSystem.getCharacter().getPathPosition(), //
        battleSystem.getWorld().getMode().getDifficultyConstant(), //
        battleSystem.getWorld().getCycle().get()) //
        );
        return 2;
    }

    /**
     * process attack to enemy
     */
    public int attack(double strength, BasicEnemy target) {
        return 2;
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
