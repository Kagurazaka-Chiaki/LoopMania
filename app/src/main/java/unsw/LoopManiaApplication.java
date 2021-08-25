package unsw;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import unsw.controller.GameOverController;
import unsw.controller.LoopManiaWorldController;
import unsw.controller.MainMenuController;
import unsw.controller.VictoryController;
import unsw.loopmania.LoopManiaWorldControllerLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

// import jfxtras.styles.jmetro.JMetro;
// import jfxtras.styles.jmetro.Style;

/**
 * the main application run main method from this class
 */
public class LoopManiaApplication extends Application {
    /**
     * the controller for the game. Stored as a field so can terminate it when click
     * exit button
     */
    private LoopManiaWorldController mainController;

    private GameOverController gameOverController;
    private Parent gameOverRoot;

    private VictoryController victoryController;
    private Parent victoryRoot;

    private static Image getIcon() {
        return new Image(Objects.requireNonNull(LoopManiaApplication.class.getResourceAsStream("/images/racoon.png")));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        // set title on top of window bar
        primaryStage.setTitle("Loop Mania");

        // primaryStage.getIcons().add(getIcon());

        // prevent human player resizing game window (since otherwise would see white space)
        // alternatively, you could allow rescaling of the game
        // (you'd have to program resizing of the JavaFX nodes)
        primaryStage.setResizable(false);

        // MusicList.mainMenuMusic.start(true);

        // load the main menu
        MainMenuController mainMenuController = new MainMenuController();
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/fxmls/MainMenuView.fxml"));
        menuLoader.setController(mainMenuController);
        Parent mainMenuRoot = menuLoader.load();

        FXMLLoader gameOverLoader = new FXMLLoader(getClass().getResource("/fxmls/GameOverView.fxml"));
        this.gameOverRoot = gameOverLoader.load();
        this.gameOverController = gameOverLoader.getController();

        FXMLLoader victoryLoader = new FXMLLoader(getClass().getResource("/fxmls/VictoryView.fxml"));
        this.victoryRoot = victoryLoader.load();
        this.victoryController = victoryLoader.getController();

        // create new scene with the main menu (so we start with the main menu)
        Scene scene = createScene(mainMenuRoot);

        // set functions which are activated when button click to switch menu is pressed
        // e.g. from main menu to start the game, or from the game to return to main menu
        // mainController.setMainMenuSwitcher(() -> switchToRoot(scene, mainMenuRoot, primaryStage));

        mainMenuController.setGameSwitcher(() -> {
            System.out.println("file: " + mainMenuController.getWorld().getWorldFile());

            // MusicList.mainMenuMusic.stop();
            // MusicList.backgroundMusic.start(true);

            // load the main game
            LoopManiaWorldControllerLoader loopManiaLoader = null;
            try {
                loopManiaLoader =
                    new LoopManiaWorldControllerLoader(mainMenuController.getWorld(), mainMenuController.getMode());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                assert loopManiaLoader != null;
                this.mainController = loopManiaLoader.loadController();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            mainController.setVictoryScene(this.victoryRoot);
            this.victoryController.setMainMenuSwitcher(() -> {
                // MusicList.victoryMusic.stop();
                // MusicList.mainMenuMusic.start(true);
                switchToRoot(scene, mainMenuRoot, primaryStage);
            });

            mainController.setGameOverScene(this.gameOverRoot);
            this.gameOverController.setMainMenuSwitcher(() -> {
                // MusicList.gameOverMusic.stop();
                // MusicList.mainMenuMusic.start(true);
                switchToRoot(scene, mainMenuRoot, primaryStage);
            });

            mainController.setMainMenuSwitcher(() -> {
                // MusicList.backgroundMusic.stop();
                // MusicList.mainMenuMusic.start(true);
                switchToRoot(scene, mainMenuRoot, primaryStage);
            });

            FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/fxmls/LoopManiaView.fxml"));
            gameLoader.setController(mainController);
            Parent gameRoot = null;
            try {
                gameRoot = gameLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // deploy the main onto the stage
            assert gameRoot != null;
            gameRoot.requestFocus();

            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });

        primaryStage.setOnCloseRequest(handle -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // wrap up activities when exit program
        // MusicList.backgroundMusic.stop();
        this.mainController.terminate();
    }

    /**
     * Creates the main application scene.
     *
     * @param root the main application layout.
     * @return the created scene.
     */
    private Scene createScene(Parent root) {
        // Scene scene = new Scene(root);
        // JMetro jMetro = new JMetro(Style.LIGHT);
        // jMetro.setScene(scene);
        // scene.getStylesheets().setAll(getClass().getResource("vista.css").toExternalForm());
        return new Scene(root);
    }

    /**
     * switch to a different Root
     */
    public void switchToRoot(Scene scene, Parent root, Stage stage) {
        scene.setRoot(root);
        root.requestFocus();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}
