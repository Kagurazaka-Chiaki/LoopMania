package unsw.shop;

import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.ItemSystem;
import unsw.mode.Mode;
import unsw.entity.Character;
import unsw.entity.Item;
import unsw.entity.item.*;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * represents the shop in the backend of the game world
 */
public class Shop {
    private final LoopManiaWorld world;
    private final ItemSystem itemSystem;
    private final Character character;
    private final List<Item> shopItems;
    private int itemBrought;
    private String message;

    /**
     * Constructor for Shop
     *
     * @param world //
     */
    public Shop(LoopManiaWorld world) {
        this.world = world;
        this.character = world.getCharacter();
        this.shopItems = new ArrayList<>();
        this.itemBrought = 0;
        this.itemSystem = world.getItemSystem();
        addItem();
    }

    /**
     * add items into shop
     **/
    private void addItem() {
        int n = world.getCycle().get() + 1;
        double difficultyConstant = world.getDifficultyConstant();
        shopItems.add(new Sword(Pair.with(0, 0), n, difficultyConstant));
        shopItems.add(new Staff(Pair.with(1, 0), n, difficultyConstant));
        shopItems.add(new Stake(Pair.with(2, 0), n, difficultyConstant));
        shopItems.add(new Helmet(Pair.with(0, 1), n, difficultyConstant));
        shopItems.add(new Armour(Pair.with(1, 1), n, difficultyConstant));
        shopItems.add(new Shield(Pair.with(2, 1), n, difficultyConstant));
        shopItems.add(new HealthPotion(Pair.with(0, 2)));
    }

    /**
     * remove all items from shop
     **/
    public void removeAllItem() {
        for (Item i : shopItems) {
            i.destroy();
        }
        shopItems.clear();
    }

    /**
     * return the item bought by character
     *
     * @param item //
     **/
    public Item buy(Item item) {
        if (character.getGoldValue() < item.getBuyPrice()) {
            setMessage("Not enough Gold!");
            return null;
        }

        Item newItem = null;
        Mode mode = world.getMode();
        if (mode.restrictedAmount(item) && itemBrought < mode.amountOfItemCanBeBought()) {
            item.buyItem(character.getGold());
            newItem = itemSystem.addUnequippedItem(item);
            itemBrought++;
        } else if (!mode.restrictedAmount(item)) {
            item.buyItem(character.getGold());
            newItem = itemSystem.addUnequippedItem(item);
        }

        if (newItem == null) { setMessage("This item can only bought once!"); }
        return newItem;
    }

    /**
     * sell item
     *
     * @param item //
     **/
    public void sell(Item item) {
        itemSystem.removeUnequippedInventoryItemByCoordinates(item.getX(), item.getY());
    }

    /**
     * Method that Sell Doggie coin
     */
    public void sellDoggie() {
        Character character = world.getCharacter();
        int curDog = character.getDoggieCoinValue();
        if (curDog > 0) {
            int curPrice = character.getDoggiePriceValue();
            character.setGold(character.getGoldValue() + curPrice);
            character.getDoggieCoinAmount().set(character.getDoggieCoinValue() - 1);
            setMessage("Sell it at the peak!");
        } else {
            setMessage("Not enough Coin!");
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    /**
     * Setter for message
     *
     * @param message //
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Getter for Message
     *
     * @return //S
     */
    public String getMessage() {
        return message;
    }

    /**
     * return item list from shop
     **/
    public List<Item> getList() {
        return this.shopItems;
    }
}
