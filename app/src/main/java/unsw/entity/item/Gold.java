package unsw.entity.item;

import org.javatuples.Pair;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.entity.StaticEntity;

public class Gold extends StaticEntity {

    private final IntegerProperty gold;

    /**
     * <p>
     * Constructor {@code Gold}
     * </p >
     * <p>
     * Initialises the gold that character can use them to purchase
     *
     * @param position //
     * @param amount   // the amount of current gold
     **/
    public Gold(Pair<Integer, Integer> position, int amount) {
        super(position);
        this.gold = new SimpleIntegerProperty(amount);
        super.setEntityView(super.loadImage("images/gold_pile.png"));
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Getter that get gold
     * @return
     */
    public IntegerProperty getGold() {
        return this.gold;
    }

    /**
     * Getter that set gold value
     * @param i
     */
    public void setGold(int i) {
        this.gold.set(i);
    }
}
