package test;

import unsw.loopmania.*;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

import org.junit.jupiter.api.Test;

import unsw.loopmania.LoopManiaWorld;

import javafx.beans.property.IntegerProperty;

import javafx.beans.property.SimpleIntegerProperty;

public class testTowerCard
{

    @Test
    public void testAddTowerCard() {

        TowerCard towerCard = createAndLoadTowerCard();

        // assert the should exist booelan property attached to the card is true
        assertTrue(towerCard.shouldExist().get() == true);

    }

    @Test
    public void testDestroyTowerCard() {

        TowerCard towerCard = createAndLoadTowerCard();

        towerCard.destroy();

        // assert the should exist booelan property attached to the card is false
        assertTrue(towerCard.shouldExist().get() == false);

    }

    @Test
    public void testInvalidPosition() {

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair <Integer, Integer> (0,0));
        orderedPath.add(new Pair <Integer, Integer> (0,1));
        orderedPath.add(new Pair <Integer, Integer> (1,1));
        orderedPath.add(new Pair <Integer, Integer> (1,0));

        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, orderedPath);
        TowerCard towerCard = new TowerCard(new SimpleIntegerProperty(cardEntities.size()), new SimpleIntegerProperty(0));
        newWorld.loadTowerCard();

        // placed on the path
        assertThrows(invalidDragPosition, ()-> {
            newWorld.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        });
        // placed on non-path tiles not adjacent to the path
        assertThrows(invalidDragPosition, ()-> {
            newWorld.convertCardToBuildingByCoordinates(0, 0, 5, 5);
        });
    }

    public TowerCard createAndLoadTowerCard() {
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, new ArrayList<>());
        TowerCard towerCard = new TowerCard(new SimpleIntegerProperty(newWorld.getCardEntities().size()),
                new SimpleIntegerProperty(0));

        // it should be newWorld.loadCard if we decide to implement strategy pattern,
        //
        newWorld.loadTowerCard();

        return towerCard;
    }
}