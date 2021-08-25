package unsw.entity;

import org.javatuples.Pair;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a non-moving entity unlike the moving entities, this can be placed
 * anywhere on the game map
 */
public abstract class StaticEntity extends Entity {
    /**
     * x and y coordinates represented by IntegerProperty, so ChangeListeners can be
     * added
     */
    private final IntegerProperty x;
    private final IntegerProperty y;

    private Integer validPosX;
    private Integer validPosY;

    /**
     * @param position //
     */
    public StaticEntity(Pair<Integer, Integer> position) {
        super();
        this.x = new SimpleIntegerProperty(position.getValue0());
        this.y = new SimpleIntegerProperty(position.getValue1());
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Method that return x
     */
    public IntegerProperty x() {
        return x;
    }

    /**
     * Method that return y
     */
    public IntegerProperty y() {
        return y;
    }

    /**
     * Getter that get value in x
     */
    public int getX() {
        return x().get();
    }

    /**
     * Getter that get value in y
     */
    public int getY() {
        return y().get();
    }

    /**
     * Setter that set validPosX, which used to determine is the item allow to equip in the cell
     *
     * @param x //
     */
    public void setValidPosX(Integer x) {
        this.validPosX = x;
    }

    /**
     * Setter that set validPosY, which used to determine is the item allow to equip in the cell
     *
     * @param y //
     */
    public void setValidPosY(Integer y) {
        this.validPosY = y;
    }

    /**
     * Getter that get validPosX
     *
     * @return //
     */
    public Integer getValidPosX() {
        return this.validPosX;
    }

    /**
     * Getter that get validPosY
     *
     * @return //
     */
    public Integer getValidPosY() {
        return this.validPosY;
    }
}
