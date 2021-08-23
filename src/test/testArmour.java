package test;

import unsw.loopmania.*;
import unsw.loopmania.Character;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.junit.jupiter.api.Test;


import javafx.beans.property.SimpleIntegerProperty;

public class testArmour {

    @Test
    public void testAddArmour() {
        // create new world, add armour object, and check if exists
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, new ArrayList<>());
        Pair<Integer, Integer> firstAvailableSlot = newWorld.ensureUnequippedSlot();

        Armour armour = new Armour(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
            new SimpleIntegerProperty(firstAvailableSlot.getValue1()));

        newWorld.addUnequippedArmour();
        assertTrue(newWorld.getUnequippedInventoryItems() == armour);
        
    }
    @Test
    public void testDestroyArmour() {
        // create new world, add armour object, and destroy it
        // then check if it still exists
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, new ArrayList<>());
        Pair<Integer, Integer> firstAvailableSlot = newWorld.ensureUnequippedSlot();

        Armour armour = new Armour(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
            new SimpleIntegerProperty(firstAvailableSlot.getValue1()));
        armour.destroy();
        assertTrue(armour.shouldExist().get() == false);
    }

    @Test
    public void testArmourProtection() {
        // create new world, path, and character
        // test damage from zombie to character with and without armour
        // damage taken with armour should be 50% less than damage taken without armour
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0, 0));
        orderedPath.add(new Pair<Integer, Integer>(0, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 0));

        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, orderedPath);

        Character character = new Character(new PathPosition(0, orderedPath));
        newWorld.setCharacter(character);

        Pair<Integer, Integer> firstAvailableSlot = newWorld.ensureUnequippedSlot();
        Armour armour = new Armour(new SimpleIntegerProperty(firstAvailableSlot.getValue0()),
            new SimpleIntegerProperty(firstAvailableSlot.getValue1()));

        Zombie zombie = new Zombie(new PathPosition(0, orderedPath), 0);
        newWorld.addEnemy(zombie);

        // check health with no armour
        double currentHealth = character.getHealth();
        zombie.attack(character);
        double healthAfterAttack = character.getHealth();
        double totalDamage = currentHealth - healthAfterAttack;

        // check health with armour
        Character character2 = new Character(new PathPosition(0, orderedPath));
        newWorld.setCharacter(character);


        Zombie zombie2 = new Zombie(new PathPosition(0, orderedPath), 0);
        newWorld.addEnemy(zombie2);
                                   
        newWorld.addUnequippedArmour();
        character.equip(armour);

        double currentHealth2 = character2.getHealth();
        zombie.attack(character);
        double healthAfterAttack2 = character2.getHealth();
        double totalDamage2 = currentHealth2 - healthAfterAttack2;

        assertTrue(totalDamage2 == totalDamage / 2);
    }
}
