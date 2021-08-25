package unsw.entity.enemy;

import javafx.scene.image.Image;
import unsw.entity.AttackStatus;
import unsw.entity.BasicEnemy;
import unsw.entity.behaviour.NormalAttack;
import unsw.entity.behaviour.ZombieCriticalBite;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

public class Zombie extends BasicEnemy {

    /**
     * Constructor for Zombie
     *
     * @param position //
     */
    public Zombie(PathPosition position, LoopManiaWorld world) {
        super(position, world);

        Image zombieView = super.loadImage("images/zombie.png");
        super.setEntityView(zombieView);

        this.setSupportRadius(1);
        this.setBattleRadius(2);
        super.setStatus(new AttackStatus(20, 40, 8));
        this.setSpeed(0.2);
        this.setEXP(50);
        this.setType("Zombie");
        super.getStatus().upgrade(world.getCycle().get(), world.getDifficultyConstant());

        super.getStatus().addAttackBehaviour(new NormalAttack(world, this));
        super.getStatus().addAttackBehaviour(new ZombieCriticalBite(world));
    }

}
