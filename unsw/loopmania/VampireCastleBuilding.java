package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * a basic form of building in the world
 */
public class VampireCastleBuilding extends Building {

    /**
     * constructs a vampire castle building
     * 
     * @param x
     * @param y
     */
    public VampireCastleBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
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
     * spawns a vampire
     * 
     * @param world backend of loop mania world
     */
    public void buildingEffect(LoopManiaWorld world) {

        Character character = world.getCharacter();
        List<BasicEnemy> spawningEnemies = world.getSpawningEnemies();
        List<BasicEnemy> enemies = world.getEnemies();
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        List<Building> buildings = world.getBuildings();

        List<Pair<Integer, Integer>> allowedPosition = new ArrayList<>();
        int CharacterIndexPosition = orderedPath
                .indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));
        // inclusive start and exclusive end of range of positions not allowed
        int startNotAllowed = (CharacterIndexPosition - 2 + orderedPath.size()) % orderedPath.size();
        int endNotAllowed = (CharacterIndexPosition + 3) % orderedPath.size();
        // for allowed path tiles
        for (int i = endNotAllowed; i != startNotAllowed; i = (i + 1) % orderedPath.size()) {
            boolean isBuilding = false;
            // loop all building, if there is same position, position is not allowed
            for (Building building : buildings) {
                int BuildingIndexPosition = orderedPath
                        .indexOf(new Pair<Integer, Integer>(building.getX(), building.getY()));
                if (i == BuildingIndexPosition) {
                    isBuilding = true;
                }
            }
            if (!isBuilding) {
                allowedPosition.add(orderedPath.get(i));
            }
        }

        List<Pair<Integer, Integer>> SpawnCandidates = new ArrayList<>();

        // the spawn position
        Pair<Integer, Integer> pos;
        // check the four neighbours, if is pathtile, add to spawn candidates
        // UP
        if (orderedPath.contains(new Pair<Integer, Integer>(this.getX(), this.getY() + 1))) {
            pos = new Pair<Integer, Integer>(this.getX(), this.getY() + 1);
            if (allowedPosition.contains(pos)) {
                SpawnCandidates.add(pos);
            }
            // DOWN
        }
        if (orderedPath.contains(new Pair<Integer, Integer>(this.getX(), this.getY() - 1))) {
            pos = new Pair<Integer, Integer>(this.getX(), this.getY() - 1);
            if (allowedPosition.contains(pos)) {
                SpawnCandidates.add(pos);
            }
            // LEFT
        } else if (orderedPath.contains(new Pair<Integer, Integer>(this.getX() - 1, this.getY()))) {
            pos = new Pair<Integer, Integer>(this.getX() - 1, this.getY());
            if (allowedPosition.contains(pos)) {
                SpawnCandidates.add(pos);
            }
            // RIGHT
        } else {
            pos = new Pair<Integer, Integer>(this.getX() + 1, this.getY());
            if (allowedPosition.contains(pos)) {
                SpawnCandidates.add(pos);
            }
        }

        Random rand = new Random();
        // choose random choice
        Pair<Integer, Integer> spawnPosition = null;
        // choose random choice
        if (SpawnCandidates.size() > 0) {
            spawnPosition = SpawnCandidates.get(rand.nextInt(SpawnCandidates.size()));
        } else {
            spawnPosition = allowedPosition.get(rand.nextInt(allowedPosition.size()));
        }

        int indexInPath = orderedPath.indexOf(spawnPosition);
        Vampire vampire = new Vampire(new PathPosition(indexInPath, orderedPath), 0);

        spawningEnemies.add(vampire);
        world.addEnemy(vampire);
    }

}
