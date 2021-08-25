package unsw.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;
import unsw.entity.Character;
import unsw.entity.*;
import unsw.entity.building.BarracksBuilding;
import unsw.entity.enemy.AlliedSoldier;
import unsw.entity.item.Gold;
import unsw.entity.item.HealthPotion;
import unsw.entity.item.TheOneRing;
import unsw.loopmania.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * the draggable types. If you add more draggable types, add an enum value here.
 * This is so we can see what type is being dragged.
 */
enum DRAGGABLE_TYPE {
    CARD, ITEM
}

/**
 * A JavaFX controller for the world.
 * <p>
 * All event handlers and the timeline in JavaFX run on the JavaFX application
 * thread:
 * https://examples.javacodegeeks.com/desktop-java/javafx/javafx-concurrency-example/
 * Note in
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Application.html
 * under heading "Threading", it specifies animation timelines are run in the
 * application thread. This means that the starter code does not need locks
 * (mutexes) for resources shared between the timeline KeyFrame, and all of the
 * event handlers (including between different event handlers). This will make
 * the game easier for you to implement. However, if you add time-consuming
 * processes to this, the game may lag or become choppy.
 * <p>
 * If you need to implement time-consuming processes, we recommend: using Task
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Task.html by
 * itself or within a Service
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/concurrent/Service.html
 * <p>
 * Tasks ensure that any changes to public properties, change notifications for
 * errors or cancellation, event handlers, and states occur on the JavaFX
 * Application thread, so is a better alternative to using a basic Java Thread:
 * https://docs.oracle.com/javafx/2/threads/jfxpub-threads.htm The Service class
 * is used for executing/reusing tasks. You can run tasks without Service,
 * however, if you don't need to reuse it.
 * <p>
 * If you implement time-consuming processes in a Task or thread, you may need
 * to implement locks on resources shared with the application thread (i.e.
 * Timeline KeyFrame and drag Event handlers). You can check whether code is
 * running on the JavaFX application thread by running the helper method
 * printThreadingNotes in this class.
 * <p>
 * NOTE: http://tutorials.jenkov.com/javafx/concurrency.html and
 * https://www.developer.com/design/multithreading-in-javafx/#:~:text=JavaFX%20has%20a%20unique%20set,in%20the%20JavaFX%20Application%20Thread.
 * <p>
 * If you need to delay some code but it is not long-running, consider using
 * Platform.runLater
 * https://openjfx.io/javadoc/11/javafx.graphics/javafx/application/Platform.html#runLater(java.lang.Runnable)
 * This is run on the JavaFX application thread when it has enough time.
 */
public class LoopManiaWorldController {

    private final DoubleProperty gameSpeed = new SimpleDoubleProperty(0.3);
    // all image views including tiles, character, enemies, cards... even though cards in separate gridpane...
    private final List<ImageView> entityImages;
    private final LoopManiaWorld world;
    private final ItemSystem itemSystem;
    private final BuildingSystem buildingSystem;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dropped over its appropriate gridpane
     */
    private final EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged over the background
     */
    private final EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragOver;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dropped in the background
     */
    private final EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> anchorPaneRootSetOnDragDropped;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged into the boundaries of its appropriate
     * gridpane
     */
    private final EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragEntered;
    /**
     * mapping from draggable type enum CARD/TYPE to the event handler triggered
     * when the draggable type is dragged outside of the boundaries of its
     * appropriate gridpane
     */
    private final EnumMap<DRAGGABLE_TYPE, EventHandler<DragEvent>> gridPaneNodeSetOnDragExited;
    @FXML private StackPane userInterface;
    /**
     * anchorPaneRoot is the "background". It is useful since anchorPaneRoot
     * stretches over the entire game world, so we can detect dragging of
     * cards/items over this and accordingly update DragIcon coordinates
     */
    @FXML private AnchorPane anchorPaneRoot;
    @FXML private GridPane cycles;
    @FXML private GridPane sliderGrid;
    @FXML private Slider slider;
    @FXML private Slider musicVolume;
    /**
     * squares gridpane includes path images, enemies, character, empty grass,
     * buildings
     */
    @FXML private GridPane squares;
    /**
     * cards gridpane includes cards and the ground underneath the cards
     */
    @FXML private GridPane cards;
    /**
     * equippedItems gridpane is for equipped items (e.g. swords, shield, axe)
     */
    @FXML private GridPane equippedItems;
    @FXML private GridPane unequippedInventory;
    @FXML private GridPane goal;
    @FXML private GridPane characterHealth;
    @FXML private GridPane doggieCoin;
    @FXML private GridPane experience;
    @FXML private GridPane gold;
    @FXML private GridPane status;
    @FXML private GridPane soldiers;
    @FXML private TextArea textArea;
    @FXML private Text goalText;
    @FXML private Text soldierText;
    @FXML private TextArea logArea;
    /**
     * when we drag a card/item, the picture for whatever we're dragging is set here
     * and we actually drag this node
     */
    private DragIcon draggedEntity;

    // private BattleController battleController;
    // private Stage battleStage;
    private boolean isPaused;
    /**
     * runs the periodic game logic - second-by-second moving of character through
     * maze, as well as enemies, and running of battles
     */
    private Timeline timeline;
    private int currentRound;
    private int currentLoop;
    private ShopController shopController;
    private Parent gameOverScene;
    private Parent victoryScene;
    /**
     * object handling switching to the main menu
     */
    private MenuSwitcher mainMenuSwitcher;
    private MenuSwitcher shopSwitcher;
    /**
     * the image currently being dragged, if there is one, otherwise null. Holding
     * the ImageView being dragged allows us to spawn it again in the drop location
     * if appropriate.
     */
    private ImageView currentlyDraggedImage;
    /**
     * null if nothing being dragged, or the type of item being dragged
     */
    private DRAGGABLE_TYPE currentlyDraggedType;

    /**
     * @param world           world object loaded from file
     * @param initialEntities the initial JavaFX nodes (ImageViews) which should be
     *                        loaded into the GUI
     */
    public LoopManiaWorldController(LoopManiaWorld world, List<ImageView> initialEntities) {
        this.world = world;
        this.itemSystem = world.getItemSystem();
        this.buildingSystem = world.getBuildingSystem();
        this.currentLoop = 0;
        this.currentRound = 1;

        entityImages = new ArrayList<>(initialEntities);

        currentlyDraggedImage = null;
        currentlyDraggedType = null;

        // initialize them all...
        gridPaneSetOnDragDropped = new EnumMap<>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragOver = new EnumMap<>(DRAGGABLE_TYPE.class);
        anchorPaneRootSetOnDragDropped = new EnumMap<>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragEntered = new EnumMap<>(DRAGGABLE_TYPE.class);
        gridPaneNodeSetOnDragExited = new EnumMap<>(DRAGGABLE_TYPE.class);
    }

    public Parent getGameOverScene() {
        return this.gameOverScene;
    }

    public void setGameOverScene(Parent gameOverScene) {
        this.gameOverScene = gameOverScene;
    }

    public Parent getVictoryScene() {
        return this.victoryScene;
    }

    public void setVictoryScene(Parent victoryScene) {
        this.victoryScene = victoryScene;
    }

    private Image loadImage(String pathname) {
        return new Image(pathname);
    }

    @FXML
    public void initialize() {
        userInterface.setOnMouseClicked(event -> userInterface.requestFocus());

        Image pathTilesImage = this.loadImage("images/32x32GrassAndDirtPath.png");
        Image inventorySlotImage = this.loadImage("images/empty_slot.png");
        Image empty = this.loadImage("images/highlight.png");

        Rectangle2D imagePart = new Rectangle2D(0, 0, 32, 32);

        // Add the ground first so it is below all other entities (inculding all the
        // twists and turns)
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                ImageView groundView = new ImageView(pathTilesImage);
                groundView.setViewport(imagePart);
                squares.add(groundView, x, y);
            }
        }

        // load entities loaded from the file in the loader into the squares gridpane
        for (ImageView entity : entityImages) {
            squares.getChildren().add(entity);
        }

        // add the ground underneath the cards
        for (int x = 0; x < world.getWidth(); x++) {
            ImageView groundView = new ImageView(pathTilesImage);
            groundView.setViewport(imagePart);
            cards.add(groundView, x, 0);
        }

        // add the empty slot images for the unequipped inventory
        for (int x = 0; x < LoopManiaWorld.unequippedInventoryWidth; x++) {
            for (int y = 0; y < LoopManiaWorld.unequippedInventoryHeight; y++) {
                ImageView emptySlotView = new ImageView(inventorySlotImage);
                unequippedInventory.add(emptySlotView, x, y);
            }
        }

        // set slider for game speed
        slider.valueProperty().addListener((ov, old_val, new_val) -> gameSpeed.set(new_val.doubleValue() / 50));

        // set music volume slider
        musicVolume.valueProperty().addListener((observable, oldValue, newValue) -> {
            double result = 0.26 * newValue.floatValue() - 20; //  [0, 100] -> [-20, 6]
            // MusicList.backgroundMusic.getVolumeControl().setValue((float) result);
        });

        // set game speed text and bind it with slider
        Text game = new Text();
        game.setFont(Font.font(16));
        game.textProperty().bind(gameSpeed.asString("%.1f"));
        sliderGrid.add(game, 2, 0);

        // set gold text
        goalText.setText("Goal: " + world.getGoal().showGoalDetail());

        // show the current cycle in
        Text cycleText = new Text("Current Cycle: ");
        cycleText.setFont(Font.font(16));
        // bind cycle with backend
        Text cycleAmount = new Text();
        cycleAmount.setFont(Font.font(16));
        cycleAmount.textProperty().bind(world.getCycle().asString());
        cycles.add(cycleText, 0, 0);
        cycles.add(cycleAmount, 1, 0);

        // show the amount of doggie killed
        Text text = new Text("\tDoggie Killed: ");
        text.setFont(Font.font(16));
        cycles.add(text, 2, 0);
        // bind doggie killed with backend
        Text doggieText = new Text();
        doggieText.setFont(Font.font(16));
        doggieText.textProperty().bind(world.getDoggieKilled().asString());
        cycles.add(doggieText, 3, 0);

        // show the amount of elan muske killed
        text = new Text("\tElan Muske Killed: ");
        text.setFont(Font.font(16));
        cycles.add(text, 4, 0);
        // bind elan muske killed with backend
        Text elanText = new Text();
        elanText.setFont(Font.font(16));
        elanText.textProperty().bind(world.getElanMuskeKilled().asString());
        cycles.add(elanText, 5, 0);

        // loas slot image for equipped inventory
        ImageView swordSlot = new ImageView(this.loadImage("images/sword_unequipped.png"));
        ImageView helmetSlot = new ImageView(this.loadImage("images/helmet_slot.png"));
        ImageView armourSlot = new ImageView(this.loadImage("images/armour_unequipped.png"));
        ImageView shieldSlot = new ImageView(this.loadImage("images/shield_unequipped.png"));
        equippedItems.add(swordSlot, 0, 0);
        equippedItems.add(helmetSlot, 1, 0);
        equippedItems.add(armourSlot, 2, 0);
        equippedItems.add(shieldSlot, 3, 0);

        Character character = world.getCharacter();

        // set a health bar to show the character's current health
        ImageView heartView = new ImageView(this.loadImage("images/heart.png"));
        characterHealth.add(heartView, 0, 0);

        Rectangle healthBar = new Rectangle(100.0, 15.0, Color.RED);
        DoubleProperty width = new SimpleDoubleProperty(100.0);
        DoubleBinding healthPercentage =
            character.getStatus().getCurrentHealth().divide(character.getStatus().health());
        healthBar.widthProperty().bind(healthPercentage.multiply(width));
        characterHealth.add(healthBar, 1, 0);

        // show the character's current health as number
        Text currentHealth = new Text();
        currentHealth.setFont(Font.font(16));
        currentHealth.textProperty().bind(character.getStatus().getCurrentHealth().asString("%.1f"));
        characterHealth.add(currentHealth, 2, 0);

        // show the level that the character is now at and bind
        // with backend
        Text levelText = new Text("Level: ");
        levelText.setFont(Font.font(16));
        experience.add(levelText, 0, 0);

        Text levelAmount = new Text();
        levelAmount.setFont(Font.font(16));
        levelAmount.textProperty().bind(character.getLevel().asString());
        experience.add(levelAmount, 1, 0);

        // show the experience that the character need to update and bind
        // with backend
        Text expText = new Text("\tEXP: ");
        expText.setFont(Font.font(16));
        experience.add(expText, 2, 0);

        Text expAmount = new Text();
        expAmount.setFont(Font.font(16));
        NumberBinding levelBind = character.getLevel().subtract(new SimpleDoubleProperty(1));
        levelBind = levelBind.multiply(new SimpleDoubleProperty(200));
        levelBind = character.getEXP().subtract(levelBind);
        expAmount.textProperty().bind(levelBind.asString());
        experience.add(expAmount, 3, 0);

        Text totExp = new Text("/200");
        totExp.setFont(Font.font(16));
        experience.add(totExp, 4, 0);

        // show the gold of character have and bind with backend
        ImageView goldView = new ImageView(this.loadImage("images/gold_pile.png"));
        gold.add(goldView, 0, 0);

        Text goldAmount = new Text();
        goldAmount.setFont(Font.font(16));
        goldAmount.textProperty().bind(character.getGold().asString());
        gold.add(goldAmount, 1, 0);

        // show total experience of character and bind with backend
        expText = new Text("\tTotal EXP:");
        expText.setFont(Font.font(16));
        gold.add(expText, 2, 0);

        Text TotalExp = new Text();
        TotalExp.setFont(Font.font(16));
        TotalExp.textProperty().bind(character.getEXP().asString());
        gold.add(TotalExp, 3, 0);

        // show the doggie coin character owned and bind with backend
        ImageView doggiView = new ImageView(this.loadImage("images/doggiecoin.png"));
        doggieCoin.add(doggiView, 0, 0);

        Text doggieAmount = new Text();
        doggieAmount.setFont(Font.font(16));
        doggieAmount.textProperty().bind(character.getDoggieCoinAmount().asString());
        doggieCoin.add(doggieAmount, 1, 0);

        // show strength of character and bind with backend
        Text strengthText = new Text("Strength: ");
        strengthText.setFont(Font.font(16));
        status.add(strengthText, 0, 0);

        Text strengthAmount = new Text();
        strengthAmount.setFont(Font.font(16));
        strengthAmount.textProperty().bind(character.getStatus().strength().asString("%.1f"));
        status.add(strengthAmount, 1, 0);

        // show defense of character and bind with backend
        Text defenseText = new Text("Defense: ");
        defenseText.setFont(Font.font(16));
        status.add(defenseText, 0, 1);

        Text defenseAmount = new Text();
        defenseAmount.setFont(Font.font(16));
        defenseAmount.textProperty().bind(character.getStatus().defense().asString("%.1f"));
        status.add(defenseAmount, 1, 1);

        // load slot for soldiers
        for (int x = 0; x < 5; x++) {
            ImageView emptySlotView = new ImageView(empty);
            soldiers.add(emptySlotView, x, 0);
        }

        // create the draggable icon
        draggedEntity = new DragIcon();
        draggedEntity.setVisible(false);
        draggedEntity.setOpacity(0.7);
        anchorPaneRoot.getChildren().add(draggedEntity);
    }

    /**
     * Method that switch to shop stage
     */
    private void openShopView() {
        this.setShopSwitcher(() -> {
            try {
                // MusicList.backgroundMusic.stop();
                // MusicList.enterShopMusic.start(true);
                Stage stage = new Stage();
                stage.setOnCloseRequest(handle -> {
                    Platform.exit();
                    System.exit(0);
                });
                stage.setResizable(false);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/ShopView.fxml"));
                shopController = new ShopController(this, world);
                loader.setController(shopController);
                Parent shopRoot = loader.load();

                Stage stage1 = (Stage) anchorPaneRoot.getScene().getWindow();
                Scene scene1 = anchorPaneRoot.getScene();
                Parent root1 = stage1.getScene().getRoot();

                shopController.setMainMenuSwitcher(() -> {
                    // MusicList.enterShopMusic.stop();
                    // MusicList.backgroundMusic.start(true);
                    switchToRoot(scene1, root1, stage1);
                });

                Scene scene = new Scene(shopRoot);
                shopRoot.requestFocus();
                switchToRoot(scene, shopRoot, stage);
                stage1.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        try {
            pause();
            switchShopMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that switch to game over stage
     */
    private void openGameOverView() {
        this.timeline.stop();
        // MusicList.backgroundMusic.stop();
        // MusicList.gameOverMusic.start(false);
        try {
            userInterface.getChildren().setAll(gameOverScene, logArea);
            logArea.setManaged(false);
            logArea.setMaxHeight(300);
            logArea.setLayoutY(400);
            logArea.setLayoutX(150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method that switch to victory stage
     */
    private void openVictoryView() {
        this.timeline.stop();
        // MusicList.backgroundMusic.stop();
        // MusicList.victoryMusic.start(false);
        try {
            userInterface.getChildren().setAll(victoryScene, logArea);
            logArea.setManaged(false);
            logArea.setMaxHeight(300);
            logArea.setLayoutY(400);
            logArea.setLayoutX(150);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create and run the timer
     */
    public void startTimer() {

        System.out.println("starting timer");
        Character character = world.getCharacter();
        isPaused = false;

        // trigger adding code to process main game logic to queue. JavaFX will target
        // framerate of 0.3 seconds
        timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
            timeline.setRate(gameSpeed.get());
            world.runTickMoves();

            if (world.checkGameIsOver()) {
                openGameOverView();
                return;
            }

            if (world.getGoal().goalAchieve(world)) {
                openVictoryView();
                return;
            }

            // set buff for character (campfire)
            buildingSystem.setBuff();
            // check if character is in the village
            buildingSystem.checkCharacterPassVillage();

            if (buildingSystem.checkCharacterPassMagicShop()) {
                openShopView();
            }

            // check shop should open or not
            int shopX = world.getHeroCastle().getX();
            int shopY = world.getHeroCastle().getY();
            int charX = character.getX();
            int charY = character.getY();
            if (shopX == charX && shopY == charY) {
                currentLoop += 1;
                if (currentLoop == currentRound) {
                    currentLoop = 0;
                    currentRound += 1;
                    openShopView();
                }
                // spawn vampire, zombie
                List<BasicEnemy> newEnemiesNearBuilding = world.getBattleSystem().gengrateStrongEnemies();
                for (BasicEnemy newEnemy : newEnemiesNearBuilding) {
                    onLoad(newEnemy);
                }

                // spawn boss
                List<BasicEnemy> Boss = world.getBattleSystem().generateBoss();
                for (BasicEnemy newEnemy : Boss) {
                    onLoad(newEnemy);
                }
            }

            //when pass through a barracks, spawn a soldier, add to world.soldiers, add to character.alliedSoldiers
            for (BarracksBuilding b : buildingSystem.getBarracks()) {
                if (b.getX() == charX && b.getY() == charY) { loadSoldier(); }
            }

            // process enemy defeated by trap
            List<BasicEnemy> defeatedByTrap = world.getBattleSystem().trapDamage();
            for (BasicEnemy e : defeatedByTrap) {
                reactToEnemyDefeat(e);
            }

            // process enemy defeated by tower
            List<BasicEnemy> defeatedByTower = world.getBattleSystem().towerBattle();
            for (BasicEnemy e : defeatedByTower) {
                reactToEnemyDefeat(e);
            }

            // process enemy defeated by character
            List<BasicEnemy> defeatedEnemies = world.getBattleSystem().runBattles();
            for (BasicEnemy e : defeatedEnemies) {
                reactToEnemyDefeat(e);
            }

            // spawn slug, thief
            List<BasicEnemy> newEnemies = world.getBattleSystem().possiblySpawnEnemies();
            for (BasicEnemy newEnemy : newEnemies) {
                onLoad(newEnemy);
            }

            // pick potion on path
            List<HealthPotion> pickPotions = itemSystem.pickPotion();
            for (int i = 0; i < pickPotions.size(); i++) {
                loadPotion();
            }

            // spawn potion on path
            List<HealthPotion> newPotions = itemSystem.possiblySpawnPotion();
            for (HealthPotion newPotion : newPotions) {
                onLoad(newPotion);
            }

            // pick gold on path
            itemSystem.pickGold();
            // spawn potion on path
            List<Gold> newGolds = itemSystem.possiblySpawnGold();
            for (Gold gold : newGolds) {
                onLoad(gold);
            }

            printThreadingNotes("HANDLED TIMER");
            // append battle information to log
            this.logArea.appendText(world.getInfo());
            List<AlliedSoldier> soldierList = world.getCharacter().getAlliedSoldiers();
            this.textArea.appendText("##################################\n");
            if (soldierList.size() == 0) { this.textArea.appendText("---------Non Allied Soldier--------\n"); }
            for (AlliedSoldier s : soldierList) {
                this.textArea.appendText(s.getName() + " Current HP: " + s.getStatus().getCurrentHealthValue() + "\n");
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * pause the execution of the game loop the human player can still drag and drop
     * items during the game pause
     */
    private void pause() {
        isPaused = true;
        System.out.println("pausing");
        timeline.stop();
    }

    public void resume() {
        isPaused = false;
        System.out.println("resume");
        timeline.play();
    }

    public void terminate() {
        pause();
    }

    /**
     * pair the entity an view so that the view copies the movements of the entity.
     * add view to list of entity images
     *
     * @param entity backend entity to be paired with view
     * @param view   frontend imageview to be paired with backend entity
     */
    private void addEntity(Entity entity, ImageView view) {
        trackPosition(entity, view);
        entityImages.add(view);
    }

    /**
     * load a card from the world, and pair it with an image in the GUI
     */
    private void loadCard() {
        Card card = world.loadCard();
        onLoad(card);
    }

    /**
     * load a soldier from the world, and pair it with an image in the GUI
     */
    private void loadSoldier() {
        AlliedSoldier soldier = world.getBattleSystem().loadSoldier();
        onLoad(soldier);
    }

    /**
     * load a potion, and pair it with an image in the GUI
     */
    private void loadPotion() {
        Item item = itemSystem.addHealthPotion();
        onLoad(item);
    }

    /**
     * run GUI events after an enemy is defeated, such as spawning
     * items/experience/gold
     * <p>
     *
     * @param enemy // defeated enemy for which we should react to the death of
     */
    private void reactToEnemyDefeat(BasicEnemy enemy) {
        // react to character defeating an enemy
        // in starter code, spawning extra card/weapon...
        loadCard();
        Item item = world.rewards();
        if (item != null) { onLoad(item); }
    }

    /**
     * load a card into the GUI. Particularly, we must connect to the
     * drag detection event handler, and load the image into the cards GridPane.
     *
     * @param card //
     */
    private void onLoad(Card card) {
        ImageView view = new ImageView(card.getEntityView());

        // FROM
        // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane
        // note target setOnDragOver and setOnDragEntered defined in initialize method
        addDragEventHandlers(view, DRAGGABLE_TYPE.CARD, cards, squares);

        addEntity(card, view);

        cards.getChildren().add(view);
    }

    /**
     * load a allied soldier
     *
     * @param soldier //
     */
    private void onLoad(AlliedSoldier soldier) {
        ImageView view = new ImageView(soldier.getEntityView());

        addEntity(soldier, view);

        soldiers.getChildren().add(view);
    }

    /**
     * load a item into the GUI. Particularly, we must connect to the drag
     * detection event handler, and load the image into the unequippedInventory
     * GridPane.
     *
     * @param item //
     */
    public void onLoad(Item item) {
        ImageView view = new ImageView(item.getEntityView());
        addDragEventHandlers(view, DRAGGABLE_TYPE.ITEM, unequippedInventory, equippedItems);
        Tooltip.install(view, new Tooltip(getDescription(item)));
        addEntity(item, view);
        unequippedInventory.getChildren().add(view);
    }

    /**
     * get description for item as string
     */
    private String getDescription(Item item) {
        if (item instanceof HealthPotion) {
            return "Restore 30% of maximum health";
        } else if (item instanceof TheOneRing) {
            return "Item that can revive charactor when he die";
        } else {
            String text = "Strength: " + String.format("%.1f", item.getStatus().getStrength()) + "\n";
            text += "Health: " + String.format("%.1f", item.getStatus().getHealth()) + "\n";
            text += "Defense: " + String.format("%.1f", item.getStatus().getDefense()) + "\n";
            return text;
        }
    }

    /**
     * load an enemy into the GUI
     *
     * @param enemy //
     */
    private void onLoad(BasicEnemy enemy) {
        ImageView view = new ImageView(enemy.getEntityView());
        addEntity(enemy, view);
        squares.getChildren().add(view);
    }

    /**
     * load an potion into the GUI
     *
     * @param potion //
     */
    private void onLoad(HealthPotion potion) {
        ImageView view = new ImageView(potion.getEntityView());
        addEntity(potion, view);
        squares.getChildren().add(view);
    }

    /**
     * load an gold into the GUI
     *
     * @param gold //
     */
    private void onLoad(Gold gold) {
        ImageView view = new ImageView(gold.getEntityView());
        addEntity(gold, view);
        squares.getChildren().add(view);
    }

    /**
     * load a building into the GUI
     *
     * @param building //
     */
    private void onLoad(Building building) {
        ImageView view = new ImageView(building.getEntityView());
        addEntity(building, view);
        squares.getChildren().add(view);
    }

    /**
     * add drag event handlers for dropping into gridpanes, dragging over the
     * background, dropping over the background. These are not attached to invidual
     * items such as swords/cards.
     *
     * @param draggableType  the type being dragged - card or item
     * @param sourceGridPane the gridpane being dragged from
     * @param targetGridPane the gridpane the human player should be dragging to
     *                       (but we of course cannot guarantee they will do so)
     */
    private void buildNonEntityDragHandlers( //
        DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane) {
        // for example, in the specification, villages can only be dropped on path,
        // whilst vampire castles cannot go on the path
        gridPaneSetOnDragDropped.put(draggableType, event -> {
            // consider applying additional if-statement logic
            /*
             * you might want to design the application so dropping at an invalid location
             * drops at the most recent valid location hovered over, or simply allow the
             * card/item to return to its slot (the latter is easier, as you won't have to
             * store the last valid drop location!)
             */
            if (currentlyDraggedType == draggableType) {
                // problem = event is drop completed is false when should be true...
                // https://bugs.openjdk.java.net/browse/JDK-8117019
                // putting drop completed at start not making complete on VLAB...

                // Data dropped
                // If there is an image on the dragboard, read it and use it
                Dragboard db = event.getDragboard();
                Node node = event.getPickResult().getIntersectedNode();
                if (node != targetGridPane && db.hasImage()) {

                    Integer cIndex = GridPane.getColumnIndex(node);
                    Integer rIndex = GridPane.getRowIndex(node);
                    int x = cIndex == null ? 0 : cIndex;
                    int y = rIndex == null ? 0 : rIndex;
                    // Places at 0,0 - will need to take coordinates once that is implemented
                    ImageView image = new ImageView(db.getImage());

                    int nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                    int nodeY = GridPane.getRowIndex(currentlyDraggedImage);
                    switch (draggableType) {
                        case CARD:
                            removeDraggableDragEventHandlers(draggableType, targetGridPane);
                            Building newBuilding = convertCardToBuildingByCoordinates(nodeX, nodeY, x, y);
                            if (newBuilding == null) {
                                currentlyDraggedImage.setVisible(true);
                            } else {
                                onLoad(newBuilding);
                            }
                            break;
                        case ITEM:
                            removeDraggableDragEventHandlers(draggableType, targetGridPane);
                            Item item = itemSystem.getUnequippedInventoryItemByCoordinates(nodeX, nodeY);
                            if (itemSystem.checkEquipValidCell(nodeX, nodeY, x, y)) {
                                itemSystem.equipItemByCoordinates(nodeX, nodeY, x, y);
                                Tooltip.install(image, new Tooltip(getDescription(item)));
                                trackPosition(item, image);
                                targetGridPane.getChildren().add(image);
                            } else {
                                currentlyDraggedImage.setVisible(true);
                            }
                            break;
                        default:
                            break;
                    }
                    node.setOpacity(1);
                    draggedEntity.setVisible(false);
                    draggedEntity.setMouseTransparent(false);
                    // remove drag event handlers before setting currently dragged image to null
                    currentlyDraggedImage = null;
                    currentlyDraggedType = null;
                    printThreadingNotes("DRAG DROPPED ON GRIDPANE HANDLED");
                }
            }
            if (!isPaused) {
                timeline.play();
            }
            event.setDropCompleted(true);
            // consuming prevents the propagation of the event to the anchorPaneRoot
            // (as a sub-node of anchorPaneRoot, GridPane is prioritized)
            // https://openjfx.io/javadoc/11/javafx.base/javafx/event/Event.html#consume()
            // to understand this in full detail, ask your tutor or read
            // https://docs.oracle.com/javase/8/javafx/events-tutorial/processing.htm
            event.consume();
        });

        // this doesn't fire when we drag over GridPane because in the event handler for
        // dragging over GridPanes, we consume the event
        // https://github.com/joelgraff/java_fx_node_link_demo/blob/master/Draggable_Node/DraggableNodeDemo/src/application/RootLayout.java#L110
        anchorPaneRootSetOnDragOver.put(draggableType, event -> {
            if (currentlyDraggedType == draggableType) {
                if (event.getGestureSource() != anchorPaneRoot && event.getDragboard().hasImage()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }
            }
            if (currentlyDraggedType != null) {
                draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
            }
            event.consume();
        });

        // this doesn't fire when we drop over GridPane because in the event handler for
        // dropping over GridPanes, we consume the event
        anchorPaneRootSetOnDragDropped.put(draggableType, event -> {
            if (currentlyDraggedType == draggableType) {
                // Data dropped
                // If there is an image on the dragboard, read it and use it
                Dragboard db = event.getDragboard();
                Node node = event.getPickResult().getIntersectedNode();
                if (node != anchorPaneRoot && db.hasImage()) {
                    // Places at 0,0 - will need to take coordinates once that is implemented
                    currentlyDraggedImage.setVisible(true);
                    draggedEntity.setVisible(false);
                    draggedEntity.setMouseTransparent(false);
                    // remove drag event handlers before setting currently dragged image to null
                    removeDraggableDragEventHandlers(draggableType, targetGridPane);

                    currentlyDraggedImage = null;
                    currentlyDraggedType = null;
                }
            }
            // let the source know whether the image was successfully transferred and used
            if (!isPaused) {
                timeline.play();
            }
            event.setDropCompleted(true);
            event.consume();
        });
    }

    /**
     * remove the card from the world, and spawn and return a building instead where
     * the card was dropped
     *
     * @param cardNodeX     the x coordinate of the card which was dragged, from 0
     *                      to width-1
     * @param cardNodeY     the y coordinate of the card which was dragged (in
     *                      starter code this is 0 as only 1 row of cards)
     * @param buildingNodeX the x coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to width-1
     * @param buildingNodeY the y coordinate of the drop location for the card,
     *                      where the building will spawn, from 0 to height-1
     * @return building entity returned from the world
     */
    private Building convertCardToBuildingByCoordinates( //
        int cardNodeX, int cardNodeY, int buildingNodeX, int buildingNodeY) {
        return buildingSystem.convertCardToBuildingByCoordinates(cardNodeX, cardNodeY, buildingNodeX, buildingNodeY);
    }

    /**
     * add drag event handlers to an ImageView
     *
     * @param view           the view to attach drag event handlers to
     * @param draggableType  the type of item being dragged - card or item
     * @param sourceGridPane the relevant gridpane from which the entity would be
     *                       dragged
     * @param targetGridPane the relevant gridpane to which the entity would be
     *                       dragged to
     */
    private void addDragEventHandlers( //
        ImageView view, DRAGGABLE_TYPE draggableType, GridPane sourceGridPane, GridPane targetGridPane) {
        view.setOnDragDetected(event -> {
            currentlyDraggedImage = view; // set image currently being dragged, so squares setOnDragEntered can
            // detect it...
            currentlyDraggedType = draggableType;
            // Drag was detected, start drap-and-drop gesture
            // Allow any transfer node
            Dragboard db = view.startDragAndDrop(TransferMode.MOVE);

            // Put ImageView on dragboard
            ClipboardContent cbContent = new ClipboardContent();
            cbContent.putImage(view.getImage());
            db.setContent(cbContent);
            view.setVisible(false);

            buildNonEntityDragHandlers(draggableType, sourceGridPane, targetGridPane);

            draggedEntity.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
            draggedEntity.setImage(view.getImage());
            draggedEntity.setVisible(true);
            draggedEntity.setMouseTransparent(true);
            draggedEntity.toFront();

            // IMPORTANT!!!
            // to be able to remove event handlers, need to use addEventHandler
            // https://stackoverflow.com/a/67283792
            targetGridPane.addEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));
            anchorPaneRoot.addEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
            anchorPaneRoot.addEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

            for (Node n : targetGridPane.getChildren()) {
                // events for entering and exiting are attached to squares children because that
                // impacts opacity change
                // these do not affect visibility of original image...
                // https://stackoverflow.com/questions/41088095/javafx-drag-and-drop-to-gridpane

                // dropped in the location, the location shouldn't be highlighted!
                gridPaneNodeSetOnDragEntered.put(draggableType, EnterEvent -> {
                    if (currentlyDraggedType == draggableType) {
                        // The drag-and-drop gesture entered the target
                        // show the user that it is an actual gesture target
                        if (EnterEvent.getGestureSource() != n && EnterEvent.getDragboard().hasImage()) {
                            Integer x = GridPane.getColumnIndex(n);
                            Integer y = GridPane.getRowIndex(n);
                            Integer nodeX = GridPane.getColumnIndex(currentlyDraggedImage);
                            Integer nodeY = GridPane.getRowIndex(currentlyDraggedImage);
                            switch (draggableType) {
                                case ITEM:
                                    if (itemSystem.checkEquipValidCell(nodeX, nodeY, x, y)) { n.setOpacity(0.5); }
                                    break;
                                case CARD:
                                    if (buildingSystem.checkValidPos(nodeX, nodeY, x, y)) { n.setOpacity(0.5); }
                                    break;
                                default:
                                    n.setOpacity(1);
                                    break;
                            }
                        }
                    }
                    EnterEvent.consume();
                });

                // could program the game so if the new highlight location is invalid the
                // highlighting doesn't change, or leave this as-is
                gridPaneNodeSetOnDragExited.put(draggableType, ExitEvent -> {
                    if (currentlyDraggedType == draggableType) { n.setOpacity(1); }
                    ExitEvent.consume();
                });

                n.addEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
                n.addEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
            }
            if (!isPaused) {
                timeline.pause();
            }
            event.consume();
        });
    }

    /**
     * remove drag event handlers so that we don't process redundant events this is
     * particularly important for slower machines such as over VLAB.
     *
     * @param draggableType  either cards, or items in unequipped inventory
     * @param targetGridPane the gridpane to remove the drag event handlers from
     */
    private void removeDraggableDragEventHandlers(DRAGGABLE_TYPE draggableType, GridPane targetGridPane) {
        // remove event handlers from nodes in children squares, from anchorPaneRoot,
        // and squares
        targetGridPane.removeEventHandler(DragEvent.DRAG_DROPPED, gridPaneSetOnDragDropped.get(draggableType));

        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_OVER, anchorPaneRootSetOnDragOver.get(draggableType));
        anchorPaneRoot.removeEventHandler(DragEvent.DRAG_DROPPED, anchorPaneRootSetOnDragDropped.get(draggableType));

        for (Node n : targetGridPane.getChildren()) {
            n.removeEventHandler(DragEvent.DRAG_ENTERED, gridPaneNodeSetOnDragEntered.get(draggableType));
            n.removeEventHandler(DragEvent.DRAG_EXITED, gridPaneNodeSetOnDragExited.get(draggableType));
        }
    }

    /**
     * handle the pressing of keyboard keys. Specifically, we should pause when
     * pressing SPACE
     *
     * @param event some keyboard key press
     */
    @FXML
    public void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE:
                // MusicList.victoryMusic.stop();
                // MusicList.gameOverMusic.stop();
                if (isPaused) {
                    resume();
                } else {
                    pause();
                }
                break;
            case H:
                itemSystem.consumeFirstPotion();
                break;
            default:
                break;
        }
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher) {
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    public void setShopSwitcher(MenuSwitcher shopSwitcher) {
        this.shopSwitcher = shopSwitcher;
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     *
     * @throws IOException //
     */
    @FXML
    private void switchToMainMenu() throws IOException {
        pause();
        mainMenuSwitcher.switchMenu();
    }

    private void switchToRoot(Scene scene, Parent root, Stage stage) {
        scene.setRoot(root);
        root.requestFocus();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    private void switchShopMenu() throws IOException {
        pause();
        shopSwitcher.switchMenu();
    }

    /**
     * Set a node in a GridPane to have its position track the position of an entity
     * in the world.
     * <p>
     * By connecting the model with the view in this way, the model requires no
     * knowledge of the view and changes to the position of entities in the model
     * will automatically be reflected in the view.
     * <p>
     * note that this is put in the controller rather than the loader because we
     * need to track positions of spawned entities such as enemy or items which
     * might need to be removed should be tracked here
     * <p>
     * NOTE teardown functions setup here also remove nodes from their GridPane. So
     * it is vital this is handled in this Controller class
     *
     * @param entity //
     * @param node   //
     */
    private void trackPosition(Entity entity, Node node) {
        GridPane.setColumnIndex(node, entity.getX());
        GridPane.setRowIndex(node, entity.getY());

        ChangeListener<Number> xListener =
            (observable, oldValue, newValue) -> GridPane.setColumnIndex(node, newValue.intValue());
        ChangeListener<Number> yListener =
            (observable, oldValue, newValue) -> GridPane.setRowIndex(node, newValue.intValue());

        // if need to remove items from the equipped inventory, add code to remove from
        // equipped inventory gridpane in the .onDetach part
        ListenerHandle handleX =
            ListenerHandles.createFor(entity.x(), node).onAttach((o, l) -> o.addListener(xListener))
                           .onDetach((o, l) -> {
                               o.removeListener(xListener);
                               entityImages.remove(node);
                               soldiers.getChildren().remove(node);
                               squares.getChildren().remove(node);
                               cards.getChildren().remove(node);
                               equippedItems.getChildren().remove(node);
                               unequippedInventory.getChildren().remove(node);
                           }).buildAttached();
        ListenerHandle handleY =
            ListenerHandles.createFor(entity.y(), node).onAttach((o, l) -> o.addListener(yListener))
                           .onDetach((o, l) -> {
                               o.removeListener(yListener);
                               entityImages.remove(node);
                               soldiers.getChildren().remove(node);
                               squares.getChildren().remove(node);
                               cards.getChildren().remove(node);
                               equippedItems.getChildren().remove(node);
                               unequippedInventory.getChildren().remove(node);
                           }).buildAttached();
        handleX.attach();
        handleY.attach();

        // this means that if we change boolean property in an entity tracked from here,
        // position will stop being tracked
        // this wont work on character/path entities loaded from loader classes
        entity.shouldExist().addListener((obervable, oldValue, newValue) -> {
            handleX.detach();
            handleY.detach();
        });
    }

    /**
     * we added this method to help with debugging so you could check your code is
     * running on the application thread. By running everything on the application
     * thread, you will not need to worry about implementing locks, which is outside
     * the scope of the course. Always writing code running on the application
     * thread will make the project easier, as long as you are not running
     * time-consuming tasks. We recommend only running code on the application
     * thread, by using Timelines when you want to run multiple processes at once.
     * EventHandlers will run on the application thread.
     */
    private void printThreadingNotes(String currentMethodLabel) {
        String timeString = java.time.LocalDateTime.now().toString().replace('T', ' ');

        System.out.println("\n###########################################");
        System.out.println("current method = " + currentMethodLabel);
        System.out.println("In application thread? = " + Platform.isFxApplicationThread());
        System.out.println("Current system time = " + timeString);
    }
}
