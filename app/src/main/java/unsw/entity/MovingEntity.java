package unsw.entity;

import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;
import unsw.loopmania.PathPosition;

/**
 * The moving entity
 */
public abstract class MovingEntity extends Entity {

    /**
     * object holding position in the path
     */
    private final PathPosition position;
    private AttackStatus status;

    /**
     * Create a moving entity which moves up and down the path in position
     *
     * @param position represents the current position in the path
     */
    public MovingEntity(PathPosition position) {
        super();
        this.position = position;
    }

    /**
     * move clockwise through the path
     */
    public void moveDownPath() {
        this.position.moveDownPath();
    }

    /**
     * move anticlockwise through the path
     */
    public void moveUpPath() {
        this.position.moveUpPath();
    }

    /**
     * Getter that get x
     */
    public SimpleIntegerProperty x() {
        return this.position.getX();
    }

    /**
     * Getter that get y
     */
    public SimpleIntegerProperty y() {
        return this.position.getY();
    }

    /**
     * Getter that get x as int
     */
    public int getX() {
        return x().get();
    }

    /**
     * Getter that get y as int
     */
    public int getY() {
        return y().get();
    }

    /**
     * Getter that get position
     *
     * @return //
     */
    public Pair<Integer, Integer> getPos() {
        return Pair.with(getX(), getY());
    }

    /**
     * Getter that get path position
     *
     * @return //
     */
    public PathPosition getPathPosition() {
        return this.position;
    }

    /**
     * Setter that set status
     *
     * @param status //
     */
    public void setStatus(AttackStatus status) {
        this.status = status;
    }

    /**
     * Getter that get status
     *
     * @return //
     */
    public AttackStatus getStatus() {
        return status;
    }

    /**
     * Method that process the lose of health after being attack
     *
     * @param damage
     */
    public void BeAttacked(double damage) {
        double preHealth = status.getCurrentHealthValue();
        double finalDamage = damage - status.getDefense();
        if (finalDamage < 1) {
            finalDamage = 1;
        }
        double currHealth = preHealth - finalDamage;
        status.setCurrentHealth(currHealth);
    }
}
