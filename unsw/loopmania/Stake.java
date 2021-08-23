package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public class Stake extends Weapon {

    static final int DAMAGE = 20;
    static final int PRICE = 150;
    static final int HEALTH = 100;
    static final int DROP_RATE = 1;

    public Stake(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, DAMAGE, PRICE, HEALTH, DROP_RATE);
    }

    public void attack(BasicEnemy e, int multiplier, int scalarDecrease) {
        if (e instanceof Vampire) {
            multiplier = multiplier * 3;
        }
        e.setHealth(e.getHealth() - (this.getDamage() * multiplier) + scalarDecrease);
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