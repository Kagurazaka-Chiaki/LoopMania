package unsw.entity.item;

import org.javatuples.Pair;

import unsw.entity.Status;

public class Armour extends ProtectiveEquipment {

    /**
     * <p>
     * Constructor {@code Armour}
     *
     * </p>
     * <p>
     * Initialises the equipment of type Armour which provides extra
     * health for the character with given arguments
     *
     * @param position          // the position in the item list
     * @param n                 // the cycles character has travelled
     * @param difficultConstant // how much the equip can upgrade according to mode
     **/
    public Armour(Pair<Integer, Integer> position, int n, double difficultConstant) {
        super(position);
        super.setStatus(new Status(0, 50, 0));
        super.increaseItemAttribute(n, difficultConstant);
        super.setEntityView(super.loadImage("images/armour.png"));
        super.setValidPosX(2);
        super.setValidPosY(0);
    }
}
