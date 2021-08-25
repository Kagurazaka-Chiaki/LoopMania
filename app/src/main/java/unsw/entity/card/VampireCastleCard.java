package unsw.entity.card;

import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.entity.Card;
import unsw.entity.building.VampireCastleBuilding;

/**
 * <p>
 * Class {@code VampireCastleCard}
 * </p>
 * <p>
 * represents a vampire castle card in the backend game world
 *
 * @see unsw.entity.card.VampireCastleCard
 * @since 1.0
 **/
public class VampireCastleCard extends Card {

    /**
     * <p>
     * Constructor {@code VampireCastleCard}
     * </p>
     * <p>
     * Initialises the new VampireCastleCard with the given parameters
     *
     * @param position //
     **/
    public VampireCastleCard(Pair<Integer, Integer> position) {
        super(position);
        super.setCardName("VampireCastleCard");

        setPath(false);
        setNearPath(true);
        setWild(false);

        super.setEntityView(super.loadImage("images/vampire_castle_card.png"));
    }

    @Override
    public Building convertToBuilding(Pair<Integer, Integer> position) {
        return new VampireCastleBuilding(position);
    }
}
