package unsw.loopmania;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * a basic form of building in the world
 */
public class CampfireBuilding extends Building {

    static final int CAMPFIRE_RADIUS = 3;

    public CampfireBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
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
     * boost the character's attack damage if the character is in battle and in
     * radius of the camp fire
     * 
     * @param world
     */
    public void buildingEffect(LoopManiaWorld world) {
        Character character = world.getCharacter();
        int damageMultiplier = character.getDamageMultiplier();
        character.setDamageMultiplier(2 * damageMultiplier);
    }
}
