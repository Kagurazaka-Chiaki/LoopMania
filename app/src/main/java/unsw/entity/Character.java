package unsw.entity;

import javafx.beans.property.IntegerProperty;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import unsw.loopmania.BattleSystem;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.GameRandom;
import unsw.entity.behaviour.AttackBehaviour;
import unsw.entity.behaviour.NormalAttack;
import unsw.entity.enemy.AlliedSoldier;
import unsw.entity.item.DoggieCoin;
import unsw.entity.item.Gold;

/**
 * represents the main character in the backend of the game world
 */
public class Character extends MovingEntity {
    private final Gold gold;
    private Item weapon;
    private Item shield;
    private int stunCount;

    private final SimpleBooleanProperty ifBuff;
    private final IntegerProperty level;
    private final IntegerProperty exp;
    private final List<AlliedSoldier> alliedSoldiers;
    private final BattleSystem battleSystem;
    private final DoggieCoin doggieCoin;

    /**
     * Constructor for Character
     *
     * @param position //
     */
    public Character(PathPosition position, LoopManiaWorld world) {
        super(position);

        this.battleSystem = world.getBattleSystem();
        this.ifBuff = new SimpleBooleanProperty(false);
        super.setStatus(new AttackStatus(15, 250, 8));
        super.getStatus().addAttackBehaviour(new NormalAttack(world));
        this.level = new SimpleIntegerProperty(1);
        this.exp = new SimpleIntegerProperty(0);
        gold = new Gold(Pair.with(0, 0), 0);
        alliedSoldiers = new ArrayList<>();
        this.weapon = null;
        this.shield = null;
        this.stunCount = 0;
        this.doggieCoin = new DoggieCoin(Pair.with(0, 0), 250);
    }

    /**
     * Method that adds the status to character from equippedItem
     *
     * @param item //
     */
    public void attributeGainByItem(Item item) {
        AttackStatus status = super.getStatus();
        Status itemStatus = item.getStatus();
        status.setStrength(status.getStrength() + itemStatus.getStrength());
        status.setHealth(status.getHealth() + itemStatus.getHealth());
        status.setCurrentHealth(status.getCurrentHealthValue() + itemStatus.getHealth());
        status.setDefense(status.getDefense() + itemStatus.getDefense());
    }

    /**
     * Method that minus the status to character from equippedItem
     *
     * @param item //
     */
    public void attributeLossByItem(Item item) {
        AttackStatus status = super.getStatus();
        Status itemStatus = item.getStatus();
        status.setStrength(status.getStrength() - itemStatus.getStrength());
        status.setHealth(status.getHealth() - itemStatus.getHealth());
        status.setDefense(status.getDefense() - itemStatus.getDefense());
        if (status.getCurrentHealthValue() > status.getHealth()) {
            status.setCurrentHealth(status.getHealth());
        }
    }

    /**
     * Method that allow the character to attack enemies
     *
     * @return //
     */
    public BasicEnemy chooseAttack() {
        AttackStatus status = super.getStatus();
        int numOfEnemies = battleSystem.getBattleEnemies().size();
        int targetNum = GameRandom.random.nextInt(numOfEnemies);
        BasicEnemy target = battleSystem.getBattleEnemies().get(targetNum);
        getBattleInfo().append("##################################################\n");
        if (getStunCount() > 0) {
            addStunCount(-1);
            getBattleInfo().append("the Character can not attack cause it is stunned by the doggie!!! \n");
        } else {
            double normalStrength = status.getStrength();
            double normalDefense = status.getDefense();
            getBattleInfo().append(battleSystem.enemyInfo(target));
            boolean abilityTrigger = false;
            if (weapon != null) {
                abilityTrigger = weapon.useAbility(this, target);
            }
            if (!abilityTrigger && shield != null) { shield.useAbility(this, target); }

            double damage = status.getStrength();
            if (this.getIfBuff()) {
                damage = 2 * damage - target.getStatus().getDefense();
            }
            AttackBehaviour normalAttack = status.getAttackBehaviour().get(0);
            normalAttack.attack(damage, target);
            if (target.getStatus().isAlive()) {
                getBattleInfo().append("Character ---- attack ---->  ").append(target.getType())
                               .append((" Current HP: ")) //
                               .append(mathRound(target.getStatus().getCurrentHealthValue(), 2)) //
                               .append("\n");
            }

            status.setStrength(normalStrength);
            status.setDefense(normalDefense);
        }
        return target;
    }

    private static double mathRound(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    /**
     * Method that adds exp to character
     *
     * @param defeatedEnemies //
     */
    public void gainEXP(List<BasicEnemy> defeatedEnemies) {
        int totalExp = 0;
        for (BasicEnemy e : defeatedEnemies) {
            totalExp = totalExp + e.getEXP();
        }

        int levelUpCounter = (getExpValue() % 200) + totalExp;

        while (levelUpCounter >= 200) {
            levelUpCounter -= 200;
            this.upgrade();
        }
        int newEXP = getExpValue() + totalExp;
        getEXP().set(newEXP);
    }

    /**
     * Method that adds status to character when upgrade
     */
    public void upgrade() {
        AttackStatus status = super.getStatus();
        int level = getLevelValue();
        getLevel().set(level + 1);
        status.setDefense(status.getDefense() + 3);
        status.setStrength(status.getStrength() + 8);
        status.setHealth(status.getHealth() + 50);
        status.setCurrentHealth(status.getCurrentHealthValue() + 50);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Getter that get battle system
     *
     * @return //
     */
    public BattleSystem getBattleSystem() {
        return this.battleSystem;
    }

    /**
     * Getter that get battle info
     *
     * @return //
     */
    private StringBuilder getBattleInfo() {
        return battleSystem.getBattleInfo();
    }

    /**
     * Getter that get stun count
     *
     * @return //
     */
    public int getStunCount() {
        return stunCount;
    }

    /**
     * Setter that add stun count
     *
     * @param duration //
     */
    public void addStunCount(int duration) {
        this.stunCount = this.stunCount + duration;
    }

    /**
     * Getter that get IfBuff
     *
     * @return //
     */
    public SimpleBooleanProperty IfBuff() {
        return ifBuff;
    }

    /**
     * Getter that get the boolean in IfBuff
     *
     * @return //
     */
    public boolean getIfBuff() {
        return ifBuff.get();
    }

    /**
     * Getter that get level
     *
     * @return //
     */
    public IntegerProperty getLevel() {
        return level;
    }

    /**
     * Getter that get the int in level
     *
     * @return //
     */
    public int getLevelValue() {
        return level.get();
    }

    /**
     * Getter that get exp
     *
     * @return //
     */
    public IntegerProperty getEXP() {
        return exp;
    }

    /**
     * Getter that get the int in exp
     *
     * @return //
     */
    public int getExpValue() {
        return exp.get();
    }

    /**
     * Getter that get gold
     *
     * @return //
     */
    public IntegerProperty getGold() {
        return gold.getGold();
    }

    /**
     * Getter that get doggie coin amount owned
     *
     * @return //
     */
    public IntegerProperty getDoggieCoinAmount() {
        return doggieCoin.getAmountOwned();
    }

    /**
     * Getter that get doggie coin amount owned as int
     *
     * @return //
     */
    public int getDoggieCoinValue() {
        return doggieCoin.getAmountOwnedValue();
    }

    /**
     * Setter that add the doggie coin owned by one
     */
    public void addDoggieCoin() {
        this.doggieCoin.addAmountOwned();
    }

    /**
     * Getter that get doggie coin price
     *
     * @return //
     */
    public IntegerProperty getDoggieCoinPrice() {
        return doggieCoin.getGold();
    }

    /**
     * Setter that add the doggie price
     *
     * @param i //
     */
    public void addDoggieCoinPrice(int i) {
        doggieCoin.setGold(getDoggiePriceValue() + i);
    }

    /**
     * Getter that get doggie coin price as int
     */
    public void setDoggieCoinPrice(int i) {
        doggieCoin.setGold(i);
    }

    /**
     * Setter that set the value in gold
     *
     * @param i //
     */
    public void setGold(int i) {
        gold.setGold(i);
    }

    /**
     * Getter that get the int in gold
     *
     * @return //
     */
    public int getGoldValue() {
        return this.getGold().get();
    }

    public int getDoggiePriceValue() {
        return this.getDoggieCoinPrice().get();
    }

    /**
     * Setter that set the weapon of character equip
     *
     * @param weapon //
     */
    public void setWeapon(Item weapon) {
        this.weapon = weapon;
    }

    /**
     * Getter that get the weapon of character equip
     *
     * @return //
     */
    public Item getWeapon() {
        return this.weapon;
    }

    /**
     * Setter that set the shield of character equip
     *
     * @param shield //
     */
    public void setShield(Item shield) {
        this.shield = shield;
    }

    /**
     * Getter that get the shield of character equip
     *
     * @return //
     */
    public Item getShield() {
        return shield;
    }

    /**
     * Getter that get alliedSoldiers
     *
     * @return //
     */
    public List<AlliedSoldier> getAlliedSoldiers() {
        return alliedSoldiers;
    }

}
