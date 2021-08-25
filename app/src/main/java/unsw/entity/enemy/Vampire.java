package unsw.entity.enemy;

import javafx.scene.image.Image;
import unsw.entity.AttackStatus;
import unsw.entity.BasicEnemy;
import unsw.entity.behaviour.NormalAttack;
import unsw.entity.behaviour.VampireCritialBite;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

public class Vampire extends BasicEnemy {

    /**
     * Constructor for Vampire
     *
     * @param position //
     */
    public Vampire(PathPosition position, LoopManiaWorld world) {
        super(position, world);

        Image vampireView = super.loadImage("images/vampire.png");
        super.setEntityView(vampireView);

        this.setSupportRadius(3);
        this.setBattleRadius(2);
        super.setStatus(new AttackStatus(16, 60, 12));
        this.setSpeed(1);
        this.setEXP(100);
        this.setType("Vampire");
        super.getStatus().upgrade(world.getCycle().get(), world.getDifficultyConstant());

        super.getStatus().addAttackBehaviour(new NormalAttack(world, this));
        super.getStatus().addAttackBehaviour(new VampireCritialBite(world));
    }

}
