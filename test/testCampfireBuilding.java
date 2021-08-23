package test;

import unsw.loopmania.*;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import javafx.beans.property.IntegerProperty;

import javafx.beans.property.SimpleIntegerProperty;

public class testCampfireBuilding {

    @Test
    public void testAddCampire() {

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0, 0));
        orderedPath.add(new Pair<Integer, Integer>(0, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 0));
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, orderedPath);
        CampfireCard campfireCard = new CampfireCard(new SimpleIntegerProperty(newWorld.getCardEntities().size()),
                new SimpleIntegerProperty(0));
        newWorld.loadCampfireCard();

        CampfireBuilding CampfireBuilding = newWorld.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        assertEquals(newWorld.getBuildingEntities()[0][0], campfireBuilding);
    }

    @Test
    public void testCampireBuff() {

        LoopManiaWorld newWorld = makePathAndWorld();

        Character character = new Character(new PathPosition(0, orderedPath));
        newWorld.setCharacter(character);

        CampfireCard campfireCard = new CampfireCard(new SimpleIntegerProperty(newWorld.getCardEntities().size()),
                new SimpleIntegerProperty(0));
        newWorld.loadCampfireCard();

        Zombie zombie = new Zombie(new PathPosition(0, orderedPath));
        newWorld.addEnemy(zombie);

        int health1 = zombie.getHealth();
        character.attack();
        int health2 = zombie.getHealth();
        int healthDiff1 = health1 - health2;

        CampfireBuilding CampfireBuilding = newWorld.convertCardToBuildingByCoordinates(0, 0, 0, 2);

        character.attack();
        int health3 = zombie.getHealth();
        int healthDiff2 = health2 - health3;

        assertTrue(healthDiff2 == 2 * healthDiff1);
    }

    public LoopManiaWorld makePathAndWorld() {

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0, 0));
        orderedPath.add(new Pair<Integer, Integer>(0, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 0));
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, orderedPath);
        return newWorld;
    }
}
