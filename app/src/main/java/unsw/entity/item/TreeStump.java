package unsw.entity.item;

import org.javatuples.Pair;

import unsw.entity.Status;
import unsw.entity.ability.StumpAbility;

public class TreeStump extends RareItem {
    /**
     * <p>
     * Constructor {@code TreeStump}
     * </p >
     * <p>
     * An especially powerful shield, which provides higher defence against bosses
     *
     * @param position // the position in the item list
     **/
    public TreeStump(Pair<Integer, Integer> position, int n, double difficultConstant) {
        super(position);
        super.setStatus(new Status(0, 0, 10));
        super.increaseItemAttribute(n, difficultConstant);
        super.setEntityView(super.loadImage("images/tree_stump.png"));
        super.setAbility(new StumpAbility());
        super.setValidPosX(3);
        super.setValidPosY(0);
    }
}
