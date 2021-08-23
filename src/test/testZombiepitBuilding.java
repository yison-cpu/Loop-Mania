package test;

import unsw.loopmania.*;
import unsw.loopmania.Character;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleIntegerProperty;
import org.javatuples.Pair;


public class testZombiepitBuilding {

    private Character character;
   
    @Test
    public void testAddingZombiePitBuilding(){
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, new ArrayList<>());
        ZombiePitCard zombiePitCard = new ZombiePitCard(new SimpleIntegerProperty(newWorld.getCardEntities().size()), new SimpleIntegerProperty(0));
        newWorld.loadZombiePitCard();

        Building zombiePitBuilding = newWorld.convertCardToBuildingByCoordinates(zombiePitCard.getX(), zombiePitCard.getY(), 0, 0);
        assertEquals(newWorld.getBuildings(), zombiePitBuilding);
    }



    
    @Test
    public void testZombiePitBuildingSpawn() {

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair <Integer, Integer> (0,0));
        orderedPath.add(new Pair <Integer, Integer> (0,1));
        orderedPath.add(new Pair <Integer, Integer> (1,1));
        orderedPath.add(new Pair <Integer, Integer> (1,0));

        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, orderedPath);
        ZombiePitCard zombiePitCard = new ZombiePitCard(new SimpleIntegerProperty(newWorld.getCardEntities().size()), new SimpleIntegerProperty(0));
        newWorld.loadZombiePitCard();

        Building zombiePitBuilding = newWorld.convertCardToBuildingByCoordinates(zombiePitCard.getX(), zombiePitCard.getY(), 0, 0);
        assertEquals(newWorld.getBuildings(), zombiePitBuilding);

        //check the character finish the loop or not;
        
        Zombie zombie = new Zombie(new PathPosition(0, newWorld.getOrderedPath()), 1);
        newWorld.addEnemy(zombie);


        int num_of_loop = character.getLoopNumber();
        int num_of_zombie = zombie.getNumber();

        if(num_of_loop > 1){

            // spawn number of zombie as the num_of_loop
            
            for (int i = 0; i < num_of_loop; i++){
            Zombie zombie2 = new Zombie(new PathPosition(0, newWorld.getOrderedPath()), 1); // dmg, battleRadius, supportRadius, health, speed
            zombie.setPosition(new Pair<Integer, Integer> (0,1)); // Assume spawn all in one exact position;
            }

            int num_of_zombie_after = zombie.getNumber();
            assertTrue (num_of_zombie_after == num_of_zombie + num_of_loop);
        }

    }


}