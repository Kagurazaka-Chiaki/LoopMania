package unsw.mode;

import org.javatuples.Pair;

import unsw.entity.Item;

public interface Mode {
    double getDifficultyConstant();

    int amountOfItemCanBeBought();

    /**
     * <p>
     * Method {@code Mode}
     * </p >
     * <p>
     * abstract method that restrict different types of item according to different mode
     * with given argument
     *
     * @param item //
     **/
    boolean restrictedAmount(Item item);

    Item addRareItem(Pair<Integer, Integer> availableSlot, int cycle);
}
