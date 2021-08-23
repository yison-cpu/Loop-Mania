package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents helmet item in the backend
 */
public class Helmet extends Item {

    static final int PRICE = 100;
    static final int HEALTH = 100;
    static final int DROP_RATE = 1;

    public Helmet(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, PRICE, HEALTH, DROP_RATE);
    }

    /**
     * Discard the oldest helmet in the inventory when it is full, the character
     * will receive gold equivalent to the price of the discarded item.
     * 
     * @param world the backend world of LoopMania Game
     */
    public void discardItem(LoopManiaWorld world) {
        super.discardItem(world, price);
    }

}
