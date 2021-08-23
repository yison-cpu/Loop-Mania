package test;
import unsw.loopmania.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;

public class testHeroCastle {
    @Test
    public void testAddHeroCastle(){
        LoopManiaWorld world = new LoopManiaWorld(10, 10, new ArrayList<>());
        HeroCastleCard heroCard = new HeroCastleCard(new SimpleIntegerProperty(world.getCardEntities.size()), new SimpleIntegerProperty(0));
        world.loadHeroCastleCard();

        HeroCastle b = world.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        assertEquals(world.getBuildingEntities()[0][0], b);
}
