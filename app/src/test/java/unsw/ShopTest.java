package unsw;

import javafx.embed.swing.JFXPanel;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unsw.entity.Character;
import unsw.entity.Item;
import unsw.entity.building.HeroCastleBuilding;
import unsw.entity.item.Armour;
import unsw.entity.item.HealthPotion;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.mode.BerserkerMode;
import unsw.mode.Mode;
import unsw.mode.StandardMode;
import unsw.mode.SurvivalMode;
import unsw.shop.Shop;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShopTest {

    private LoopManiaWorld world;
    private List<Pair<Integer, Integer>> orderedPath;

    @BeforeEach
    public void setupWorld() {
        JFXPanel jfxPanel = new JFXPanel();

        this.orderedPath = Arrays.asList( //
            Pair.with(3, 4), Pair.with(4, 4), Pair.with(4, 5) //
        );

        this.world = new LoopManiaWorld(7, 7, this.orderedPath);

        this.world.setHeroCastle(new HeroCastleBuilding(Pair.with(0, 0)));
        this.world.setMode(new StandardMode());
    }

    @Test
    public void buySwordTest() {
        // test that character can buy sword
        Mode stan = new StandardMode();
        GameRandom.random.setSeed(4);

        PathPosition pathPosition = new PathPosition(0, orderedPath);
        Character character = new Character(pathPosition, world);
        // set gold to the character so it has enough gold to buy item
        character.setGold(100);
        world.setCharacter(character);
        Shop shop = new Shop(this.world);

        List<Item> shopItems = shop.getList();
        Item bought = shop.buy(shopItems.get(0));

        // test that the standard mode does not restrict 
        // the amount of  item to be bought
        assertEquals(-1, stan.amountOfItemCanBeBought());
        // assert that sword is bought
        assertEquals(20.6, bought.getStatus()
                                 .getStrength());
        // assert that sword exist in inventory
        assertEquals(1, world.getItemSystem()
                             .getUnequippedInventory()
                             .size());
    }

    @Test
    public void sellItemTest() {
        // test that character can sell item in shop
        GameRandom.random.setSeed(4);

        PathPosition pathPosition = new PathPosition(0, orderedPath);
        Character character = new Character(pathPosition, world);

        world.setCharacter(character);
        Shop shop = new Shop(this.world);

        Item item = world.getItemSystem()
                         .addUnequippedItem();
        
        // assert that item exist in inventory
        assertEquals(1, world.getItemSystem()
                             .getUnequippedInventory()
                             .size());
        shop.sell(item);

        // assert that character get the correct amount of gold after selling item
        assertEquals(50, character.getGoldValue());
        // assert that item no longer exist in inventory
        assertEquals(0, world.getItemSystem()
                             .getUnequippedInventory()
                             .size());
    }

    @Test
    public void removeTest() {
        // test that remove works in shop, so that everytime
        // the shop close, all created object is remove
        // to free the memory out.
        GameRandom.random.setSeed(4);

        PathPosition pathPosition = new PathPosition(0, orderedPath);
        Character character = new Character(pathPosition, world);

        world.setCharacter(character);
        Shop shop = new Shop(this.world);

        shop.removeAllItem();

        assertEquals(0, shop.getList()
                            .size());
    }

    @Test
    public void survivalTest() {
        // test that potion can only be buy once in survival mode
        this.world.setMode(new SurvivalMode());
        GameRandom.random.setSeed(4);

        PathPosition pathPosition = new PathPosition(0, orderedPath);
        Character character = new Character(pathPosition, world);
        world.setCharacter(character);
        character.setGold(10000);
        Shop shop = new Shop(this.world);

        List<Item> shopItems = shop.getList();
        Item bought1 = shop.buy(shopItems.get(6));
        Item bought2 = shop.buy(shopItems.get(6));
        // assert that first healthPotion is bought successfully
        assertEquals(HealthPotion.class, bought1.getClass());
        // assert that second healthpotion can not be buy
        assertEquals(null, bought2);
        assertEquals("This item can only bought once!", shop.getMessage());

        // assert that other item can still be bought many times except potion
        for (int i = 0; i < 7; i++) {
            if (i != 6) {
                shop.buy(shopItems.get(i));
                shop.buy(shopItems.get(i));
            }
        }
        assertEquals(13, world.getItemSystem()
            .getUnequippedInventory()
            .size());

        // test that character can use potion and heals from the potion
        // brought from shop
        world.getItemSystem().consumeFirstPotion();
        assertEquals(12, world.getItemSystem()
            .getUnequippedInventory()
            .size());
    }

    @Test
    public void BerserkerTest() {
        // test that protective item can only be buy once in berserker mode
        this.world.setMode(new BerserkerMode());
        GameRandom.random.setSeed(4);

        PathPosition pathPosition = new PathPosition(0, orderedPath);
        Character character = new Character(pathPosition, world);
        world.setCharacter(character);
        character.setGold(10000);
        Shop shop = new Shop(this.world);

        List<Item> shopItems = shop.getList();
        Item bought1 = shop.buy(shopItems.get(4));
        Item bought2 = shop.buy(shopItems.get(4));
        // assert that first protective item (armour) is bought successfully
        assertEquals(Armour.class, bought1.getClass());
        // assert that second protective item (armour) can not be buy
        assertEquals(null, bought2);
        assertEquals("This item can only bought once!", shop.getMessage());

        // assert that protective item (Shield) can not be buy aswell
        Item bought3 = shop.buy(shopItems.get(5));
        assertEquals(null, bought3);
        assertEquals("This item can only bought once!", shop.getMessage());

        // assert that protective item (Helmet) can not be buy aswell
        Item bought4 = shop.buy(shopItems.get(3));
        assertEquals(null, bought4);
        assertEquals("This item can only bought once!", shop.getMessage());

        // assert that other item can still be bought 
        // many times except protective item
        for (int i = 0; i < 3; i++) {
            shop.buy(shopItems.get(i));
            shop.buy(shopItems.get(i));
        }
        shop.buy(shopItems.get(6));
        shop.buy(shopItems.get(6));
        assertEquals(9, world.getItemSystem()
                             .getUnequippedInventory()
                             .size());
    }

    @Test
    public void testNotEnoughGold() {
        // test character can not buy item if it has not enough gold
        this.world.setMode(new BerserkerMode());
        GameRandom.random.setSeed(4);

        PathPosition pathPosition = new PathPosition(0, orderedPath);
        Character character = new Character(pathPosition, world);
        world.setCharacter(character);
        Shop shop = new Shop(this.world);

        List<Item> shopItems = shop.getList();
        Item bought1 = shop.buy(shopItems.get(4));
        assertEquals(null, bought1);
        assertEquals("Not enough Gold!", shop.getMessage());
    }

    @Test
    public void testNotEnoughCoin() {
        // test character can not sell coin if it has not enough doggie coin
        this.world.setMode(new StandardMode());
        GameRandom.random.setSeed(4);

        PathPosition pathPosition = new PathPosition(0, orderedPath);
        Character character = new Character(pathPosition, world);
        world.setCharacter(character);
        Shop shop = new Shop(this.world);
        shop.sellDoggie();
        assertEquals("Not enough Coin!", shop.getMessage());
    }

    @Test
    public void testSellDoggie() {
        // test character can sell coin to earn gold
        this.world.setMode(new StandardMode());
        GameRandom.random.setSeed(4);

        PathPosition pathPosition = new PathPosition(0, orderedPath);
        Character character = new Character(pathPosition, world);
        world.setCharacter(character);
        character.addDoggieCoin();
        Shop shop = new Shop(this.world);
        shop.sellDoggie();
        assertEquals(character.getGoldValue(), 250);
    }

}
