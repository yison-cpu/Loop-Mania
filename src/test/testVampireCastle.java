package test;
import unsw.loopmania.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import javafx.beans.property.SimpleIntegerProperty;

public class testVampireCastle {
    @Test
    public void testAddVampireCastle(){
        LoopManiaWorld world = new LoopManiaWorld(10, 10, new ArrayList<>());
        VampireCastleCard barCard = new VampireCastleCard(new SimpleIntegerProperty(world.getCardEntities.size()), new SimpleIntegerProperty(0));
        world.loadVampireCastleCard();

        VampireCastle b = world.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        assertEquals(world.getBuildingEntities()[0][0], b);
    }
}
