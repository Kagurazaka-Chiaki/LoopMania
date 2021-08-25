package unsw.entity.item;

import org.javatuples.Pair;

import unsw.entity.Status;
import unsw.entity.ability.StakeAbility;

public class Stake extends MeleeWeapon {

    /**
     * <p>
     * Constructor {@code Stake}
     * </p >
     * <p>
     * Initialises the equipment of type Stake which can attck the vampire
     * with true damage(ignore its defence)
     *
     * @param position          // the position in the item list
     * @param n                 // the cycles character has travelled
     * @param difficultConstant // how much the equip can upgrade according to mode
     **/
    public Stake(Pair<Integer, Integer> position, int n, double difficultConstant) {
        super(position);
        super.setStatus(new Status(5, 0, 0));
        super.increaseItemAttribute(n, difficultConstant);
        super.setEntityView(super.loadImage("images/stake.png"));
        super.setAbility(new StakeAbility());
        super.setValidPosX(0);
        super.setValidPosY(0);
    }

}
