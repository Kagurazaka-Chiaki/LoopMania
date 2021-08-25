package unsw.entity.item;

import org.javatuples.Pair;
import unsw.entity.Item;

public class MeleeWeapon extends Item {

    /**
     * <p>
     * Constructor {@code MeleeWeapon}
     * </p >
     * <p>
     * Initialises the MeleeWeapon which is a superclass for weapons
     *
     * @param position // the position in the item list
     **/
    public MeleeWeapon(Pair<Integer, Integer> position) {
        super(position);
        super.setBuyPrice(100);
        super.setSellPrice(50);
    }
}
