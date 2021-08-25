package unsw.entity.item;

import org.javatuples.Pair;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class DoggieCoin extends Gold {

    private final IntegerProperty amountOwned;

    /**
     * <p>
     * Constructor {@code DoggieCoin}
     * </p >
     * <p>
     * A revolutionary asset type, which randomly fluctuates in sellable price to an extraordinary extent.
     * Can sell at shop
     *
     * @param position // the position in the item list
     **/
    public DoggieCoin(Pair<Integer, Integer> position, int amount) {
        super(position, amount);
        this.amountOwned = new SimpleIntegerProperty(0);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Getter that get the amount doggie coin owned
     *
     * @return //
     */
    public IntegerProperty getAmountOwned() {
        return amountOwned;
    }

    /**
     * Getter that get the amount doggie coin owned as int
     *
     * @return //
     */
    public int getAmountOwnedValue() {
        return amountOwned.get();
    }

    /**
     * Method that add doggie coin amount by one
     */
    public void addAmountOwned() {
        this.amountOwned.set(getAmountOwnedValue() + 1);
    }

}
