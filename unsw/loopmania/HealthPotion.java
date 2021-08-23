package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class HealthPotion extends Item {

    static final int PRICE = 100;
    static final int HEALTH = 1;
    static final int DROP_RATE = 1;

    /**
     * constructs a healthPotion object
     * 
     * @param x
     * @param y
     */
    public HealthPotion(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, PRICE, HEALTH, DROP_RATE);
    }

    /**
     * Restores player health back to 100.
     * 
     * @param c character object that will use the health potion
     */
    public void use(Character c) {
        c.setHealth(100);
        this.deteriorate();
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
