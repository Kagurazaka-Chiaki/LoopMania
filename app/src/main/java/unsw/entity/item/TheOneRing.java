package unsw.entity.item;

import org.javatuples.Pair;

import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.Status;
import unsw.entity.ability.ItemAbility;
import unsw.entity.ability.RingAbility;

public class TheOneRing extends RareItem {

    /**
     * <p>
     * Constructor {@code TheOneRing}
     * </p >
     * <p>
     * Initialises the equipment of type TheOneRing which can respawn the character
     * by destroy itself
     *
     * @param position // the position in the item list
     **/
    public TheOneRing(Pair<Integer, Integer> position) {
        super(position);
        super.setStatus(new Status(0, 0, 0));
        super.setEntityView(super.loadImage("images/the_one_ring.png"));
        super.setAbility(new RingAbility());
        // TheOneRing can not be equipped, so set the validPos to null
        super.setValidPosX(null);
        super.setValidPosY(null);
    }
}
