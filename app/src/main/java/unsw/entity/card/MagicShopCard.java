package unsw.entity.card;

import org.javatuples.Pair;
import unsw.entity.Building;
import unsw.entity.Card;
import unsw.entity.building.MagicShopBuilding;

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
public class MagicShopCard extends Card {

    /**
     * <p>
     * Constructor {@code MagicShopCard}
     * </p>
     * <p>
     * Initialises the new MagicShopCard with the given parameters
     *
     * @param position //
     **/
    public MagicShopCard(Pair<Integer, Integer> position) {
        super(position);
        super.setCardName("MagicShopCard");

        setPath(true);
        setNearPath(false);
        setWild(false);

        super.setEntityView(super.loadImage("images/magicshop.png"));
    }

    @Override
    public Building convertToBuilding(Pair<Integer, Integer> position) {
        return new MagicShopBuilding(position);
    }
}
