package unsw.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import unsw.loopmania.MenuSwitcher;
import unsw.mode.BerserkerMode;
import unsw.mode.ConfusingMode;
import unsw.mode.Mode;
import unsw.mode.StandardMode;
import unsw.mode.SurvivalMode;
import unsw.worlds.*;

import java.io.IOException;

/**
 * controller for the main menu.
 */
public class MainMenuController {

    @FXML private VBox vBox;
    @FXML private ChoiceBox<String> choiceBox;
    @FXML private ChoiceBox<String> modeChoice;
    @FXML private Label title;

    private Mode mode;
    private Mode standardMode;
    private Mode survivalMode;
    private Mode berserkerMode;
    private Mode confusingMode;

    private Worlds world;
    private Worlds chapter1;
    private Worlds chapter2;
    private Worlds chapter3;
    private Worlds mini;
    private Worlds test;

    private final String[] mapList = new String[] {"Chapter 1", "Chapter 2", "Chapter 3", "mini_map", "test_map"};

    private final String[] modeList = new String[] {"Standard Mode", "Survival Mode", "Berserker Mode", "Confusing Mode"};

    public Worlds getWorld() {
        return this.world;
    }

    public Mode getMode() {
        return this.mode;
    }

    @FXML
    public void initialize() {
        standardMode = new StandardMode();
        survivalMode = new SurvivalMode();
        berserkerMode = new BerserkerMode();
        confusingMode = new ConfusingMode();

        chapter1 = new Chapter1();
        chapter2 = new Chapter2();
        chapter3 = new Chapter3();
        mini = new Mini_map();
        test = new Test_map();

        chooseWorld();
        chooseMode();
    }

    /**
     * choose which world(map) you want to play in
     */
    private void chooseWorld() {
        this.choiceBox.setItems(FXCollections.observableArrayList(mapList));
        this.choiceBox.setTooltip(new Tooltip("Select the world"));

        this.choiceBox.getSelectionModel().select(0);
        this.world = chapter1;

        this.choiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldV, newV) -> {
            String worldName = mapList[newV.intValue()];
            switch (worldName) {
                case "Chapter 1":
                    this.world = chapter1;
                    break;
                case "Chapter 2":
                    this.world = chapter2;
                    break;
                case "Chapter 3":
                    this.world = chapter3;
                    break;
                case "mini_map":
                    this.world = mini;
                    break;
                case "test_map":
                    this.world = test;
                    break;
            }
        });

    }

    /**
     * choose from standard mode, survival mode and berserker mode
     */
    private void chooseMode() {
        this.modeChoice.setItems(FXCollections.observableArrayList(modeList));
        this.modeChoice.setTooltip(new Tooltip("Select mode to start"));
        this.modeChoice.getSelectionModel().select(0);
        this.mode = standardMode;

        this.modeChoice.getSelectionModel().selectedIndexProperty().addListener((observable, oldV, newV) -> {
            String modeName = modeList[newV.intValue()];
            switch (modeName) {
                case "Standard Mode":
                    this.mode = standardMode;
                    break;
                case "Survival Mode":
                    this.mode = survivalMode;
                    break;
                case "Berserker Mode":
                    this.mode = berserkerMode;
                    break;
                case "Confusing Mode":
                    this.mode = confusingMode;
                    break;
            }
        });
    }

    /**
     * facilitates switching to main game
     */
    private MenuSwitcher gameSwitcher;

    public void setGameSwitcher(MenuSwitcher gameSwitcher) {
        this.gameSwitcher = gameSwitcher;
    }

    /**
     * facilitates switching to main game upon button click
     *
     * @throws IOException //
     */
    @FXML
    private void switchToGame() throws IOException {
        gameSwitcher.switchMenu();
    }
}
