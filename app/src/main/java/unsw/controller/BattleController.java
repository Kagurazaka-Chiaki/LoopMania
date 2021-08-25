package unsw.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import unsw.entity.BasicEnemy;
import unsw.entity.Character;

import java.util.List;

/*
 * To be continue
 */
public class BattleController {

    @FXML private AnchorPane battlePane;
    @FXML private VBox characterVBox;
    @FXML private ImageView characterView;
    @FXML private VBox enemyVBox_1;
    @FXML private ImageView enemyView_1;
    @FXML private VBox enemyVBox_2;
    @FXML private ImageView enemyView_2;
    @FXML private VBox enemyVBox_3;
    @FXML private ImageView enemyView_3;

    private final Character character;

    private final List<BasicEnemy> defeatedEnemies;

    public BattleController(Character character, List<BasicEnemy> defeatedEnemies) {
        this.character = character;
        this.defeatedEnemies = defeatedEnemies;
    }

    public Character getCharacter() {
        return character;
    }

    public List<BasicEnemy> getDefeatedEnemies() {
        return defeatedEnemies;
    }

    @FXML
    public void initialize() {

        Rectangle healthBar = new Rectangle(150.0, 10.0, Color.RED);

        // DoubleProperty width = new SimpleDoubleProperty(100.0);

        // try {
        //     character.currentHealth();
        // } catch (Exception error) {
        //     System.out.println(error);
        // }

        // DoubleBinding healthPercentage = character.currentHealth().divide(character.health());
        // healthBar.widthProperty().bind(healthPercentage.multiply(width));
        characterVBox.getChildren().add(healthBar);

    }

    @FXML
    void exitBattle(ActionEvent event) {

    }

}
