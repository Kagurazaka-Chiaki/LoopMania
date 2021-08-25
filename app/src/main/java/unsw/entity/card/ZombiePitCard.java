package unsw.entity.card;

import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.entity.Card;
import unsw.entity.building.ZombiePitBuilding;

/**
 * <p>
 * Class {@code ZombiePitCard}
 * </p>
 *
 * <p>
 * represents a zombie card in the backend game world
 * </p>
 *
 * @see unsw.entity.card.ZombiePitCard
 * @since 1.0
 **/
public class ZombiePitCard extends Card {

    /**
     * <p>
     * Constructor {@code ZombiePitCard}
     * </p>
     * <p>
     * Initialises the new ZombiePitCard with the given parameters
     *
     * @param position //
     **/
    public ZombiePitCard(Pair<Integer, Integer> position) {
        super(position);
        super.setCardName("ZombiePitCard");

        setPath(false);
        setNearPath(true);
        setWild(false);

        super.setEntityView(super.loadImage("images/zombie_pit_card.png"));
    }

    @Override
    public Building convertToBuilding(Pair<Integer, Integer> position) {
        return new ZombiePitBuilding(position);
    }
}
