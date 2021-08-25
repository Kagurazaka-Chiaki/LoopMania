package unsw.entity;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import unsw.entity.behaviour.AttackBehaviour;

import java.util.ArrayList;
import java.util.List;

public class AttackStatus extends Status {
    private final DoubleProperty currentHealth;
    private final List<AttackBehaviour> attackBehaviours;

    /**
     * Constructor for Status
     *
     * @param strength //
     * @param health   //
     * @param defense  //
     */
    public AttackStatus(double strength, double health, double defense) {
        super(strength, health, defense);
        this.currentHealth = new SimpleDoubleProperty(health);
        attackBehaviours = new ArrayList<>();
    }

    /**
     * Method that allow the enemy upgrade and increase status according to the cycle
     *
     * @param round //
     */
    public void upgrade(int round, double DifficultyConstant) {
        double CurrUpgrade = (double) (round) * (1 + (round - 1) * 0.03) * DifficultyConstant;
        setDefense(getBaseDefense() * CurrUpgrade);
        setHealth(getBaseHealth() * CurrUpgrade);
        setCurrentHealth(getBaseHealth() * CurrUpgrade);
        setStrength(getBaseStrength() * CurrUpgrade);
    }

    /**
     * Method that check is the current target alive
     *
     * @return //
     */
    public boolean isAlive() {
        return (getCurrentHealthValue() > 0);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Setter for currentHealth
     *
     * @param value //
     */
    public void setCurrentHealth(double value) {
        this.currentHealth.set(value);
    }

    /**
     * Getter for currentHealth
     *
     * @return //
     */
    public DoubleProperty getCurrentHealth() {
        return currentHealth;
    }

    /**
     * Getter that get the current health as int
     *
     * @return //
     */
    public Double getCurrentHealthValue() {
        return currentHealth.get();
    }

    /**
     * Method that add attackBehaviours to list
     *
     * @param attackBehaviour //
     */
    public void addAttackBehaviour(AttackBehaviour attackBehaviour) {
        this.attackBehaviours.add(attackBehaviour);
    }

    /**
     * Getter that get attackBehaviour
     *
     * @return //
     */
    public List<AttackBehaviour> getAttackBehaviour() {
        return attackBehaviours;
    }
}
