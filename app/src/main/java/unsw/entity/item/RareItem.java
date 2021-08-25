package unsw.entity.item;

import org.javatuples.Pair;
import unsw.entity.Item;

public class RareItem extends Item {

    /**
     * <p>
     * Constructor {@code RareItem}
     * </p >
     * <p>
     * Initialises the RareItem which is hard to get
     *
     * @param position // the position in the item list
     **/
    public RareItem(Pair<Integer, Integer> position) {
        super(position);
        super.setSellPrice(200);
    }
}
