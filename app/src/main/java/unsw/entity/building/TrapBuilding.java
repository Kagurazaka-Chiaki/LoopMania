package unsw.entity.building;

import javafx.scene.image.Image;
import org.javatuples.Pair;
import unsw.entity.BasicEnemy;
import unsw.entity.Building;
import unsw.type.BuildingType;

/**
 * <p>
 * Class {@code TrapBuilding}
 * </p>
 * <p>
 * Trap that can damage enemies and can be only placed on the path
 *
 * @see unsw.entity.building.TrapBuilding
 * @since 1.0
 **/
public class TrapBuilding extends Building {

    /**
     * <p>
     * Constructor {@code TrapBuilding}
     * </p>
     * <p>
     * Initialises the new TrapBuilding with the given parameters
     *
     * @param position //
     **/
    public TrapBuilding(Pair<Integer, Integer> position) {
        super(position);

        super.setBuildingType(BuildingType.TrapBuilding);

        Image trapBuildingView = super.loadImage("images/trap.png");
        super.setEntityView(trapBuildingView);
    }

    /**
     * cause damage to enemy
     **/
    public void damageEnemy(BasicEnemy enemy) {
        double hp = enemy.getStatus().getHealth();
        double currentHp = enemy.getStatus().getCurrentHealthValue();
        double afterDamage = currentHp - hp * 0.15;
        enemy.getStatus().setCurrentHealth(afterDamage);
    }
}
