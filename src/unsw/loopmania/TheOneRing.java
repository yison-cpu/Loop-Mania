package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped rare item TheOneRing in the backend world
 */
public class TheOneRing extends Item {

    static final int PRICE = 1000;
    static final int HEALTH = 1;
    static final int DROP_RATE = 1;

    public TheOneRing(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, PRICE, HEALTH, DROP_RATE);
        this.dropRate = 0;
    }

    /**
     * resets the characters health to 200 as though respawned
     * 
     * @param c
     */
    public void respawn(Character c) {
        c.setHealth(200);
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
        super.discardItem(world, PRICE);
    }
}