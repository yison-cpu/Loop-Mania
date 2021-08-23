package test;

import unsw.loopmania.*;
import unsw.loopmania.Character;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;


import javafx.beans.property.SimpleIntegerProperty;

public class testShield {

    @Test
    public void testAddShield() {
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, new ArrayList<>());

        // this should be newWorld.addUnequippeditem(staticEntity) if we decide to
        // implement strategy pattern (and shield class)
        Pair<Integer, Integer> firstAvailableSlot = newWorld.ensureUnequippedSlot();

        Shield shield = new Shield(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
        new SimpleIntegerProperty(firstAvailableSlot.getValue1()));

        newWorld.addUnequippedShield();
        assertTrue(newWorld.getUnequippedInventoryItems() == shield);
        
    }

    @Test
    public void testDestroyShield() {
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, new ArrayList<>());
        Pair<Integer, Integer> firstAvailableSlot = newWorld.ensureUnequippedSlot();

        Shield shield = new Shield(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
            new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        shield.destroy();
        assertTrue(shield.shouldExist().get() == false);
    }

    @Test
    public void testShieldProtection() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0, 0));
        orderedPath.add(new Pair<Integer, Integer>(0, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 0));

        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, orderedPath);

        Character character = new Character(new PathPosition(0, orderedPath));
        newWorld.setCharacter(character);

        Pair<Integer, Integer> firstAvailableSlot = newWorld.ensureUnequippedSlot();
        Shield shield = new Shield(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
            new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        shield.destroy();

        Vampire vampire = new Vampire(new PathPosition(0, orderedPath), 1);
        newWorld.addEnemy(vampire);

        double health1 = character.getHealth();
        vampire.attack(character);
        double health2 = character.getHealth();
        double healthDiff1 = health1 - health2;

        newWorld.addUnequippedShield();
        character.equip(shield);

        vampire.attack(character);
        double health3 = character.getHealth();
        double healthDiff2 = health2 - health3;

        assertTrue(healthDiff2 <= healthDiff1);

    }

}
