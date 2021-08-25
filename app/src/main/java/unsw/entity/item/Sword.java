package unsw.entity.item;

import org.javatuples.Pair;

import unsw.entity.Status;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Sword extends MeleeWeapon {

    /**
     * <p>
     * Constructor {@code Sword}
     * </p >
     * <p>
     * Initialises the equipment of type Sword which has a relative higher strength
     *
     * @param position          // the position in the item list
     * @param n                 // the cycles' character has travelled
     * @param difficultConstant // how much to equip can upgrade according to mode
     **/
    public Sword(Pair<Integer, Integer> position, int n, double difficultConstant) {
        super(position);
        super.setStatus(new Status(10, 0, 0));
        super.increaseItemAttribute(n, difficultConstant);
        super.setEntityView(super.loadImage("images/basic_sword.png"));
        super.setValidPosX(0);
        super.setValidPosY(0);
    }
}
