package test;

import unsw.loopmania.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;


public class testVillageBuilding {
    @Test
    public void testAddVillage(){
        LoopManiaWorld world = new LoopManiaWorld(10, 10, new ArrayList<>());
        VillageCard villageCard = new VillageCard(new SimpleIntegerProperty(world.getCardEntities().size()), new SimpleIntegerProperty(0));
        world.loadVillageCard();

        Building village = world.convertCardToBuildingByCoordinates(villageCard.getX(), villageCard.getY(), 0, 0);
        assertEquals(world.getBuildings(), village);
    }
}
