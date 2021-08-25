package unsw.entity.building;

import javafx.scene.image.Image;
import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.type.BuildingType;

/**
 * <p>
 * Class {@code ZombiePitBuilding}
 * </p>
 * <p>
 * Produces zombies every cycle of the path completed by the Character, spawning nearby on the path
 *
 * @see unsw.entity.building.ZombiePitBuilding
 * @since 1.0
 **/
public class ZombiePitBuilding extends Building {

    /**
     * <p>
     * Constructor {@code ZombiePitBuilding}
     * </p>
     * <p>
     * Initialises the new ZombiePitBuilding with the given parameters
     *
     * @param position //
     **/
    public ZombiePitBuilding(Pair<Integer, Integer> position) {
        super(position);

        super.setBuildingType(BuildingType.ZombiePitBuilding);

        Image zombiePitBuildingView = super.loadImage("images/zombie_pit.png");
        super.setEntityView(zombiePitBuildingView);
    }
}
