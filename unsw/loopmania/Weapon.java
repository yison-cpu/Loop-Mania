package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents an equipped or unequipped sword in the backend world
 */
public abstract class Weapon extends Item {

    private int damage;
    private int price;
    private int health;
    private int dropRate;

    public Weapon(SimpleIntegerProperty x, SimpleIntegerProperty y, int damage, int price, int health, int dropRate) {
        super(x, y, price, health, dropRate);
        this.damage = damage;
        this.price = price;
        this.health = health;
        this.dropRate = dropRate;
    }

    public abstract void attack(BasicEnemy e, int multiplier, int scalarDecrease);

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * the item is being discarded as being the oldest item due to the character
     * receives a new item while the inventory is full, the character will receive
     * item value worth of gold
     * 
     * @param world     the backend world of LoopMania Game
     * @param itemValue the value of an item
     */
    public void discardItem(LoopManiaWorld world, int price) {
        super.discardItem(world, price);
    }
}
