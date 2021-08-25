package unsw.entity.building;

import javafx.scene.image.Image;
import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.type.BuildingType;

/**
 * <p>
 * Class {@code MagicShopBuilding}
 * </p>
 * <p>
 * Character can buy item when he pass MagicShop
 *
 * @see unsw.entity.building.MagicShopBuilding
 * @since 1.0
 **/
public class MagicShopBuilding extends Building {

    /**
     * <p>
     * Constructor {@code MagicShopBuilding}
     * </p>
     * <p>
     * Initialises the new MagicShopBuilding with the given parameters
     *
     * @param position //
     **/
    public MagicShopBuilding(Pair<Integer, Integer> position) {
        super(position);

        super.setBuildingType(BuildingType.MagicShopBuilding);

        Image campFireBuildingView = super.loadImage("images/magicshop.png");
        super.setEntityView(campFireBuildingView);
    }
}
