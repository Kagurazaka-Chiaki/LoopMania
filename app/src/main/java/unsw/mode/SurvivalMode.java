package unsw.mode;

import org.javatuples.Pair;

import unsw.entity.Item;
import unsw.entity.item.Anduril;
import unsw.entity.item.TheOneRing;
import unsw.entity.item.TreeStump;
import unsw.type.RareItemType;
import unsw.entity.item.HealthPotion;

/**
 * <p>
 * Class {@code  SurvivalMode}
 * </p>
 * <p>
 * <p>
 * Game mode Survival
 *
 * @see unsw.mode.SurvivalMode
 * @since 1.0
 **/
public class SurvivalMode implements Mode {
    private final double difficultyConstant;
    private final int amountOfItemCanBeBought;

    /**
     * <p>
     * Constructor {@code StandardMode}
     *
     * </p >
     * <p>
     * Initialise the medium difficulty game mode which named SurvivalMode
     **/
    public SurvivalMode() {
        this.difficultyConstant = 1.05;
        this.amountOfItemCanBeBought = 1;
    }

    /**
     * <p>
     * Getter for {@code difficultyConstant} in Class {@code SurvivalMode}
     * </p>
     *
     * @return difficultyConstant
     **/
    public double getDifficultyConstant() {
        return difficultyConstant;
    }

    /**
     * some item can only be buy one in survival mode
     */
    public int amountOfItemCanBeBought() {
        return amountOfItemCanBeBought;
    }

    /**
     * number of potion will be restricted in survival mode
     */
    public boolean restrictedAmount(Item item) {
        return isHealthPotion(item);
    }

    /**
     * return whether the item is health potion
     **/
    private boolean isHealthPotion(Item item) {
        return HealthPotion.class.equals(item.getClass());
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
