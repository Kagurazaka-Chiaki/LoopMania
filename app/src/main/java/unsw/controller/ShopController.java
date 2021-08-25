package unsw.controller;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.codefx.libfx.listener.handle.ListenerHandle;
import org.codefx.libfx.listener.handle.ListenerHandles;

import unsw.entity.Entity;
import unsw.entity.Item;
import unsw.entity.item.HealthPotion;
import unsw.entity.item.TheOneRing;
import unsw.loopmania.ItemSystem;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.MenuSwitcher;
import unsw.shop.Shop;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.List;

public class ShopController {

    @FXML private GridPane inventory;
    @FXML private GridPane shopItem;
    @FXML private GridPane goldAmount;
    @FXML private GridPane doggieCoin;
    @FXML private GridPane doggieStock;
    @FXML private Button exit;
    @FXML private Button sell;
    @FXML private Text message;
    @FXML private Text description;
    @FXML private ImageView owner;

    private final LoopManiaWorld world;
    private final LoopManiaWorldController controller;
    private final Shop shop;
    private final ItemSystem itemSystem;

    private MenuSwitcher mainMenuSwitcher;

    public ShopController(LoopManiaWorldController controller, LoopManiaWorld world) {
        this.world = world;
        this.controller = controller;
        this.shop = new Shop(world);
        this.itemSystem = world.getItemSystem();
    }

    @FXML
    public void initialize() {

        // initialize gold text and bind character's gold value
        ImageView goldView = new ImageView(this.loadImage("images/gold_pile.png"));
        goldAmount.add(goldView, 0, 0);
        Text goldText = new Text("Gold: ");
        goldText.setFont(Font.font(16));
        goldAmount.add(goldText, 1, 0);
        Text gold = new Text();
        gold.textProperty().bind(world.getCharacter().getGold().asString());
        gold.setFont(Font.font(16));
        goldAmount.add(gold, 2, 0);

        // initialize doggie text and bind the doggie's value
        ImageView doggiView = new ImageView(this.loadImage("images/doggiecoin.png"));
        doggieCoin.add(doggiView, 0, 0);
        Text doggieText = new Text("DoggieCoin: ");
        doggieText.setFont(Font.font(16));
        doggieCoin.add(doggieText, 1, 0);

        // bind the amount of doggie coin character have
        Text dogieAmount = new Text();
        dogieAmount.textProperty().bind(world.getCharacter().getDoggieCoinAmount().asString());
        dogieAmount.setFont(Font.font(16));
        doggieCoin.add(dogieAmount, 2, 0);

        // add a image of stock
        ImageView stockView = new ImageView(this.loadImage("images/stock.png"));
        doggieStock.add(stockView, 0, 0);
        Text stockText = new Text("Bid price: ");
        stockText.setFont(Font.font(16));
        doggieStock.add(stockText, 1, 0);

        // show the price of sell
        Text sellprice = new Text();
        sellprice.textProperty().bind(world.getCharacter().getDoggieCoinPrice().asString());
        sellprice.setFont(Font.font(16));
        doggieStock.add(sellprice, 2, 0);

        // set Welcome text
        message.setText("WelCome!!!");
        List<Item> characterInventory = itemSystem.getUnequippedInventory();
        // initialize the inventory item
        for (Item i : characterInventory) {
            onLoad(i, inventory);
        }
        // initialize the shop item
        for (Item i : shop.getList()) {
            onLoad(i, shopItem);
        }
    }

    /**
     * load goods image into goods gridpane
     */
    private void onLoad(Item item, GridPane gridpane) {
        ImageView view = new ImageView(item.getEntityView());
        view.setFitHeight(40);
        view.setFitWidth(40);
        addMouseClickHandlers(item, view, gridpane);
        addMouseHoverHandlers(item, view, gridpane);
        trackPosition(item, view);
        gridpane.getChildren().add(view);
    }

    private Image loadImage(String pathname) {
        // return new Image((new File("images/32x32GrassAndDirtPath.png")).toURI().toString());
        return new Image(pathname);
    }

    /**
     * set description for item
     */
    private void setDescriptionText(Item item) {
        if (item instanceof HealthPotion) {
            description.setText(" Restore 30% of maximum health");
        } else if (item instanceof TheOneRing) {
            description.setText(" Item that can revive charactor when he die");
        } else {
            String text = " Strength: " + String.format("%.1f", item.getStatus().getStrength()) + "\n";
            text += " Health: " + String.format("%.1f", item.getStatus().getHealth()) + "\n";
            text += " Defense: " + String.format("%.1f", item.getStatus().getDefense()) + "\n";
            description.setText(text);
        }
    }

    /**
     * eventhandler for mouse hover on items
     */
    private void addMouseHoverHandlers(Item item, ImageView view, GridPane sourceGridPane) {
        view.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                switch (sourceGridPane.getId()) {
                    case "shopItem":
                        message.setText(" Buy price: " + item.getBuyPrice());
                        break;
                    case "inventory":
                        message.setText(" Sell price: " + item.getSellPrice());
                        break;
                    default:
                        break;
                }
                setDescriptionText(item);
            } else {
                description.setText(" Hover on an item to see status");
                message.setText("What will you like?");
            }
        });
    }

    /**
     * click handler for clicking on items
     */
    private void addMouseClickHandlers(Item item, ImageView view, GridPane sourceGridPane) {
        view.setOnMouseClicked(event -> {
            switch (sourceGridPane.getId()) {
                case "shopItem":
                    Item newItem = shop.buy(item);
                    if (newItem == null) {
                        message.setText(shop.getMessage());
                        break;
                    }
                    onLoad(newItem, inventory);
                    controller.onLoad(newItem);
                    break;
                case "inventory":
                    shop.sell(item);
                    break;
                default:
                    break;
            }
        });
    }

    /**
     * add listener to track items
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
                               inventory.getChildren().remove(node);
                               shopItem.getChildren().remove(node);
                           }).buildAttached();
        ListenerHandle handleY =
            ListenerHandles.createFor(entity.y(), node).onAttach((o, l) -> o.addListener(yListener))
                           .onDetach((o, l) -> {
                               o.removeListener(yListener);
                               inventory.getChildren().remove(node);
                               shopItem.getChildren().remove(node);
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
     * Switch to main menu
     * @param mainMenuSwitcher
     */
    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher) {
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

    @FXML
    private void exitShop() throws IOException {
        shop.removeAllItem();
        controller.resume();
        mainMenuSwitcher.switchMenu();
        Stage stage1 = (Stage) inventory.getScene().getWindow();
        stage1.close();
    }

    @FXML
    private void sellDoggie(ActionEvent event) {
        shop.sellDoggie();
        message.setText(shop.getMessage());
        
    }

}
