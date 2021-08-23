package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

public class BarracksBuilding extends Building {

    public BarracksBuilding(SimpleIntegerProperty x, SimpleIntegerProperty y) {
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
     * When the character is on the same tile as the barracks, a new allied soldier
     * will spawn.
     * 
     */
    @Override
    public void buildingEffect(LoopManiaWorld world) {
        Character character = world.getCharacter();
        List<Pair<Integer, Integer>> orderedPath = world.getOrderedPath();
        ArrayList<AlliedSoldier> currentAlliedSoldiersList = world.getAlliedSoldiers();
        int characterIndexPosition = orderedPath
                .indexOf(new Pair<Integer, Integer>(character.getX(), character.getY()));

        if (currentAlliedSoldiersList.size() < 3) {
            // if character is in barracks, create allied soldier
            AlliedSoldier alliedSoldier = new AlliedSoldier(new PathPosition(characterIndexPosition, orderedPath)); // need
                                                                                                                    // method
                                                                                                                    // to
                                                                                                                    // determine
                                                                                                                    // current
            // character indexInPath
            world.addAlliedSoldier(alliedSoldier);
        }
    }
}
