package test;

import unsw.loopmania.*;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import javafx.beans.property.IntegerProperty;

import javafx.beans.property.SimpleIntegerProperty;


//Basic test for the inventory class;
public class testInventory {
    private LoopManiaWorld newWorld;

    private Character character;
    private Inventory inventory;



    /*
    @BeforeEach
    public void setUp() throws Exception {
        LoopManiaWorld newWorld = make_path_and_world();
        Character character = new Character(0, 0, 200, 1);
        character.setPosition(new Pair<Integer, Integer> (0,1));

        //setting sword, sheid, staff
        Sword sword =  new Sword(new SimpleIntegerProperty(newWorld.getFirstAvailableSlot().getValue0()),
        new SimpleIntegerProperty(newWorld.getFirstAvailableSlot().getValue1()));


        Shield shield = new Shield(new SimpleIntegerProperty(newWorld.getFirstAvailableSlot().getValue0()),
                new SimpleIntegerProperty(newWorld.getFirstAvailableSlot().getValue1()));

        
        Staff staff = new Staff(new SimpleIntegerProperty(newWorld.getFirstAvailableSlot().getValue0()),
        new SimpleIntegerProperty(newWorld.getFirstAvailableSlot().getValue1()));


    }
    // Assume all other sup class tests has been done
    // the inventory test would be around setInventory and EquipedInventory

    @Test
    public void test_setInventory(){


        newWorld.addUnequippedShield();
        newWorld.addUnequippedSword();
        newWorld.addUnequippedStaff();
        assertTrue(newWorld.getUnequippedInventoryItems()[0][0] == shield);
        assertTrue(newWorld.getUnequippedInventoryItems()[0][1] == sword);
        assertTrue(newWorld.getUnequippedInventoryItems()[0][0] == staff);
     
    }


    @Aftereach

    @AfterEach
    public void cleanUp() throws Exception {
        newWorld.delete();
        character.delete();
        sword.delete();
        staff.delete();
        sheid.delete();
    }




*/
}