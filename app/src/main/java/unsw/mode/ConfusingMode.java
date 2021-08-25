package unsw.mode;

import org.javatuples.Pair;

import unsw.GameRandom;
import unsw.entity.Item;
import unsw.entity.ability.AndurilAbility;
import unsw.entity.ability.RingAbility;
import unsw.entity.ability.StumpAbility;
import unsw.entity.item.Anduril;
import unsw.entity.item.TheOneRing;
import unsw.entity.item.TreeStump;
import unsw.type.RareItemType;

public class ConfusingMode implements Mode {
    private final double difficultyConstant;
    private final int amountOfItemCanBeBought;
    private int cycle;

    /**
     * <p>
     * Constructor {@code StandardMode}
     * </p >
     * <p>
     * Initialises the game mode which named ConfusingMode
     **/
    public ConfusingMode() {
        this.difficultyConstant = 1.05;
        this.amountOfItemCanBeBought = -1;
    }

    public double getDifficultyConstant() {
        return difficultyConstant;
    }

    /**
     * unlimited number of item can be bought in confusing mode
     */
    public int amountOfItemCanBeBought() {
        return amountOfItemCanBeBought;
    }

    /**
     * no restriction in confusing mode
     */
    public boolean restrictedAmount(Item item) {
        return false;
    }

    public Item addRareItem(Pair<Integer, Integer> availableSlot, int cycle) {
        Item item = null;
        this.cycle = cycle;
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
        addNewStatus(item, itemType);
        return item;
    }

    private void addNewStatus(Item item, RareItemType itemType) {
        int possibility = GameRandom.random.nextInt(100);
        if (possibility < 50) {
            addNewAbility(item, itemType);
        }
    }

    private void addNewAbility(Item item, RareItemType itemType) {
        RareItemType newType = RareItemType.getRandomItemType();
        while (itemType.equals(newType)) {
            newType = RareItemType.getRandomItemType();
        }
        switch (newType) {
            case TheOneRing:
                item.setAbility(new RingAbility());
                break;
            case Anduril:
                if (item.getValidPosX() == null) {
                    item.setValidPosX(0);
                    item.setValidPosY(0);
                }
                item.getStatus().setBaseStrength(10);
                item.increaseItemAttribute(cycle, difficultyConstant);
                item.setAbility(new AndurilAbility());
                break;
            case TreeStump:
                if (item.getValidPosX() == null) {
                    item.setValidPosX(3);
                    item.setValidPosY(0);
                }
                item.getStatus().setBaseDefense(10);
                item.increaseItemAttribute(cycle, difficultyConstant);
                item.setAbility(new StumpAbility());
                break;
            default:
                break;
        }
    }
}
