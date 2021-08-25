package unsw.entity.enemy;

import org.javatuples.Pair;
import unsw.GameRandom;
import unsw.entity.AttackStatus;
import unsw.entity.BasicEnemy;
import unsw.entity.StaticEntity;
import unsw.entity.behaviour.AttackBehaviour;
import unsw.entity.behaviour.NormalAttack;
import unsw.loopmania.BattleSystem;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

import java.util.List;

public class AlliedSoldier extends StaticEntity {
    private AttackStatus status;
    private String name;

    private final LoopManiaWorld world;
    private final BattleSystem battleSystem;

    /**
     * Constructor for AlliedSoldier
     *
     * @param position //
     * @param world    //
     */
    public AlliedSoldier(Pair<Integer, Integer> position, LoopManiaWorld world) {
        super(position);
        this.world = world;
        this.battleSystem = world.getBattleSystem();
        this.status = new AttackStatus(10, 50, 5);
        status.addAttackBehaviour(new NormalAttack(world));
        status.upgrade(world.getCycle().get(), world.getDifficultyConstant());
        super.setEntityView(super.loadImage("images/deep_elf_master_archer.png"));
        //attackSpeed = 0.6;
    }

    /**
     * Method that process the enemy attack alliedSoldier, and decrease its health
     *
     * @param damage //
     */
    public void BeAttacked(double damage) {
        double preHealth = status.getCurrentHealthValue();
        double finalDamage = damage - status.getDefense();
        if (finalDamage < 0) { finalDamage = 0.0; }
        double currHealth = preHealth - finalDamage;
        status.setCurrentHealth(currHealth);
    }

    /**
     * Method that process the allied soldier to deal damage to enemy
     *
     * @param battleEnemies //
     * @return //
     */
    public BasicEnemy chooseAttack(List<BasicEnemy> battleEnemies) {
        int numOfEnemies = battleEnemies.size();
        int targetNum = GameRandom.random.nextInt(numOfEnemies);
        BasicEnemy target = battleEnemies.get(targetNum);
        double damage = status.getStrength();
        AttackBehaviour normalAttack = status.getAttackBehaviour().get(0);
        normalAttack.attack(damage, target);
        return target;
    }

    public BasicEnemy BeInfected(PathPosition position, double difficultyConstant, int round) {
        return new Zombie(position, this.world);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    public LoopManiaWorld getWorld() {
        return this.world;
    }

    public BattleSystem getBattleSystem() {
        return battleSystem;
    }

    public void setStatus(AttackStatus status) {
        this.status = status;
    }

    public AttackStatus getStatus() {
        return status;
    }

    /**
     * Getter for name
     *
     * @return //
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name
     *
     * @param name //
     */
    public void setName(String name) {
        this.name = name;
    }
}
