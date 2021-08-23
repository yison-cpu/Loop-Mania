package unsw.loopmania;

import java.io.IOException;
import javafx.fxml.FXML;

public class PurchaseConfirmController {

    private MenuSwitcher shopSwitcher;

    public void setShopSwitcher(MenuSwitcher shopSwitcher) {
        this.shopSwitcher = shopSwitcher;
    }

    /**
     * facilitates switching to main game upon button click
     * 
     * @throws IOException
     */
    @FXML
    private void switchToShop() throws IOException {
        shopSwitcher.switchMenu();
    }
}
