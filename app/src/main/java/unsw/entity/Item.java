package unsw.entity;

import org.javatuples.Pair;

import javafx.beans.property.IntegerProperty;
import unsw.entity.ability.ItemAbility;
import unsw.entity.ability.RingAbility;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic Building in the world
 */
public class Item extends StaticEntity {
    private Status status;
    private final List<ItemAbility> ability;
    private int buyPrice;
    private int sellPrice;

    /**
     * constructor for item
     *
     * @param position //
     */
    public Item(Pair<Integer, Integer> position) {
        super(position);
        this.ability = new ArrayList<>();
    }

    /**
     * Method that increase the item status according to mode and cycle
     *
     * @param n                 //
     * @param difficultConstant //
     */
    public void increaseItemAttribute(int n, double difficultConstant) {
        status.strength().set(formula(n, difficultConstant, status.getBaseStrength()));
        status.health().set(formula(n, difficultConstant, status.getBaseHealth()));
        status.defense().set(formula(n, difficultConstant, status.getBaseDefense()));
    }

    /**
     * Formula that calculate the increase rate
     *
     * @param n                 //
     * @param difficultConstant //
     * @param s                 //
     * @return //
     */
    private double formula(int n, double difficultConstant, double s) {
        return s * n * (1 + (n - 1) * 0.03) * difficultConstant;
    }

    /**
     * Method that allow character buy item using gold
     *
     * @param gold //
     */
    public void buyItem(IntegerProperty gold) {
        gold.set(gold.get() - buyPrice);
    }

    /**
     * Method that allow character sell item and gain gold
     *
     * @param gold //
     */
    public void sellItem(IntegerProperty gold) {
        gold.set(gold.get() + sellPrice);
    }

    /**
     * Method that use item ability if it has one
     *
     * @param character //
     * @param enemy     //
     * @return //
     */
    public boolean useAbility(Character character, BasicEnemy enemy) {
        for (ItemAbility a : ability) {
            if (a.ability(character, enemy)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method that revive character
     *
     * @param character //
     * @return //
     */
    public boolean revive(Character character) {
        for (ItemAbility a : ability) {
            if (RingAbility.class.equals(a.getClass())) {
                a.ability(character, null);
                destroy();
                return true;
            }
        }
        return false;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Setter that set status
     *
     * @param status //
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Getter that get status
     *
     * @return //
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Setter that add item ability to this item
     *
     * @param itemAbility //
     */
    public void setAbility(ItemAbility itemAbility) {
        this.ability.add(itemAbility);
    }

    /**
     * Getter that get the list of ability
     *
     * @return //
     */
    public List<ItemAbility> getAbility() {
        return ability;
    }

    /**
     * Setter that set the buy price
     *
     * @param price //
     */
    public void setBuyPrice(int price) {
        this.buyPrice = price;
    }

    /**
     * Setter that set sell price
     *
     * @param price //
     */
    public void setSellPrice(int price) {
        this.sellPrice = price;
    }

    /**
     * Getter that get buy price
     *
     * @return //
     */
    public int getBuyPrice() {
        return buyPrice;
    }

    /**
     * Getter that get sell price
     *
     * @return //
     */
    public int getSellPrice() {
        return sellPrice;
    }
}
