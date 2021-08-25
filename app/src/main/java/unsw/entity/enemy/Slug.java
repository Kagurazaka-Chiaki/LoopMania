package unsw.entity.enemy;

import javafx.scene.image.Image;
import unsw.entity.AttackStatus;
import unsw.entity.BasicEnemy;
import unsw.entity.behaviour.NormalAttack;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

public class Slug extends BasicEnemy {

    /**
     * Constructor for Slug
     *
     * @param position //
     */
    public Slug(PathPosition position, LoopManiaWorld world) {
        super(position, world);

        Image slugView = super.loadImage("images/slug.png");
        super.setEntityView(slugView);

        this.setSupportRadius(1);
        this.setBattleRadius(1);
        super.setStatus(new AttackStatus(11, 20, 3));
        this.setSpeed(0.5);
        this.setEXP(25);
        this.setType("Slug");
        super.getStatus().upgrade(world.getCycle().get(), world.getDifficultyConstant());

        super.getStatus().addAttackBehaviour(new NormalAttack(world, this));
    }

}
