package unsw.entity.ability;

import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.enemy.Doggie;
import unsw.entity.enemy.ElanMuske;

public class AndurilAbility implements ItemAbility {
    public boolean ability(Character character, BasicEnemy enemy) {
        if (ElanMuske.class.equals(enemy.getClass()) || Doggie.class.equals(enemy.getClass())) {
            double strength = character.getStatus().getStrength() - enemy.getStatus().getDefense();
            strength = character.getStatus().getStrength() + (2 * strength);
            character.getStatus().setStrength(strength);
            return true;
        }
        return false;
    }
}
