package test;

import unsw.loopmania.*;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import unsw.loopmania.LoopManiaWorld;

import javafx.beans.property.IntegerProperty;

import javafx.beans.property.SimpleIntegerProperty;

public class testtreestump {

    @Test
    public void testAddtreestump() {
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, new ArrayList<>());

        // this should be newWorld.addUnequippeditem(staticEntity) if we decide to
        // implement strategy pattern (and treestump class)
        TreeStump treestump = new TreeStump(new SimpleIntegerProperty(newWorld.getFirstAvailableSlot().getValue0()),
                new SimpleIntegerProperty(newWorld.getFirstAvailableSlot().getValue1()));

        newWorld.addUnequippedtreestump();
        assertTrue(newWorld.getUnequippedInventoryItems()[0][0] == treestump);
    }

    @Test
    public void testDestroytreestump() {
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, new ArrayList<>());

        Treestump treestump = new treestump(new SimpleIntegerProperty(newWorld.getFirstAvailableSlot().getValue0()),
                new SimpleIntegerProperty(newWorld.getFirstAvailableSlot().getValue1()));
        treestump.destroy();
        assertTrue(treestump.shouldExist().get() == false);
    }

    @Test
    public void testtreestumpProtection() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0, 0));
        orderedPath.add(new Pair<Integer, Integer>(0, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 0));

        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, orderedPath);

        Character character = new Character(new PathPosition(0, orderedPath));
        newWorld.setCharacter(character);

        Treestump treestump = new treestump(new SimpleIntegerProperty(newWorld.getFirstAvailableSlot().getValue0()),
                new SimpleIntegerProperty(newWorld.getFirstAvailableSlot().getValue1()));

        Vampire vampire = new Vampire(new PathPosition(0, orderedPath));
        newWorld.addEnemy(vampire);

        vampire.attack();
        int health2 = character.getHealth();
        int healthDiff1 = health1 - health2;

        newWorld.addUnequippedtreestump();
        newWorld.addEquippedtreestump();

        vampire.attack();
        int health3 = character.getHealth();
        int healthDiff2 = health2 - health3;

        assertTrue(healthDiff2 <= healthDiff1);

    }

}
