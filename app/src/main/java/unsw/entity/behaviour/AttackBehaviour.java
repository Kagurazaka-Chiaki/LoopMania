package unsw.entity.behaviour;

import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.enemy.AlliedSoldier;

public interface AttackBehaviour {
    /**
     * <p>
     * AbstractClass {@code AttackBehaviour}
     * </p >
     *
     * <p>
     * represent the method for attack
     * </p >
     **/
    int attack(double strength, Character target);

    /**
     * Process attack to allied
     *
     * @param strength //
     * @param target   //
     * @param sequence //
     * @return //
     */
    int attack(double strength, AlliedSoldier target, int sequence);

    /**
     * Process attack to enemy
     *
     * @param strength //
     * @param target   //
     * @return //
     */
    int attack(double strength, BasicEnemy target);
}
