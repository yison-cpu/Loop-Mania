package test;

import unsw.loopmania.*;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;

public class testTowerBuilding
{

    @Test
    public void testAddTower() {

        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, new ArrayList<>());
        TowerCard towerCard = new TowerCard(new SimpleIntegerProperty(newWorld.getCardEntities().size()), new SimpleIntegerProperty(0));
        newWorld.loadTowerCard();

        Building towerBuilding = newWorld.convertCardToBuildingByCoordinates(towerCard.getX(), towerCard.getY(), 0, 0);
        assertEquals(newWorld.getBuildings(), towerBuilding);
    }

    @Test
    public void testTowerAttack() {

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair <Integer, Integer> (0,0));
        orderedPath.add(new Pair <Integer, Integer> (0,1));
        orderedPath.add(new Pair <Integer, Integer> (1,1));
        orderedPath.add(new Pair <Integer, Integer> (1,0));

        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, orderedPath);
        TowerCard towerCard = new TowerCard(new SimpleIntegerProperty(newWorld.getCardEntities().size()), new SimpleIntegerProperty(0));
        newWorld.loadTowerCard();

        Building towerBuilding = newWorld.convertCardToBuildingByCoordinates(towerCard.getX(), towerCard.getY(), 0, 0);
        assertEquals(newWorld.getBuildings(), towerBuilding);

        Zombie zombie = new Zombie(new PathPosition(0, newWorld.getOrderedPath()), 1);
        newWorld.addEnemy(zombie);

        assertTrue(zombie.getHealth() == 100);
        towerBuilding.buildingEffect(newWorld);
        assertTrue(zombie.getHealth() == 95);
    }

}