package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * a building in the world which doesn't move
 */
public abstract class Building extends StaticEntity implements Observer {
    public Building(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    /**
     * a special effect of each building type
     */
    public abstract void buildingEffect(LoopManiaWorld world);
}