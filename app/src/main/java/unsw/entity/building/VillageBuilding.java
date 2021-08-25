package unsw.entity.building;

import javafx.scene.image.Image;
import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.type.BuildingType;

/**
 * <p>
 * Class {@code VillageBuilding}
 * </p>
 * <p>
 * Character regains health when passing through
 *
 * @see unsw.entity.building.VillageBuilding
 * @since 1.0
 **/
public class VillageBuilding extends Building {

    /**
     * <p>
     * Constructor {@code VillageBuilding }
     * </p>
     * <p>
     * Initialises the new VillageBuilding with the given parameters
     *
     * @param position //
     **/
    public VillageBuilding(Pair<Integer, Integer> position) {
        super(position);

        super.setBuildingType(BuildingType.VillageBuilding);

        Image villageBuildingView = super.loadImage("images/village.png");
        super.setEntityView(villageBuildingView);
    }

}
