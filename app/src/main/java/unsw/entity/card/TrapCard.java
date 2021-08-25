package unsw.entity.card;

import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.entity.Card;
import unsw.entity.building.TrapBuilding;

/**
 * <p>
 * Class {@code TrapCard}
 * </p>
 *
 * <p>
 * Trap that can damage enemies and can be only placed on the path
 * </p>
 *
 * @see unsw.entity.card.TrapCard
 * @since 1.0
 **/
public class TrapCard extends Card {

    /**
     * <p>
     * Constructor {@code TrapCard}
     * </p>
     * <p>
     * Initialises the new TrapCard with the given parameters
     *
     * @param position //
     **/
    public TrapCard(Pair<Integer, Integer> position) {
        super(position);
        super.setCardName("TrapCard");

        setPath(true);
        setNearPath(false);
        setWild(false);

        super.setEntityView(super.loadImage("images/trap_card.png"));
    }

    @Override
    public Building convertToBuilding(Pair<Integer, Integer> position) {
        return new TrapBuilding(position);
    }
}
