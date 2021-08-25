package unsw.mode;

import org.javatuples.Pair;

import unsw.entity.Item;
import unsw.entity.item.Anduril;
import unsw.entity.item.TheOneRing;
import unsw.entity.item.TreeStump;
import unsw.type.RareItemType;

/**
 * <p>
 * Class {@code  StandardMode}
 * </p>
 * <p>
 * Game mode standard
 *
 * @see unsw.mode.StandardMode
 * @since 1.0
 **/
public class StandardMode implements Mode {
    private final double difficultyConstant;
    private final int amountOfItemCanBeBought;

    /**
     * <p>
     * Constructor {@code StandardMode}
     * </p >
     * <p>
     * Initialises the easiest game mode which named StandardMode
     **/
    public StandardMode() {
        this.difficultyConstant = 1;
        this.amountOfItemCanBeBought = -1;
    }

    public double getDifficultyConstant() {
        return difficultyConstant;
    }

    /**
     * unlimited number of item can be bought in standard mode
     */
    public int amountOfItemCanBeBought() {
        return amountOfItemCanBeBought;
    }

    /**
     * no restriction in standard mode
     */
    public boolean restrictedAmount(Item item) {
        return false;
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
