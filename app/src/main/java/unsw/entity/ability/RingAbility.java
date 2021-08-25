package unsw.entity.ability;

import unsw.entity.BasicEnemy;
import unsw.entity.Character;

public class RingAbility implements ItemAbility {
    public boolean ability(Character character, BasicEnemy enemy) {
        if (!character.getStatus().isAlive()) {
            character.getStatus().setCurrentHealth(character.getStatus().getHealth());
        }
        return false;
    }
}
