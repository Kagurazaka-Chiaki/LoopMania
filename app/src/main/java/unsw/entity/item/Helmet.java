package unsw.entity.item;

import org.javatuples.Pair;

import unsw.entity.Status;

public class Helmet extends ProtectiveEquipment {

    /**
     * <p>
     * Constructor {@code Helmet}
     * </p >
     * <p>
     * Initialises the equipment of type Helmet which provides extra
     * health and defence for the character with given arguments
     *
     * @param position          // the position in the item list
     * @param n                 // the cycles character has travelled
     * @param difficultConstant // how much the equip can upgrade according to mode
     **/
    public Helmet(Pair<Integer, Integer> position, int n, double difficultConstant) {
        super(position);
        super.setStatus(new Status(0, 25, 5));
        super.increaseItemAttribute(n, difficultConstant);
        super.setEntityView(super.loadImage("images/helmet.png"));
        super.setValidPosX(1);
        super.setValidPosY(0);
    }
}
