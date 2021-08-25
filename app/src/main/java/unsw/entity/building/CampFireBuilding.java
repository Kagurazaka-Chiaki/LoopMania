package unsw.entity.building;

import javafx.scene.image.Image;
import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.type.BuildingType;

/**
 * <p>
 * Class {@code CampFireBuilding}
 * </p>
 * <p>
 * Character deals double damage within campfire battle radius
 *
 * @see unsw.entity.building.CampFireBuilding
 * @since 1.0
 **/
public class CampFireBuilding extends Building {

    /**
     * <p>
     * Constructor {@code CampFireBuilding}
     * </p>
     * <p>
     * Initialises the new CampFireBuilding with the given parameters
     *
     * @param position //
     **/
    public CampFireBuilding(Pair<Integer, Integer> position) {
        super(position);

        super.setBuildingType(BuildingType.CampfireBuilding);

        Image campFireBuildingView = super.loadImage("images/campfire.png");
        super.setEntityView(campFireBuildingView);
    }
}
