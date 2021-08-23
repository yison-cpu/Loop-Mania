package unsw.loopmania;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ShopController {
    private LoopManiaWorld world;
    /**
     * facilitates switching to game
     */
    private MenuSwitcher gameSwitcher;
    private MenuSwitcher purchaseConfirmSwitcher;

    @FXML
    private Label goldLabel;

    public ShopController(LoopManiaWorld world) {
        this.world = world;
    }

    @FXML
    public void initialize() {
        goldLabel.textProperty().bind(world.getCharacter().goldProperty().asString());
    }

    public void setGameSwitcher(MenuSwitcher gameSwitcher) {
        this.gameSwitcher = gameSwitcher;
    }

    public void setPurchaseConfirmSwitcher(MenuSwitcher purchasConfirmSwitcher) {
        this.purchaseConfirmSwitcher = purchasConfirmSwitcher;
    }

    /**
     * facilitates switching to main game upon button click
     * 
     * @throws IOException
     */
    @FXML
    private void switchToGame() throws IOException {
        gameSwitcher.switchMenu();
    }

    /**
     * facilitates switching to purchase confirm upon button click
     * 
     * @throws IOException
     */
    @FXML
    private void switchToPurchaseConfirm() throws IOException {
        purchaseConfirmSwitcher.switchMenu();
    }
}
