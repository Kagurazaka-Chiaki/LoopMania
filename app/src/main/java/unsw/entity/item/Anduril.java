package unsw.entity.item;

import org.javatuples.Pair;

import unsw.entity.Status;
import unsw.entity.ability.AndurilAbility;

public class Anduril extends RareItem {

    /**
     * <p>
     * Constructor {@code Anduril}
     * </p >
     * <p>
     * A very high damage sword which causes triple damage against bosses
     *
     * @param position // the position in the item list
     **/
    public Anduril(Pair<Integer, Integer> position, int n, double difficultConstant) {
        super(position);
        super.setStatus(new Status(10, 0, 0));
        super.increaseItemAttribute(n, difficultConstant);
        super.setEntityView(super.loadImage("images/anduril_flame_of_the_west.png"));
        super.setAbility(new AndurilAbility());
        super.setValidPosX(0);
        super.setValidPosY(0);
    }
}
