package unsw.entity.card;

import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.entity.Card;
import unsw.entity.building.TowerBuilding;

/**
 * <p>
 * Class {@code TowerCard}
 * </p>
 *
 * <p>
 * represents a vampire castle card in the backend game world
 * </p>
 *
 * @see unsw.entity.card.TowerCard
 * @since 1.0
 **/
public class TowerCard extends Card {

    /**
     * <p>
     * Constructor {@code TowerCard}
     * </p>
     *
     * <p>
     * Initialises the new TowerCard with the given parameters
     * </p>
     *
     * @param position //
     **/
    public TowerCard(Pair<Integer, Integer> position) {
        super(position);
        super.setCardName("TowerCard");

        setPath(false);
        setNearPath(true);
        setWild(false);

        super.setEntityView(super.loadImage("images/tower_card.png"));
    }

    @Override
    public Building convertToBuilding(Pair<Integer, Integer> position) {
        return new TowerBuilding(position);
    }

}
