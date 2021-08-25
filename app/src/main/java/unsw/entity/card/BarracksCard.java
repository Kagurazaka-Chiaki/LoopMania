package unsw.entity.card;

import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.entity.Card;
import unsw.entity.building.BarracksBuilding;

/**
 * <p>
 * Class {@code BarracksCard}
 * </p>
 *
 * <p>
 * Barracks can spawn allied soldier
 * </p>
 *
 * @see unsw.entity.card.BarracksCard
 * @since 1.0
 **/
public class BarracksCard extends Card {

    /**
     * <p>
     * Constructor {@code BarracksCard}
     * </p>
     * <p>
     * Initialises the new BarracksCard with the given parameters
     *
     * @param position //
     **/
    public BarracksCard(Pair<Integer, Integer> position) {
        super(position);
        super.setCardName("BarracksCard");

        setPath(true);
        setNearPath(false);
        setWild(false);

        super.setEntityView(super.loadImage("images/barracks_card.png"));
    }

    @Override
    public Building convertToBuilding(Pair<Integer, Integer> position) {
        return new BarracksBuilding(position);
    }
}
