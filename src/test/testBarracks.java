package test;

import unsw.loopmania.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;

public class testBarracks {
    @Test
    public void testAddBarracks(){
        LoopManiaWorld world = new LoopManiaWorld(10, 10, new ArrayList<>());
        BarracksCard barCard = new BarracksCard(new SimpleIntegerProperty(world.getCardEntities().size()), new SimpleIntegerProperty(0));
        world.loadBarracksCard();

        Building barracks = world.convertCardToBuildingByCoordinates(barCard.getX(), barCard.getY(), 0, 0);
        assertEquals(world.getBuildings(), barracks);
    }
    @Test 
    public void test_spawn_soldiers(){
        
    }

}