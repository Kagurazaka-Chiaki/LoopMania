package unsw.entity.ability;

import unsw.entity.BasicEnemy;
import unsw.entity.Character;

public interface ItemAbility {
    /**
     * ability for item
     *
     * @param character //
     * @param enemy     //
     * @return //
     */
    boolean ability(Character character, BasicEnemy enemy);
}
