package unsw.loopmania;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;
import unsw.GameRandom;
import unsw.entity.Character;
import unsw.entity.*;
import unsw.entity.building.*;
import unsw.entity.card.*;
import unsw.goal.Goal;
import unsw.mode.Mode;
import unsw.type.CardType;

import java.util.ArrayList;
import java.util.List;

/**
 * A backend world.
 * <p>
 * A world can contain many entities, each occupy a square. More than one entity
 * can occupy the same square.
 */
public class LoopManiaWorld {
    public static final int unequippedInventoryWidth = 4;
    public static final int unequippedInventoryHeight = 4;

    /**
     * width of the world in GridPane cells
     */
    private final int width;

    /**
     * height of the world in GridPane cells
     */
    private final int height;

    /**
     * generic entities - i.e. those which don't have dedicated fields
     */
    private final List<Entity> nonSpecifiedEntities;

    /**
     * list of x,y coordinate pairs in the order
     * by which moving entities traverse them
     */
    private final List<Pair<Integer, Integer>> orderedPath;

    private final ItemSystem itemSystem;
    private final BattleSystem battleSystem;
    private final BuildingSystem buildingSystem;
    private final List<Card> cardEntities;
    private final Position position;
    private final IntegerProperty cycle;
    private final IntegerProperty elanMuskeKilled;
    private final IntegerProperty doggieKilled;
    private HeroCastleBuilding heroCastle;
    private Character character;
    private Mode mode;
    private Goal goal;
    private boolean GameOver;
    private String info = "";

    /**
     * create the world (constructor)
     *
     * @param width       width of world in number of cells
     * @param height      height of world in number of cells
     * @param orderedPath ordered list of x, y coordinate pairs representing
     *                    position of path cells in world
     */
    public LoopManiaWorld(int width, int height, List<Pair<Integer, Integer>> orderedPath) {
        this.width = width;
        this.height = height;
        this.orderedPath = orderedPath;

        this.GameOver = false;

        this.cycle = new SimpleIntegerProperty(1);
        this.elanMuskeKilled = new SimpleIntegerProperty(0);
        this.doggieKilled = new SimpleIntegerProperty(0);
        this.character = null;
        this.heroCastle = null;
        this.mode = null;

        this.position = new Position(this);
        this.cardEntities = new ArrayList<>();
        this.nonSpecifiedEntities = new ArrayList<>();

        this.GameOver = false;

        this.buildingSystem = new BuildingSystem(this);
        this.itemSystem = new ItemSystem(this);
        this.battleSystem = new BattleSystem(this);
    }

    /**
     * add a generic entity (without it's own dedicated method for adding to the
     * world)
     *
     * @param entity //
     */
    public void addEntity(Entity entity) {
        // for adding non-specific entities (ones without another dedicated list)
        nonSpecifiedEntities.add(entity);
    }

    /**
     * Method that process rewards after killing enemy
     *
     * @return
     */
    public Item rewards() {
        int possibility = GameRandom.random.nextInt(100);
        Item item = null;
        if (possibility < 40) {
            item = itemSystem.addUnequippedItem();
        } else {
            character.getGold().set(character.getGoldValue() + 50);
        }
        return item;
    }

    /**
     * Method that increase doggie coin price after each run move
     */
    private void increaseDoggieCoinPrice() {
        List<BasicEnemy> enemies = getBattleSystem().getEnemies();
        boolean flag = false;
        for (BasicEnemy e : enemies) {
            if (e.getType() == "ElanMuske") {
                flag = true;
                break;
            }
        }
        int price;
        if (flag) {
            price = GameRandom.random.nextInt(500) + 1;
            character.addDoggieCoinPrice(price);
        } else if (getElanMuskeKilledValue() == 0) {
            price = GameRandom.random.nextInt(250) + 250;
            character.setDoggieCoinPrice(price);
        } else {
            price = GameRandom.random.nextInt(250) + 1;
            character.setDoggieCoinPrice(price);
        }
    }

    /**
     * run moves which occur with every tick without needing to spawn anything
     * immediately
     */
    public void runTickMoves() {
        this.character.moveDownPath();
        this.battleSystem.moveBasicEnemies();
        increaseDoggieCoinPrice();
        int startX = heroCastle.getX();
        int startY = heroCastle.getY();
        int charX = character.getX();
        int charY = character.getY();
        if (startX == charX && startY == charY) { cycle.set(cycle.get() + 1); }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Help

    /**
     * get a randomly generated position which could be used to spawn an entity
     *
     * @return null if random choice is that wont be spawning an entity or
     * it isn't possible, or random coordinate pair if should go ahead
     */
    public Pair<Integer, Integer> possiblySpawnPosition(int choice, int size, int maxSize) {
        if ((choice == 0) && (size < maxSize)) {
            List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
            int indexPosition = this.orderedPath.indexOf(new Pair<>(character.getX(), character.getY()));
            // inclusive start and exclusive end of range of positions not allowed
            int startNotAllowed = (indexPosition - 2 + this.orderedPath.size()) % this.orderedPath.size();
            int endNotAllowed = (indexPosition + 3) % this.orderedPath.size();
            // note terminating condition has to be != rather than < since wrap around...
            for (int i = endNotAllowed; i != startNotAllowed; i = (i + 1) % this.orderedPath.size()) {
                orderedPathSpawnCandidates.add(this.orderedPath.get(i));
            }
            // choose random choice
            return orderedPathSpawnCandidates.get(GameRandom.random.nextInt(orderedPathSpawnCandidates.size()));
        }
        return null;
    }

    /**
     * Method that proccess the spawn position for boss
     *
     * @return
     */
    public Pair<Integer, Integer> possilyPositionForBoss() {
        List<Pair<Integer, Integer>> orderedPathSpawnCandidates = new ArrayList<>();
        int indexPosition = this.orderedPath.indexOf(new Pair<>(character.getX(), character.getY()));
        // inclusive start and exclusive end of range of positions not allowed
        int startNotAllowed = (indexPosition - 2 + this.orderedPath.size()) % this.orderedPath.size();
        int endNotAllowed = (indexPosition + 3) % this.orderedPath.size();
        // note terminating condition has to be != rather than < since wrap around...
        for (int i = endNotAllowed; i != startNotAllowed; i = (i + 1) % this.orderedPath.size()) {
            orderedPathSpawnCandidates.add(this.orderedPath.get(i));
        }
        // choose random choice
        return orderedPathSpawnCandidates.get(GameRandom.random.nextInt(orderedPathSpawnCandidates.size()));
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Card

    /**
     * spawn a card in the world and return the card entity
     *
     * @return a card to be spawned in the controller as a JavaFX node
     */
    public Card loadCard() {
        // if adding more cards than have, remove the first card...
        if (cardEntities.size() >= getWidth()) {
            removeCard(0);
        }

        Card newCard = null;
        CardType cardType = CardType.getRandomCardType();

        Pair<Integer, Integer> cardPosition = Pair.with(cardEntities.size(), 0);

        switch (cardType) {
            case ZombiePitCard:
                newCard = new ZombiePitCard(cardPosition);
                break;
            case VampireCastleCard:
                newCard = new VampireCastleCard(cardPosition);
                break;
            case TowerCard:
                newCard = new TowerCard(cardPosition);
                break;
            case VillageCard:
                newCard = new VillageCard(cardPosition);
                break;
            case BarracksCard:
                newCard = new BarracksCard(cardPosition);
                break;
            case TrapCard:
                newCard = new TrapCard(cardPosition);
                break;
            case CampfireCard:
                newCard = new CampFireCard(cardPosition);
                break;
            case MagicShopCard:
                newCard = new MagicShopCard(cardPosition);
                break;
            default:
                System.err.println("no such card type!!!");
                break;
        }
        cardEntities.add(newCard);
        return newCard;
    }

    /**
     * remove card at a particular index of cards (position in gridpane of unplayed cards)
     *
     * @param index the index of the card, from 0 to length-1
     */
    private void removeCard(int index) {
        Card c = cardEntities.get(index);
        int x = c.getX();
        c.destroy();
        cardEntities.remove(index);
        character.setGold(character.getGold().get() + 30);
        shiftCardsDownFromXCoordinate(x);
    }

    /**
     * shift card coordinates down starting from x coordinate
     *
     * @param x x coordinate which can range from 0 to width-1
     */
    public void shiftCardsDownFromXCoordinate(int x) {
        for (Card c : cardEntities) {
            if (c.getX() >= x) { c.x().set(c.getX() - 1); }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Getter and Setter

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * set the character. This is necessary because it is loaded as a special entity
     * out of the file
     *
     * @param character the character
     */
    public void setCharacter(Character character) {
        this.character = character;
        this.itemSystem.setCharacter(character);
        this.getBattleSystem().setCharacter(character);
    }

    /**
     * set the heroCastle. This is necessary because it is loaded as a special
     * entity out of the file
     *
     * @param heroCastle the character
     */
    public void setHeroCastle(HeroCastleBuilding heroCastle) {
        this.heroCastle = heroCastle;
    }

    /**
     * Getter that get mode
     *
     * @return
     */
    public Mode getMode() {
        return this.mode;
    }

    /**
     * Setter that set mode
     *
     * @param mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * Setter that set goal
     *
     * @param goal
     */
    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    /**
     * Getter that get goal
     *
     * @return
     */
    public Goal getGoal() {
        return goal;
    }

    /**
     * Getter that get Item system
     *
     * @return
     */
    public ItemSystem getItemSystem() {
        return this.itemSystem;
    }

    /**
     * check whether game is over
     **/
    public boolean checkGameIsOver() {
        return this.GameOver;
    }

    /**
     * Setter that set the game over
     *
     * @param gameOver
     */
    public void setGameOver(boolean gameOver) {
        this.GameOver = gameOver;
    }

    /**
     * Getter that get info
     *
     * @return
     */
    public String getInfo() {
        return this.info;
    }

    /**
     * Setter that set info
     *
     * @param info
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * Getter that get character
     *
     * @return
     */
    public Character getCharacter() {
        return this.character;
    }

    /**
     * Getter that get battle system
     *
     * @return
     */
    public BattleSystem getBattleSystem() {
        return this.battleSystem;
    }

    /**
     * Getter that get card entity
     *
     * @return
     */
    public List<Card> getCardEntities() {
        return this.cardEntities;
    }

    /**
     * Getter that get building system
     *
     * @return
     */
    public BuildingSystem getBuildingSystem() {
        return buildingSystem;
    }

    /**
     * Getter that get cycle
     *
     * @return
     */
    public IntegerProperty getCycle() {
        return cycle;
    }

    /**
     * Getter that get amount of elan muske killed
     *
     * @return
     */
    public IntegerProperty getElanMuskeKilled() {
        return elanMuskeKilled;
    }

    /**
     * Getter that get amount of elan muske killed as int
     *
     * @return
     */
    public int getElanMuskeKilledValue() {
        return elanMuskeKilled.get();
    }

    /**
     * Method that add elan muske killed by one
     *
     * @return
     */
    public void addElanMuskeKilled() {
        this.elanMuskeKilled.set(getElanMuskeKilledValue() + 1);
    }

    /**
     * Getter that get amount of doggie killed
     *
     * @return
     */
    public IntegerProperty getDoggieKilled() {
        return doggieKilled;
    }

    /**
     * Getter that get amount of doggie killed as int
     *
     * @return
     */
    public int getDoggieKilledValue() {
        return doggieKilled.get();
    }

    /**
     * Method that add doogie killed by one
     *
     * @return
     */
    public void addDoggieKilled() {
        this.doggieKilled.set(getDoggieKilledValue() + 1);
    }

    /**
     * Setter that set the cycle
     *
     * @param num
     */
    public void setCycle(int num) {
        this.cycle.set(num);
    }

    /**
     * Getter that get position
     *
     * @return
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Getter that get hero castle
     *
     * @return
     */
    public HeroCastleBuilding getHeroCastle() {
        return this.heroCastle;
    }

    /**
     * Getter that get ordered path
     *
     * @return
     */
    public List<Pair<Integer, Integer>> getOrderedPath() {
        return this.orderedPath;
    }

    /**
     * Getter that get difficult constant
     *
     * @return
     */
    public double getDifficultyConstant() {
        return mode.getDifficultyConstant();
    }
}
