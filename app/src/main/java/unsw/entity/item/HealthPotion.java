package unsw.entity.item;

import org.javatuples.Pair;
import unsw.entity.Character;
import unsw.entity.Item;

public class HealthPotion extends Item {

    /**
     * <p>
     * Constructor {@code HealthPotion}
     * </p >
     * <p>
     * Initialises the equipment of type HealthPotion so that character can
     * use the to heal health
     *
     * @param position // the position in the item list
     **/
    public HealthPotion(Pair<Integer, Integer> position) {
        super(position);
        super.setEntityView(super.loadImage("images/brilliant_blue_new.png"));
        // HealthPotion can not be equipped, so set the validPos to null
        super.setValidPosX(null);
        super.setValidPosY(null);
        super.setBuyPrice(100);
        super.setSellPrice(50);
    }

    /**
     * Method that use potion
     * @param character
     */
    public void usePotion(Character character) {
        double currentHealth = character.getStatus().getCurrentHealthValue();
        double health = character.getStatus().getHealth();
        currentHealth = currentHealth + (health * 0.3);
        if (currentHealth > character.getStatus().getHealth()) {
            currentHealth = health;
        }
        character.getStatus().setCurrentHealth(currentHealth);
    }
}
