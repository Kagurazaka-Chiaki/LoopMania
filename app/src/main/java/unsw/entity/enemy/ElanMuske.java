package unsw.entity.enemy;

import javafx.scene.image.Image;
import unsw.entity.AttackStatus;
import unsw.entity.BasicEnemy;
import unsw.entity.behaviour.ElanMuskeHeal;
import unsw.entity.behaviour.NormalAttack;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

public class ElanMuske extends BasicEnemy {
    /**
     * Constructor for ElanMuske
     *
     * @param position //
     */
    public ElanMuske(PathPosition position, LoopManiaWorld world) {
        super(position, world);
        Image ElanMuskeView = super.loadImage("images/ElanMuske.png");
        super.setEntityView(ElanMuskeView);
        

        this.setSupportRadius(1);
        this.setBattleRadius(1);
        super.setStatus(new AttackStatus(24, 100, 14));
        this.setType("ElanMuske");
        super.getStatus().upgrade(world.getCycle().get(), world.getDifficultyConstant());
    
        super.getStatus().addAttackBehaviour(new NormalAttack(world, this));
        super.getStatus().addAttackBehaviour(new ElanMuskeHeal(world));
    }
}
