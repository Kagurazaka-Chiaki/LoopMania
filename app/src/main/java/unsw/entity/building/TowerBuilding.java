package unsw.entity.building;

import javafx.scene.image.Image;
import org.javatuples.Pair;
import unsw.entity.BasicEnemy;
import unsw.entity.Building;
import unsw.type.BuildingType;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Class {@code }
 * </p>
 * <p>
 * During a battle within its shooting radius, enemies will be attacked by the tower
 *
 * @see unsw.entity.building.TowerBuilding
 * @since 1.0
 **/
public class TowerBuilding extends Building {

    private final int attackRange;

    /**
     * <p>
     * Constructor {@code TowerBuilding}
     * </p>
     * <p>
     * Initialises the new TowerBuilding with the given parameters
     *
     * @param position //
     **/
    public TowerBuilding(Pair<Integer, Integer> position) {
        super(position);

        super.setBuildingType(BuildingType.TowerBuilding);

        Image towerBuildingView = super.loadImage("images/tower.png");
        super.setEntityView(towerBuildingView);
        attackRange = 2;
    }

    /**
     * attack enemy
     **/
    public void attackEnemy(BasicEnemy enemy) {
        double hp = enemy.getStatus().getHealth();
        double currentHp = enemy.getStatus().getCurrentHealthValue();
        double afterDamage = currentHp - hp * 0.03;
        enemy.getStatus().setCurrentHealth(afterDamage);
    }

    /**
     * return attack position
     **/
    public List<Pair<Integer, Integer>> attackPos(List<Pair<Integer, Integer>> path) {
        List<Pair<Integer, Integer>> posInRange = new ArrayList<>();
        List<Pair<Integer, Integer>> attackPos = new ArrayList<>();
        int posX = getX();
        int posY = getY();
        for (int i = 1; i <= attackRange; i++) {

            Pair<Integer, Integer> N = Pair.with(posX, posY - i);
            Pair<Integer, Integer> E = Pair.with(posX + i, posY);
            Pair<Integer, Integer> S = Pair.with(posX, posY + i);
            Pair<Integer, Integer> W = Pair.with(posX - i, posY);
            posInRange.add(N);
            posInRange.add(E);
            posInRange.add(S);
            posInRange.add(W);
            if (i == 1) {
                Pair<Integer, Integer> NW = Pair.with(posX - i, posY - i);
                Pair<Integer, Integer> NE = Pair.with(posX + i, posY - i);
                Pair<Integer, Integer> SE = Pair.with(posX + i, posY + i);
                Pair<Integer, Integer> SW = Pair.with(posX - i, posY + i);
                posInRange.add(NW);
                posInRange.add(NE);
                posInRange.add(SE);
                posInRange.add(SW);
            }
        }

        for (Pair<Integer, Integer> pair : posInRange) {
            if (path.contains(pair)) { attackPos.add(pair); }
        }
        return attackPos;
    }

}
