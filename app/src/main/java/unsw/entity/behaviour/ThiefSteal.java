package unsw.entity.behaviour;

import unsw.GameRandom;
import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.enemy.AlliedSoldier;
import unsw.loopmania.BattleSystem;
import unsw.loopmania.ItemSystem;
import unsw.loopmania.LoopManiaWorld;

public class ThiefSteal implements AttackBehaviour {
    private final BattleSystem battleSystem;

    /**
     * Constructor for thief steal
     *
     * @param world
     */
    public ThiefSteal(LoopManiaWorld world) {
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
        target.BeAttacked(strength);
        int choice = GameRandom.random.nextInt(5);
        if (choice < 4) {
            int stolenGold = GameRandom.random.nextInt(20) + 10;
            Character character = target;
            if (character.getGoldValue() - stolenGold < 0) {
                character.setGold(0);
                stolenGold = character.getGoldValue();
            } else {
                character.setGold(character.getGoldValue() - stolenGold);
            }
            getBattleInfo().append("------------ the thief steal: ").append(stolenGold).append(" amount of money")
                           .append("------------") //
                           .append("\n");
        } else {
            getItemSystem().stealItemByPositionInUnequippedInventoryItems();
            getBattleInfo().append("------------ the thief steal one of you item, be careful!!!")
                           .append("------------") //
                           .append("\n");
        }

        return 1;
    }

    /**
     * Proccess attack to allied soldier
     */
    public int attack(double strength, AlliedSoldier target, int sequence) {
        target.BeAttacked(strength);
        return 1;
    }

    /**
     * process attack to enemy
     */
    public int attack(double strength, BasicEnemy target) {
        return 1;
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
     * Getter for item system
     *
     * @return
     */
    private ItemSystem getItemSystem() {
        return this.battleSystem.getWorld().getItemSystem();
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
