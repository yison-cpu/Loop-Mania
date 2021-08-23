package test;

import unsw.loopmania.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import unsw.loopmania.LoopManiaWorld;

import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;


public class testTrapBuilding {
    @Test
    // create a new world, create card and load it into the world
    // tests if trapBuilding exists in world instance
    public void testAddTrap() {
        LoopManiaWorld world = new LoopManiaWorld(10, 10, new ArrayList<>());
        TrapCard trapCard = new TrapCard(new SimpleIntegerProperty(world.getCardEntities().size()), new SimpleIntegerProperty(0));
        world.loadTrapCard();

        Building trapBuilding = world.convertCardToBuildingByCoordinates(trapCard.getX(), trapCard.getY(), 0, 0);
        assertEquals(world.getBuildings(), trapBuilding);
    }

    @Test
    // create a new path, world, and load trap card into the world
    // create a new enemy and check if enemy takes damage from trapCard
    public void testTrapAttack() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair <Integer, Integer> (0, 0));
        orderedPath.add(new Pair <Integer, Integer> (0, 1));
        orderedPath.add(new Pair <Integer, Integer> (1, 1));
        orderedPath.add(new Pair <Integer, Integer> (1, 0));

        LoopManiaWorld world = new LoopManiaWorld(10, 10, orderedPath);
        TrapCard trapCard = new TrapCard(new SimpleIntegerProperty(world.getCardEntities().size()), new SimpleIntegerProperty(0));
        world.loadTrapCard();

        Building trapBuilding = world.convertCardToBuildingByCoordinates(trapCard.getX(), trapCard.getY(), 0, 0);
        assertEquals(world.getBuildings(), trapBuilding);

        Zombie zombie = new Zombie(new PathPosition(0, world.getOrderedPath()), 1);
        world.addEnemy(zombie);

        int currHealth = zombie.getHealth();
        trapBuilding.buildingEffect(world);
        int newHealth = zombie.getHealth();

        assertTrue(newHealth == currHealth - 50);
    }

    // might need to add a test to see if trapCard only damages enemy when they are standing on the same path
    // test: enemy on adjacent path tile
    // test: enemy on sane path tile

}
