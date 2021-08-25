package unsw.entity.card;

import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.entity.Card;
import unsw.entity.building.VillageBuilding;

/**
 * <p>
 * Class {@code VillageCard}
 * </p>
 *
 * <p>
 * represents a vampire castle card in the backend game world
 * </p>
 *
 * @see unsw.entity.card.VillageCard
 * @since 1.0
 **/
public class VillageCard extends Card {

    /**
     * <p>
     * Constructor {@code VillageCard}
     * </p>
     * <p>
     * Initialises the new VillageCard with the given parameters
     *
     * @param position //
     **/
    public VillageCard(Pair<Integer, Integer> position) {
        super(position);
        super.setCardName("VillageCard");

        setPath(true);
        setNearPath(false);
        setWild(false);

        super.setEntityView(super.loadImage("images/village_card.png"));
    }

    @Override
    public Building convertToBuilding(Pair<Integer, Integer> position) {
        return new VillageBuilding(position);
    }
}
