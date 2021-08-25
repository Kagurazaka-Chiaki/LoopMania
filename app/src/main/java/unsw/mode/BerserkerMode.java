package unsw.mode;

import org.javatuples.Pair;
import unsw.entity.Item;
import unsw.entity.item.Anduril;
import unsw.entity.item.Armour;
import unsw.entity.item.Shield;
import unsw.entity.item.TheOneRing;
import unsw.entity.item.TreeStump;
import unsw.type.RareItemType;
import unsw.entity.item.Helmet;

/**
 * <p>
 * Class {@code  BerserkerMode}
 * </p>
 * <p>
 * <p>
 * Game mode Berserker
 *
 * @see unsw.mode.BerserkerMode
 * @since 1.0
 **/
public class BerserkerMode implements Mode {
    private final double difficultyConstant;
    private final int amountOfItemCanBeBought;

    /**
     * <p>
     * Constructor {@code Berserker Mode}
     * </p>
     * <p>
     * Initialises the new Berserker Mode with the given parameters
     **/
    public BerserkerMode() {
        this.difficultyConstant = 1.05;
        this.amountOfItemCanBeBought = 1;
    }

    /**
     * berserker mode has different difficulty constant from other mode
     */
    public double getDifficultyConstant() {
        return difficultyConstant;
    }

    /**
     * limit the number of item that can be bought in shop
     */
    public int amountOfItemCanBeBought() {
        return amountOfItemCanBeBought;
    }

    /**
     * retune whether the item is restricted
     **/
    public boolean restrictedAmount(Item item) {
        return isProtectiveItem(item);
    }

    /**
     * determine if an item is a protective item
     */
    private boolean isProtectiveItem(Item item) {
        if (Armour.class.equals(item.getClass())) {
            return true;
        } else if (Shield.class.equals(item.getClass())) {
            return true;
        } else
            return Helmet.class.equals(item.getClass());
    }

    public Item addRareItem(Pair<Integer, Integer> availableSlot, int cycle) {
        Item item = null;
        RareItemType itemType = RareItemType.getRandomItemType();
        switch (itemType) {
            case TheOneRing:
                item = new TheOneRing(availableSlot);
                break;
            case Anduril:
                item = new Anduril(availableSlot, cycle, difficultyConstant);
                break;
            case TreeStump:
            item = new TreeStump(availableSlot, cycle, difficultyConstant);
                break;
            default:
                break;
        }
        return item;
    }
}
