package unsw.entity.item;

import org.javatuples.Pair;

import unsw.entity.Status;

public class Shield extends ProtectiveEquipment {

    /**
     * <p>
     * Constructor {@code Shield}
     * </p >
     * <p>
     * Initialises the equipment of type Shield which provides extra
     * defence for the character with given arguments
     *
     * @param position          // the position in the item list
     * @param n                 // the cycles character has travelled
     * @param difficultConstant // how much the equip can upgrade according to mode
     **/
    public Shield(Pair<Integer, Integer> position, int n, double difficultConstant) {
        super(position);
        super.setStatus(new Status(0, 0, 10));
        super.increaseItemAttribute(n, difficultConstant);
        super.setEntityView(super.loadImage("images/shield.png"));
        super.setValidPosX(3);
        super.setValidPosY(0);
    }
}
