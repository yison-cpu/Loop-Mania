package unsw.loopmania;

import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;

public class HeroCastle extends Building {

    int heroCastleCycle = -1;
    ArrayList<Item> shopItems = new ArrayList<Item>();

    public HeroCastle(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public void buyItem(Item item) {

    }

    @Override
    public void update(Subject s) {
        if (s instanceof Character) {
            LoopManiaWorld world = ((Character) s).getWorld();
            buildingEffect(world);
        }
    }

    /**
     * when character enters HeroCastle, increment numberOfLoops and display shop
     * 
     * pre: character coordinate is in castle, character isInCastle is false and
     * character loop doesn't match heroCastleLoop
     */

    @Override
    public void buildingEffect(LoopManiaWorld world) {
        Character character = world.getCharacter();

        character.setInCastle(true);
        heroCastleCycle = character.getCycle();
    }

    public int getHeroCastleCycle() {
        return this.heroCastleCycle;
    }
}
