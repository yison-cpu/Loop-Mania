package unsw.loopmania;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a rare shield in the backend world
 */
public class TreeStump extends BasicShield {

    static final int PRICE = 1000;
    static final int HEALTH = 1;
    static final int DROP_RATE = 1;

    public TreeStump(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    /**
     * the item is being discarded as being the oldest item due to the character
     * receives a new item while the inventory is full, the character will receive
     * item value worth of gold
     * 
     * @param world the backend world of LoopMania Game
     */
    public void discardItem(LoopManiaWorld world) {
        super.discardItem(world, price);
    }

    @Override
    public int getDropRate() {
        return DROP_RATE;
    }
}
