package unsw.entity.enemy;

import javafx.scene.image.Image;
import unsw.entity.AttackStatus;
import unsw.entity.BasicEnemy;
import unsw.entity.behaviour.NormalAttack;
import unsw.entity.behaviour.ThiefSteal;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

public class Thief extends BasicEnemy {

    /**
     * Constructor for Thief
     *
     * @param position           //
     */
    public Thief(PathPosition position, LoopManiaWorld world) {
        super(position, world);

        Image thiefView = super.loadImage("images/thief.png");
        super.setEntityView(thiefView);

        this.setSupportRadius(2);
        this.setBattleRadius(1);
        super.setStatus(new AttackStatus(1, 50, 8));
        this.setSpeed(1);
        this.setEXP(50);
        this.setType("Thief");
        super.getStatus().upgrade(world.getCycle().get(), world.getDifficultyConstant());

        super.getStatus().addAttackBehaviour(new NormalAttack(world, this));
        super.getStatus().addAttackBehaviour(new ThiefSteal(world));
    }

}
