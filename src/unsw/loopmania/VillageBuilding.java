package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

public class VillageBuilding extends Building {

    int villageCycle = 0;

    /**
     * constructs a village object
     * 
     * @param x
     * @param y
     */
    public VillageBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    @Override
    public void update(Subject s) {
        if (s instanceof Character) {
            LoopManiaWorld world = ((Character) s).getWorld();
            buildingEffect(world);
        }
    }

    /**
     * heals the character to full health when they walk past
     * 
     * @param world the backend of loop mania world
     */
    @Override
    public void buildingEffect(LoopManiaWorld world) {
        Character character = world.getCharacter();
        // add 10% health
        character.setHealth(character.getHealth() + Character.MAX_HEALTH * 0.1);
        villageCycle = character.getCycle();
    }

    public int getVillageCycle() {
        return villageCycle;
    }
}
