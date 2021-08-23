package unsw.loopmania;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * a basic form of building in the world
 */
public class TowerBuilding extends Building {

    static final int TOWER_RADIUS = 5;
    static final int DAMAGE = 5;

    public TowerBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
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
     * attack all enemy that is in radius during a battle
     * 
     * @param world backend of loop mania world
     */
    public void buildingEffect(LoopManiaWorld world) {

        List<BasicEnemy> enemies = world.getEnemies();

        for (BasicEnemy e : enemies) {
            // if the enemy is in radius and is in battle
            if (Math.pow((this.getX() - e.getX()), 2) + Math.pow((this.getY() - e.getY()), 2) < Math.pow(TOWER_RADIUS,
                    2) && e.isInBattle()) {
                int beforeHealth = e.getHealth();
                e.setHealth(beforeHealth - DAMAGE);
            }
        }
    }

}
