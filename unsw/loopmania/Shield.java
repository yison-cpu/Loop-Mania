package unsw.loopmania;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Shield extends BasicShield {

    static final int PRICE = 100;
    static final int HEALTH = 100;
    static final int DROP_RATE = 1;

    public Shield(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public int getDropRate() {
        return this.dropRate;
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
}
