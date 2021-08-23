package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public abstract class Item extends StaticEntity {

    protected int price;
    protected int health;
    protected int dropRate;

    public Item(SimpleIntegerProperty x, SimpleIntegerProperty y, int price, int health, int dropRate) {
        super(x, y);
        this.price = price;
        this.health = health;
        this.dropRate = dropRate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void deteriorate() {
        this.health -= dropRate;
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
        Character character = world.getCharacter();
        character.setGold(character.getGold() + price);
    }

}
