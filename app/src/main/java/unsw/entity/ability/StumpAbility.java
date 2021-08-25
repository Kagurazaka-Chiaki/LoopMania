package unsw.entity.ability;

import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.enemy.Doggie;
import unsw.entity.enemy.ElanMuske;

public class StumpAbility implements ItemAbility {
    public boolean ability(Character character, BasicEnemy enemy) {
        if (ElanMuske.class.equals(enemy.getClass()) || Doggie.class.equals(enemy.getClass())) {
            double extraDefense = character.getShield().getStatus().getDefense() * 0.4;
            double curDefense = character.getStatus().getDefense();
            character.getStatus().setDefense(curDefense + extraDefense);
            return true;
        }
        return false;
    }

}
