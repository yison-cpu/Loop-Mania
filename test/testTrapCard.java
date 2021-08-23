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

public class testTrapCard {
    @Test
    // create a trapcard and check if it exists
    public void testCheckExists() {
        TrapCard trapCard = createAndLoadTrapCard();

        assertTrue(trapCard.shouldExist().get() == true);
    }

    @Test
    // create trap card, destroy it and check if it still exists
    public void testCheckRemoved() {
        TrapCard trapCard = createAndLoadTrapCard();
        trapCard.destroy();

        assertTrue(trapCard.shouldExist().get() == false);
    }

    @Test
    // create a path, load a new world, create and load a trapCard
    // place trapCard on a tile that is not part of the path
    // check if that move is invalid
    public void testInvalidCardPlacement() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair <Integer, Integer> (0, 0));
        orderedPath.add(new Pair <Integer, Integer> (0, 1));
        orderedPath.add(new Pair <Integer, Integer> (1, 1));
        orderedPath.add(new Pair <Integer, Integer> (1, 0));

        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, orderedPath);
        TrapCard trapCard = new TrapCard(new SimpleIntegerProperty(cardEntities.size()),
                        new SimpleIntegerProperty(0));

        newWorld.loadTrapCard();

        // placing card on a non-path tile
        assertThrows(isValidDraggingPosition, ()-> {
            newWorld.convertCardToBuildingByCoordinates(0, 0, 6, 6);
        });
    }

    @Test
    // create a path, load a new world, create and load a trapCard
    // place the card on a tile that is a path tile and check if that move is valid
    public void testValidCardPlacement() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair <Integer, Integer> (0, 0));
        orderedPath.add(new Pair <Integer, Integer> (0, 1));
        orderedPath.add(new Pair <Integer, Integer> (1, 1));
        orderedPath.add(new Pair <Integer, Integer> (1, 0));

        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, orderedPath);
        TrapCard trapCard = new TrapCard(new SimpleIntegerProperty(newWorld.getCardEntities().size()),
                        new SimpleIntegerProperty(0));

        newWorld.loadTrapCard();

        // placing card on a path tile
        assertEquals(validDragPosition, ()-> {
            newWorld.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        });
    }

    public TrapCard createAndLoadTrapCard() {
        LoopManiaWorld world = new LoopManiaWorld(10, 10, new ArrayList<>());
        TrapCard trapCard = new TrapCard(new SimpleIntegerProperty(world.getCardEntities().size()), 
                new SimpleIntegerProperty(0));

        world.loadTrapCard();

        return trapCard;
    }
}
