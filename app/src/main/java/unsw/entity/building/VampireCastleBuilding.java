package unsw.entity.building;

import javafx.scene.image.Image;
import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.type.BuildingType;

/**
 * <p>
 * Class {@code VampireCastleBuilding}
 * </p>
 * <p>
 * a basic form of building in the world
 *
 * @see unsw.entity.building.VampireCastleBuilding
 * @since 1.0
 **/
public class VampireCastleBuilding extends Building {

    /**
     * <p>
     * Constructor {@code VampireCastleBuilding}
     * </p>
     * <p>
     * Initialises the new VampireCastleBuilding with the given parameters
     *
     * @param position //
     **/
    public VampireCastleBuilding(Pair<Integer, Integer> position) {
        super(position);

        super.setBuildingType(BuildingType.VampireCastleBuilding);

        Image zombiePitBuildingView = super.loadImage("images/vampire_castle_building_purple_background.png");
        super.setEntityView(zombiePitBuildingView);
    }
}
