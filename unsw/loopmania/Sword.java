package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Sword extends Weapon {

    static final int DAMAGE = 30;
    static final int PRICE = 100;
    static final int DROP_RATE = 1;
    static final int HEALTH = 100;

    public Sword(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, DAMAGE, PRICE, HEALTH, DROP_RATE);
    }

    public void attack(BasicEnemy e, int multiplier, int scalarDecrease) {
        e.setHealth(e.getHealth() - (DAMAGE * multiplier) + scalarDecrease);
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