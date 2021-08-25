package unsw.entity.item;

import org.javatuples.Pair;

import unsw.entity.Status;
import unsw.entity.ability.StaffAbility;

public class Staff extends MeleeWeapon {

    /**
     * <p>
     * Constructor {@code Staff}
     * </p >
     * <p>
     * Initialises the equipment of type Staff which has chance to
     * trance an enemy and turns it into a temp allied soldier
     *
     * @param position          // the position in the item list
     * @param n                 // the cycles character has travelled
     * @param difficultConstant // how much the equip can upgrade according to mode
     **/
    public Staff(Pair<Integer, Integer> position, int n, double difficultConstant) {
        super(position);
        super.setStatus(new Status(2, 0, 0));
        super.increaseItemAttribute(n, difficultConstant);
        super.setEntityView(super.loadImage("images/staff.png"));
        super.setAbility(new StaffAbility());
        super.setValidPosX(0);
        super.setValidPosY(0);
    }
}
