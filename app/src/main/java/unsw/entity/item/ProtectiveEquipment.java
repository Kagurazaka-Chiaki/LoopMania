package unsw.entity.item;

import org.javatuples.Pair;
import unsw.entity.Item;

public class ProtectiveEquipment extends Item {

    /**
     * <p>
     * Constructor {@code ProtectiveEquipment}
     * </p >
     * <p>
     * Initialises the ProtectiveEquipment which is a superclass for Armours
     *
     * @param position // the position in the item list
     **/
    public ProtectiveEquipment(Pair<Integer, Integer> position) {
        super(position);
        super.setBuyPrice(100);
        super.setSellPrice(50);
    }
}
