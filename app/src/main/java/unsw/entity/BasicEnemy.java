package unsw.entity;

import unsw.GameRandom;
import unsw.entity.behaviour.AttackBehaviour;
import unsw.entity.enemy.AlliedSoldier;
import unsw.loopmania.BattleSystem;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

/**
 * a basic form of enemy in the world
 */
public class BasicEnemy extends MovingEntity {

    private double supportRadius;
    private double battleRadius;
    private double speed;

    private int exp;
    private String type;
    private final BattleSystem battleSystem;

    /**
     * Constructor for BasicEnemy
     *
     * @param position //
     */
    public BasicEnemy(PathPosition position, LoopManiaWorld world) {
        super(position);
        this.battleSystem = world.getBattleSystem();
    }

    /**
     * Method that allow the enemy to attack character
     *
     * @param target //
     * @return int
     */
    public int ChooseAttack(Character target) {
        AttackStatus status = super.getStatus();
        if (status.getAttackBehaviour().size() == 1) {
            AttackBehaviour currBehaviour = status.getAttackBehaviour().get(0);
            return currBehaviour.attack(status.getStrength(), target);
        } else {
            int temp = GameRandom.random.nextInt(5);
            AttackBehaviour currBehaviour;
            if (temp <= 3) {
                currBehaviour = status.getAttackBehaviour().get(0);
            } else {
                currBehaviour = status.getAttackBehaviour().get(1);
            }
            return currBehaviour.attack(status.getStrength(), target);
        }
    }

    /**
     * Method that allow the enemy to attack alliedSoldier
     *
     * @param target //
     * @return int
     */
    public int ChooseAttack(AlliedSoldier target, int sequence) {
        AttackStatus status = super.getStatus();
        if (status.getAttackBehaviour().size() == 1) {
            AttackBehaviour currBehaviour = status.getAttackBehaviour().get(0);
            return currBehaviour.attack(status.getStrength(), target, sequence);
        } else {
            if (GameRandom.random.nextInt(5) <= 3) {
                AttackBehaviour currBehaviour = status.getAttackBehaviour().get(0);
                return currBehaviour.attack(status.getStrength(), target, sequence);
            } else {
                AttackBehaviour currBehaviour = status.getAttackBehaviour().get(1);
                return currBehaviour.attack(status.getStrength(), target, sequence);
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Setter for type
     *
     * @param type //
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Method that move
     */
    public void move() {
        // enemy behaviour
        // this basic enemy moves in a random direction... 25% chance up or down, 50%
        // chance not at all...
        int directionChoice = GameRandom.random.nextInt(4);
        if (directionChoice == 0) {
            moveUpPath();
        } else if (directionChoice == 1) { moveDownPath(); }
    }

    /**
     * Setter for supportRadius
     *
     * @param r //
     */
    public void setSupportRadius(double r) {
        this.supportRadius = r;
    }

    /**
     * Setter for battleRadius
     *
     * @param r //
     */
    public void setBattleRadius(double r) {
        this.battleRadius = r;
    }

    /**
     * Setter for speed
     *
     * @param speed //
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Setter for exp
     *
     * @param exp //
     */
    public void setEXP(int exp) {
        this.exp = exp;
    }

    /**
     * Getter for exp
     *
     * @return exp
     */
    public int getEXP() {
        return exp;
    }

    /**
     * Getter for supportRadius
     *
     * @return supportRadius
     */
    public double getSupportRadius() {
        return this.supportRadius;
    }

    /**
     * Getter for battleRadius
     *
     * @return battleRadius
     */
    public double getBattleRadius() {
        return this.battleRadius;
    }

    /**
     * Getter for speed
     *
     * @return speed
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Getter for type
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Getter that get battle system
     *
     * @return //
     */
    public BattleSystem getBattleSystem() {
        return battleSystem;
    }
}
