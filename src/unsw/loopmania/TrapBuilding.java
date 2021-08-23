package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * Backend representation of TrapBuilding object
 */

public class TrapBuilding extends Building {
    static final int DAMAGE = 50;

    public TrapBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    public int getDamage() {
        return DAMAGE;
    }

    @Override
    public void update(Subject s) {
        if (s instanceof BasicEnemy) {
            BasicEnemy e = (BasicEnemy) s;
            e.setHealth(e.getHealth() - DAMAGE);
            LoopManiaWorld world = e.getWorld();
            buildingEffect(world);
        }
    }

    /**
     * function to inflict damage on enemies who are standing on same pathTile as
     * TrapCard
     * 
     * @param world backend of loop mania world
     */
    @Override
    public void buildingEffect(LoopManiaWorld world) {
        world.removeBuilding(this);
        this.destroy();
    }
}
