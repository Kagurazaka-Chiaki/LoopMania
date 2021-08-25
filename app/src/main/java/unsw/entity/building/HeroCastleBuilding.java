package unsw.entity.building;

import javafx.scene.image.Image;
import org.javatuples.Pair;
import unsw.entity.Building;

/**
 * <p>
 * Class {@code HeroCastleBuilding}
 * </p>
 * <p>
 * The start of the cycle, allied building, also the shop
 *
 * @see unsw.entity.building.HeroCastleBuilding
 * @since 1.0
 **/
public class HeroCastleBuilding extends Building {

    /**
     * <p>
     * Constructor {@code HeroCastleBuilding}
     * </p>
     * <p>
     * Initialises the new HeroCastleBuilding with the given parameters
     *
     * @param position //
     **/
    public HeroCastleBuilding(Pair<Integer, Integer> position) {
        super(position);

        super.setBuildingType(null);

        Image heroCastleBuildingView = super.loadImage("images/heros_castle.png");
        super.setEntityView(heroCastleBuildingView);
    }

}
