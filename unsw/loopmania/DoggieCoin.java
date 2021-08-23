package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class DoggieCoin extends Item {

    static final int PRICE = 100;
    static final int HEALTH = 1;
    static final int DROP_RATE = 1;

    public DoggieCoin(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y, PRICE, HEALTH, DROP_RATE);
    }

    public void sell(Character c, int stockValue) {
        c.setGold(stockValue);
    }

}
