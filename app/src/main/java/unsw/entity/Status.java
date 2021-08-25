package unsw.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * Status class is used to store status of item
 */
public class Status {
    private final DoubleProperty strength;
    private final DoubleProperty health;
    private final DoubleProperty defense;
    private double baseHealth;
    private double baseStrength;
    private double baseDefense;

    /**
     * Constructor for Status
     *
     * @param strength //
     * @param health   //
     * @param defense  //
     */
    public Status(double strength, double health, double defense) {
        this.strength = new SimpleDoubleProperty(strength);
        this.health = new SimpleDoubleProperty(health);
        this.defense = new SimpleDoubleProperty(defense);
        setBaseStrength(strength);
        setBaseHealth(health);
        setBaseDefense(defense);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Method that return health
     *
     * @return //
     */
    public DoubleProperty health() {
        return health;
    }

    /**
     * Getter for double in health
     *
     * @return //
     */
    public double getHealth() {
        return health.get();
    }

    public void setHealth(double value) {
        this.health.set(value);
    }

    /**
     * Method that return defense
     *
     * @return //
     */
    public DoubleProperty defense() {
        return defense;
    }

    /**
     * Getter for double in defense
     *
     * @return //
     */
    public double getDefense() {
        return defense.get();
    }

    public void setDefense(double value) {
        this.defense.set(value);
    }

    /**
     * Method that return strength
     *
     * @return //
     */
    public DoubleProperty strength() {
        return strength;
    }

    /**
     * Getter that get double in strength
     *
     * @return //
     */
    public double getStrength() {
        return strength.get();
    }

    /**
     * Setter that set the strength value
     */
    public void setStrength(double value) {
        this.strength.set(value);
    }

    /**
     * Setter that set the base health value
     *
     * @param value //
     */
    public void setBaseHealth(double value) {
        this.baseHealth = value;
    }

    /**
     * Getter that get base health
     *
     * @return //
     */
    public Double getBaseHealth() {
        return baseHealth;
    }

    /**
     * Setter that set base strength value
     *
     * @param value //
     */
    public void setBaseStrength(double value) {
        this.baseStrength = value;
    }

    /**
     * Getter that get base strength
     *
     * @return //
     */
    public Double getBaseStrength() {
        return baseStrength;
    }

    /**
     * Setter that set base defense value
     *
     * @param value //
     */
    public void setBaseDefense(double value) {
        this.baseDefense = value;
    }

    /**
     * Getter that get base defense
     *
     * @return //
     */
    public Double getBaseDefense() {
        return baseDefense;
    }
}
