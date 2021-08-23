package unsw.loopmania;

import java.io.IOException;
import javafx.fxml.FXML;

/**
 * controller for the main menu. TODO = you could extend this, for example with
 * a settings menu, or a menu to load particular maps.
 */
public class MainMenuController {
    /**
     * facilitates switching to main game
     */
    private MenuSwitcher selectModeSwitcher;

    public void setSelectModeSwitcher(MenuSwitcher selectModeSwitcher) {
        this.selectModeSwitcher = selectModeSwitcher;
    }

    /**
     * facilitates switching to main game upon button click
     * 
     * @throws IOException
     */
    @FXML
    private void switchToSelectMode() throws IOException {
        selectModeSwitcher.switchMenu();
    }
}
