package unsw.loopmania;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * the main application run main method from this class
 */
public class LoopManiaApplication extends Application {
    // TODO = possibly add other menus?

    /**
     * the controller for the game. Stored as a field so can terminate it when click
     * exit button
     */
    private LoopManiaWorldController mainController;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Play music
        music();
        // set title on top of window bar
        primaryStage.setTitle("Loop Mania");

        // prevent human player resizing game window (since otherwise would see white
        // space)
        // alternatively, you could allow rescaling of the game (you'd have to program
        // resizing of the JavaFX nodes)
        primaryStage.setResizable(false);

        // load the main game
        LoopManiaWorldControllerLoader loopManiaLoader = new LoopManiaWorldControllerLoader(
                "world_with_twists_and_turns.json");
        mainController = loopManiaLoader.loadController();
        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("LoopManiaView.fxml"));
        gameLoader.setController(mainController);
        Parent gameRoot = gameLoader.load();

        // load the main menu
        MainMenuController mainMenuController = new MainMenuController();
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("MainMenuView.fxml"));
        menuLoader.setController(mainMenuController);
        Parent mainMenuRoot = menuLoader.load();

        // load the select game mode page
        SelectModeController selectModeController = new SelectModeController();
        FXMLLoader selectModeLoader = new FXMLLoader(getClass().getResource("SelectModeView.fxml"));
        selectModeLoader.setController(selectModeController);
        Parent selectModeRoot = selectModeLoader.load();

        // load the shop page
        ShopController shopController = new ShopController(loopManiaLoader.load());
        FXMLLoader shopLoader = new FXMLLoader(getClass().getResource("shopView.fxml"));
        shopLoader.setController(shopController);
        Parent shopRoot = shopLoader.load();

        // load the confirm purchase page
        PurchaseConfirmController purchaseConfirmController = new PurchaseConfirmController();
        FXMLLoader purchaseConfirmLoader = new FXMLLoader(getClass().getResource("PurchaseConfirmView.fxml"));
        purchaseConfirmLoader.setController(purchaseConfirmController);
        Parent purchaseConfirmRoot = purchaseConfirmLoader.load();

        // create new scene with the main menu (so we start with the main menu)
        Scene scene = new Scene(mainMenuRoot);

        // set functions which are activated when button click to switch menu is pressed
        // e.g. from main menu to start the game, or from the game to return to main
        // menu
        mainController.setMainMenuSwitcher(() -> {
            switchToRoot(scene, mainMenuRoot, primaryStage);
        });
        mainController.setShopSwitcher(() -> {
            switchToRoot(scene, shopRoot, primaryStage);
        });
        mainMenuController.setSelectModeSwitcher(() -> {
            switchToRoot(scene, selectModeRoot, primaryStage);
        });
        selectModeController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
            mainController.startTimer();
        });
        selectModeController.setMainMenuSwitcher(() -> {
            switchToRoot(scene, mainMenuRoot, primaryStage);
        });
        shopController.setGameSwitcher(() -> {
            switchToRoot(scene, gameRoot, primaryStage);
        });
        shopController.setPurchaseConfirmSwitcher(() -> {
            switchToRoot(scene, purchaseConfirmRoot, primaryStage);
        });
        purchaseConfirmController.setShopSwitcher(() -> {
            switchToRoot(scene, shopRoot, primaryStage);
        });

        // deploy the main onto the stage
        gameRoot.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // wrap up activities when exit program
        mainController.terminate();
    }

    /**
     * switch to a different Root
     */
    private void switchToRoot(Scene scene, Parent root, Stage stage) {
        scene.setRoot(root);
        root.requestFocus();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    MediaPlayer Player;

    public void music() {
        String music = "witcher.mp3";
        Media covert = new Media(new File(music).toURI().toString());
        Player = new MediaPlayer(covert);
        Player.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}