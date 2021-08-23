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

public class testZombiepitCard {

    @Test
    public void testAddzombiePitCard() {

        ZombiePitCard zombiePitCard = createAndLoadZombiePitCard();

        // assert the should exist booelan property attached to the card is true
        assertTrue(zombiePitCard.shouldExist().get() == true);

    }

    @Test
    //create zombie pit card and destroy it and check if it still exists
    public void testRemovezombiePitCard() {

        ZombiePitCard zombiePitCard = createAndLoadZombiePitCard();
        zombiePitCard.destroy();

        // assert the boolean statement is false;
        assertTrue(zombiePitCard.shouldExist().get() == false);

    }



    @Test
    public void testInvalidPosition() {
        // create a path, load a new world, create and load a trapCard
        // place trapCard on a tile that is not part of the path
        // check if that move is invalid

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair <Integer, Integer> (0,0));
        orderedPath.add(new Pair <Integer, Integer> (0,1));
        orderedPath.add(new Pair <Integer, Integer> (1,1));
        orderedPath.add(new Pair <Integer, Integer> (1,0));

        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, orderedPath);
        ZombiePitCard zombiePitCard = new ZombiePitCard(new SimpleIntegerProperty(cardEntities.size()), 
                new SimpleIntegerProperty(0));
        
        newWorld.loadTowerCard();

         //placed on the path
        assertThrows(validDragPosition, ()-> {
            newWorld.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        });

        // placed on non-path tiles not adjacent to the path
        assertThrows(invalidDragPosition, ()-> {
            newWorld.convertCardToBuildingByCoordinates(0, 0, 5, 5);
        });
    }

    public ZombiePitCard createAndLoadZombiePitCard() {
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, new ArrayList<>());
        ZombiePitCard zombiePitCard = new ZombiePitCard(new SimpleIntegerProperty(newWorld.getCardEntities().size()),
                new SimpleIntegerProperty(0));

        // it should be newWorld.loadCard if we decide to implement strategy pattern,
        //
        newWorld.loadzombiePitCard();

        return zombiePitCard;
    }

    
}