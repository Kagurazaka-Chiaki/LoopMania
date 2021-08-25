package unsw.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.MenuSwitcher;
import unsw.music.MusicList;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController {

    @FXML private AnchorPane anchorPane;
    @FXML private Button backButton;
    @FXML private Text gameOver;

    private LoopManiaWorld world;

    private MenuSwitcher mainMenuSwitcher;

    public LoopManiaWorld getWorld() {
        return this.world;
    }

    /**
     * this method is triggered when click button to go to main menu in FXML
     *
     * @throws IOException //
     */
    @FXML
    public void backToMainMenu(ActionEvent event) throws IOException {
        this.mainMenuSwitcher.switchMenu();
    }

    public void setWorld(LoopManiaWorld world) {
        this.world = world;
    }

    public void setMainMenuSwitcher(MenuSwitcher mainMenuSwitcher) {
        this.mainMenuSwitcher = mainMenuSwitcher;
    }

}
