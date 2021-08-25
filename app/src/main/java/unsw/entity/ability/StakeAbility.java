package unsw.entity.ability;

import unsw.entity.BasicEnemy;
import unsw.entity.Character;
import unsw.entity.enemy.Vampire;

public class StakeAbility implements ItemAbility {
    public boolean ability(Character character, BasicEnemy enemy) {
        if (Vampire.class.equals(enemy.getClass())) {
            double strength = character.getStatus().getStrength() + enemy.getStatus().getDefense();
            character.getStatus().setStrength(strength);
            return true;
        }
        return false;
    }
}
