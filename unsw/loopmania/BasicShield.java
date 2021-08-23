package unsw.loopmania;

import java.util.Random;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public abstract class BasicShield extends Item {

    static final int PRICE = 100;
    static final int HEALTH = 1;
    static final int DROP_RATE = 1;

    public BasicShield(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, PRICE, HEALTH, DROP_RATE);
    }

    public abstract int getDropRate();

    /**
     * this method is called when the vampire triggers a critical attack and the
     * character had equipped a shield, the shield have a 60% chance of preventing
     * the vampire's critical attack
     * 
     * @return true if the shield had prevented a critical attack from a vampire
     */
    public Boolean isBlockCriticalBite() {
        // 60% chance block critical bite
        int randomCriticalWithShield = (new Random()).nextInt(9);
        if (randomCriticalWithShield <= 3) {
            this.deteriorate();
            return true;
        } else {
            return false;
        }
    }

}
