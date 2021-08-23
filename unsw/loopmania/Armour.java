package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents Armour item in the backend
 */
public class Armour extends Item {

    static final int PRICE = 100;
    static final int HEALTH = 100;
    static final int DROP_RATE = 1;

    /**
     * creates Armour object
     * 
     * @param x
     * @param y
     */
    public Armour(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, PRICE, HEALTH, DROP_RATE);
    }

    /**
     * the item is being discarded as being the oldest item due to the character
     * receives a new item while the inventory is full, the character will receive
     * item value worth of gold
     * 
     * @param world
     */
    public void discardItem(LoopManiaWorld world) {
        super.discardItem(world, price);
    }

}
