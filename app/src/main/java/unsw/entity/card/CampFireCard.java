package unsw.entity.card;

import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.entity.Card;
import unsw.entity.building.CampFireBuilding;

/**
 * <p>
 * Class {@code CampFireCard}
 * </p>
 *
 * <p>
 * The start of the cycle, allied building, also the shop
 * </p>
 *
 * @see unsw.entity.card.CampFireCard
 * @since 1.0
 **/
public class CampFireCard extends Card {

    /**
     * <p>
     * Constructor {@code CampFireCard}
     * </p>
     * <p>
     * Initialises the new CampFireCard with the given parameters
     *
     * @param position //
     **/
    public CampFireCard(Pair<Integer, Integer> position) {
        super(position);
        super.setCardName("CampFireCard");

        setPath(false);
        setNearPath(true);
        setWild(true);

        super.setEntityView(super.loadImage("images/campfire_card.png"));
    }

    @Override
    public Building convertToBuilding(Pair<Integer, Integer> position) {
        return new CampFireBuilding(position);
    }
}
