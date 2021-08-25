package unsw.entity;

import org.javatuples.Pair;
import unsw.type.BuildingType;

/**
 * Basic Building in the world
 */
public abstract class Building extends StaticEntity {
    private BuildingType buildingType;

    /**
     * Constructor for Building
     *
     * @param position //
     */
    public Building(Pair<Integer, Integer> position) {
        super(position);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Setter that set buildingType
     *
     * @param buildingType //
     */
    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    /**
     * Getter that get buildingType
     *
     * @return //
     */
    public BuildingType getBuildingType() {
        return this.buildingType;
    }
}
