package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

import unsw.GameRandom;
import unsw.entity.Character;
import unsw.entity.item.*;
import unsw.entity.Item;
import unsw.type.ItemType;

public class ItemSystem {

    private final LoopManiaWorld world;
    private Character character;

    private final List<Item> unequippedInventoryItems;
    private final List<Item> equippedItems;
    private final List<HealthPotion> potionOnPath;
    private final List<Gold> goldOnPath;

    /**
     * Constructor for item system
     * @param world
     */
    public ItemSystem(LoopManiaWorld world) {
        this.world = world;
        this.unequippedInventoryItems = new ArrayList<>();
        this.equippedItems = new ArrayList<>();
        this.potionOnPath = new ArrayList<>();
        this.goldOnPath = new ArrayList<>();
    }

    /**
     * Method that add unequipped item to accorrding to the possibility
     * @return
     */
    public Item addUnequippedItem() {
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();

        Item item;
        int cycle = world.getCycle().get();
        double possibility = GameRandom.random.nextDouble();
        if (possibility < 0.15) {
            item = world.getMode().addRareItem(firstAvailableSlot, cycle);
        } else {
            item = addItem(firstAvailableSlot, cycle);
        }
        // now we insert the new item, as we know we have at least made a slot
        // available...
        unequippedInventoryItems.add(item);
        return item;
    }

    /**
     * Method that add item thats same as the one buy from shop
     * @param item
     * @return
     */
    public Item addUnequippedItem(Item item) {
        Pair<Integer, Integer> availableSlot = getFirstAvailableSlotForItem();
        Item newItem;
        int n = world.getCycle().get() + 1;
        if (HealthPotion.class.equals(item.getClass())) {
            newItem = new HealthPotion(availableSlot);
        } else if (Staff.class.equals(item.getClass())) {
            newItem = new Staff(availableSlot, n, world.getDifficultyConstant());
        } else if (Stake.class.equals(item.getClass())) {
            newItem = new Stake(availableSlot, n, world.getDifficultyConstant());
        } else if (Sword.class.equals(item.getClass())) {
            newItem = new Sword(availableSlot, n, world.getDifficultyConstant());
        } else if (Helmet.class.equals(item.getClass())) {
            newItem = new Helmet(availableSlot, n, world.getDifficultyConstant());
        } else if (Shield.class.equals(item.getClass())) {
            newItem = new Shield(availableSlot, n, world.getDifficultyConstant());
        } else {
            newItem = new Armour(availableSlot, n, world.getDifficultyConstant());
        }
        unequippedInventoryItems.add(newItem);
        return newItem;
    }

    /**
     * Method that add normal item
     * @param availableSlot
     * @param cycle
     * @return
     */
    private Item addItem(Pair<Integer, Integer> availableSlot, int cycle) {
        Item item = null;
        ItemType itemType = ItemType.getRandomItemType();

        switch (itemType) {
            case Armour:
                item = new Armour(availableSlot, cycle, world.getDifficultyConstant());
                break;
            case Helmet:
                item = new Helmet(availableSlot, cycle, world.getDifficultyConstant());
                break;
            case Shield:
                item = new Shield(availableSlot, cycle, world.getDifficultyConstant());
                break;
            case Sword:
                item = new Sword(availableSlot, cycle, world.getDifficultyConstant());
                break;
            case Stake:
                item = new Stake(availableSlot, cycle, world.getDifficultyConstant());
                break;
            case Staff:
                item = new Staff(availableSlot, cycle, world.getDifficultyConstant());
                break;
            default:
                break;
        }
        return item;
    }

    /**
     * remove an item by x,y coordinates
     *
     * @param x x coordinate from 0 to width-1
     * @param y y coordinate from 0 to height-1
     */
    public void removeUnequippedInventoryItemByCoordinates(int x, int y) {
        Item item = getUnequippedInventoryItemByCoordinates(x, y);
        assert item != null;
        item.sellItem(character.getGold());
        item.destroy();
        unequippedInventoryItems.remove(item);
    }

    /**
     * return an unequipped inventory item by x and y coordinates assumes that no 2
     * unequipped inventory items share x and y coordinates
     *
     * @param x x index from 0 to width-1
     * @param y y index from 0 to height-1
     * @return unequipped inventory item at the input position
     */
    public Item getUnequippedInventoryItemByCoordinates(int x, int y) {
        for (Item i : unequippedInventoryItems) {
            if ((i.getX() == x) && (i.getY() == y)) { return i; }
        }
        return null;
    }

    /**
     * Method that equip item according to coordinates
     * @param sourceX
     * @param sourceY
     * @param targetX
     * @param targetY
     */
    public void equipItemByCoordinates(int sourceX, int sourceY, int targetX, int targetY) {
        Item oldItem = getEquippedItemByCoordinates(targetX, targetY);
        Item newItem = getUnequippedInventoryItemByCoordinates(sourceX, sourceY);
        assert newItem != null;
        newItem.x().set(targetX);
        newItem.y().set(targetY);
        if (oldItem == null) {
            equipItem(newItem);
        } else {
            changeEquippedItem(newItem, oldItem);
        }

        if (targetX == 0) { character.setWeapon(newItem); }
        if (targetX == 3) { character.setShield(newItem); }
    }

    /**
     * Method that equip item
     * @param item
     */
    private void equipItem(Item item) {
        equippedItems.add(item);
        unequippedInventoryItems.remove(item);
        character.attributeGainByItem(item);
    }

    /**
     * Method that change the equip item
     * @param newItem
     * @param oldItem
     */
    private void changeEquippedItem(Item newItem, Item oldItem) {
        character.attributeLossByItem(oldItem);
        equippedItems.remove(oldItem);
        oldItem.destroy();
        equippedItems.add(newItem);
        unequippedInventoryItems.remove(newItem);
        character.attributeGainByItem(newItem);
    }

    /**
     * Method that get equipped item by coordinates
     * @param x
     * @param y
     * @return
     */
    private Item getEquippedItemByCoordinates(int x, int y) {
        for (Item i : equippedItems) {
            if ((i.getX() == x) && (i.getY() == y)) { return i; }
        }
        return null;
    }

    /**
     * Method that check the item can or can not equip to the given coordinates
     * @param sourceX
     * @param sourceY
     * @param targetX
     * @param targetY
     * @return
     */
    public boolean checkEquipValidCell(Integer sourceX, Integer sourceY, Integer targetX, Integer targetY) {
        Item item = getUnequippedInventoryItemByCoordinates(sourceX, sourceY);
        assert item != null;
        // if (item.getValidPosY() == -1) {
        //     return true;
        // }
        return targetX.equals(item.getValidPosX()) && targetY.equals(item.getValidPosY());
    }

    /**
     * Method that allow character to pick potion on path
     * @return
     */
    public List<HealthPotion> pickPotion() {
        List<HealthPotion> pickPotions = new ArrayList<>();
        for (HealthPotion p : potionOnPath) {
            if (character.getX() == p.getX() && character.getY() == p.getY()) { pickPotions.add(p); }
        }
        for (HealthPotion p : pickPotions) {
            p.destroy();
            potionOnPath.remove(p);
        }
        return pickPotions;
    }

    /**
     * Method that spawn potion on path
     * @return
     */
    public List<HealthPotion> possiblySpawnPotion() {
        int choice = GameRandom.random.nextInt(50);
        Pair<Integer, Integer> pos = world.possiblySpawnPosition(choice, this.potionOnPath.size(), 4);
        List<HealthPotion> spawningPotion = new ArrayList<>();
        if (pos != null) {
            HealthPotion potion = new HealthPotion(pos);
            potionOnPath.add(potion);
            spawningPotion.add(potion);
        }
        return spawningPotion;
    }

    /**
     * Method that add health potion
     * @return
     */
    public Item addHealthPotion() {
        Pair<Integer, Integer> firstAvailableSlot = getFirstAvailableSlotForItem();
        Item item = new HealthPotion(firstAvailableSlot);
        unequippedInventoryItems.add(item);
        return item;
    }

    /**
     * Method that consume the first potion
     */
    public void consumeFirstPotion() {
        for (Item i : unequippedInventoryItems) {
            if (HealthPotion.class.equals(i.getClass())) {
                consumePotion(i);
                return;
            }
        }
    }

    /**
     * Method that reheals character when consume potion
     * @param item
     */
    private void consumePotion(Item item) {
        HealthPotion potion = (HealthPotion) item;
        potion.usePotion(character);
        item.destroy();
        unequippedInventoryItems.remove(item);
    }

    /**
     * Method that spawn gold
     * @return
     */
    public List<Gold> possiblySpawnGold() {
        int choice = GameRandom.random.nextInt(100);
        Pair<Integer, Integer> pos = world.possiblySpawnPosition(choice, this.goldOnPath.size(), 3);
        List<Gold> spawningGold = new ArrayList<>();
        if (pos != null) {
            Gold gold = new Gold(pos, 10);
            goldOnPath.add(gold);
            spawningGold.add(gold);
        }
        return spawningGold;
    }

    /**
     * Method that allow character to pick gold on path
     * @return
     */
    public void pickGold() {
        List<Gold> pickGolds = new ArrayList<>();
        for (Gold g : goldOnPath) {
            if (character.getX() == g.getX() && character.getY() == g.getY()) {
                character.getGold().set(character.getGoldValue() + g.getGold().get());
                pickGolds.add(g);
                g.destroy();
            }
        }
        for (Gold g : pickGolds) {
            goldOnPath.remove(g);
        }
    }

    /**
     * get the first pair of x,y coordinates which don't have any items in it in the
     * unequipped inventory
     *
     * @return x, y coordinate pair
     */
    private Pair<Integer, Integer> getFirstAvailableSlotForItem() {
        // first available slot for an item...
        // IMPORTANT - have to check by y then x, since trying to find first available
        // slot defined by looking row by row
        for (int y = 0; y < LoopManiaWorld.unequippedInventoryHeight; y++) {
            for (int x = 0; x < LoopManiaWorld.unequippedInventoryWidth; x++) {
                if (getUnequippedInventoryItemByCoordinates(x, y) == null) { return new Pair<>(x, y); }
            }
        }

        // eject the oldest unequipped item and replace it... oldest item is that at
        // beginning of items
        // give some cash/experience rewards for the discarding of the oldest sword
        removeItemByPositionInUnequippedInventoryItems(0);
        return getFirstAvailableSlotForItem();
    }

    /**
     * remove item at a particular index in the unequipped inventory items list
     * (this is ordered based on age in the starter code)
     *
     * @param index index from 0 to length-1
     */
    private void removeItemByPositionInUnequippedInventoryItems(int index) {
        Item item = unequippedInventoryItems.get(index);
        item.sellItem(character.getGold());
        item.destroy();
        unequippedInventoryItems.remove(index);
    }

    /**
     * Method that let thief to steal item
     */
    public void stealItemByPositionInUnequippedInventoryItems() {
        if (unequippedInventoryItems.size() > 0) {
            int index = GameRandom.random.nextInt(unequippedInventoryItems.size());
            Item item = unequippedInventoryItems.get(index);
            item.destroy();
            unequippedInventoryItems.remove(index);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter
    
    /**
     * set the character. This is necessary because it is loaded as a special entity
     * out of the file
     *
     * @param character the character
     */
    public void setCharacter(Character character) {
        this.character = character;
    }

    /**
     * Getter that get the list of unequippedInventoryItems
     * @return
     */
    public List<Item> getUnequippedInventory() {
        return unequippedInventoryItems;
    }

    /**
     * Getter that get the list of equipped Items
     * @return
     */
    public List<Item> getEquippedItem() {
        return equippedItems;
    }

    /**
     * Getter that get the list of potion on path
     * @return
     */
    public List<HealthPotion> getPotionOnPath() {
        return potionOnPath;
    }

    /**
     * Getter that get the list of gold on path
     * @return
     */
    public List<Gold> getGoldOnPath() {
        return goldOnPath;
    }
}
