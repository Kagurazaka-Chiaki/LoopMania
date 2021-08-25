package unsw.entity.enemy;

import javafx.scene.image.Image;
import unsw.entity.AttackStatus;
import unsw.entity.BasicEnemy;
import unsw.entity.behaviour.DoogieStun;
import unsw.entity.behaviour.NormalAttack;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;

public class Doggie extends BasicEnemy {
    /**
     * Constructor for Doggie
     *
     * @param position //
     */
    public Doggie(PathPosition position, LoopManiaWorld world) {
        super(position, world);
        
        Image doogieView = super.loadImage("images/doggie.png");
        super.setEntityView(doogieView);
        

        this.setSupportRadius(1);
        this.setBattleRadius(1);
        super.setStatus(new AttackStatus(22, 85, 13));
        this.setType("Doggie");
        super.getStatus().upgrade(world.getCycle().get(), world.getDifficultyConstant());
    
        super.getStatus().addAttackBehaviour(new NormalAttack(world, this));
        super.getStatus().addAttackBehaviour(new DoogieStun(world));
    }
}
