package unsw.entity.building;

import javafx.scene.image.Image;
import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.type.BuildingType;

/**
 * <p>
 * Class {@code BarracksBuilding}
 * </p>
 *
 * <p>
 * Barracks can spawn allied soldier
 * </p>
 * <p>
 * Produces allied soldier to join Character when passes through
 * </p>
 *
 * @see unsw.entity.building.BarracksBuilding
 * @since 1.0
 **/
public class BarracksBuilding extends Building {

    /**
     * <p>
     * Constructor {@code BarrcaksBuilding}
     * </p>
     * <p>
     * Initialises the new BarrcaksBuilding with the given parameters
     *
     * @param position //
     **/
    public BarracksBuilding(Pair<Integer, Integer> position) {
        super(position);

        super.setBuildingType(BuildingType.BarracksBuilding);

        Image barracksBuildingView = super.loadImage("images/barracks.png");
        super.setEntityView(barracksBuildingView);

    }

}
