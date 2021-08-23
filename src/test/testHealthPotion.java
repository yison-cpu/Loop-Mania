package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.javatuples.Pair;


import unsw.loopmania.HealthPotion;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Zombie;
import unsw.loopmania.Character;


public class testHealthPotion {
    private LoopManiaWorld newWorld;
    private Zombie zombie;
    private Character character;
    private HealthPotion healthPotion;

    @BeforeEach
    public void setUp() throws Exception {
        //LoopManiaWorld newWorld = makePathAndWorld();
        //Character character = new Character(new PathPosition(0, newWorld.getOrderedPath()));
        //character.setPosition(new Pair<Integer, Integer> (0,1));

        //Zombie zombie = new Zombie(new PathPosition(0, newWorld.getOrderedPath()), 1);
        //zombie.setPosition(new Pair<Integer, Integer> (0,1));

        //newWorld.addUnequippedHealthPotion();
    }

    @AfterEach
    public void cleanUp() throws Exception {
        //newWorld.delete();
        //character.delete();
        //zombie.delete();
        //healthPotion.delete(); // not sure if this should be included if healthPotion goes away after being used
    }

    @Test
    // check if health potion is removed once used 
    public void testHealthPotionRemoval() {
        newWorld = makePathAndWorld();
        newWorld.addUnequippedHealthPotion();
        character = new Character(new PathPosition(0, newWorld.getOrderedPath()));
        zombie = new Zombie(new PathPosition(0, newWorld.getOrderedPath()), 1);

        int potionExists = 0;
        character.equip(healthPotion);
        zombie.attack(character);
        healthPotion.use(character);
        healthPotion.discardItem(newWorld);
        assertEquals(0, potionExists);
    }

    @Test
    // check if health potion heals player health to 100
    public void testHealthPotionFunctionality() {
        newWorld = makePathAndWorld();
        newWorld.addUnequippedHealthPotion();
        character = new Character(new PathPosition(0, newWorld.getOrderedPath()));
        zombie = new Zombie(new PathPosition(0, newWorld.getOrderedPath()), 1);
        
        double currentHealth = character.getHealth();
        zombie.attack(character);
        double newHealth = character.getHealth();
        character.equip(healthPotion);
        healthPotion.use(character);
        double healthAfterPotion = character.getHealth();

        assertTrue(newHealth < currentHealth);
        assertEquals(healthAfterPotion, 100.00, 1);
    }

    @Test
    // test if healthPotion object is created properly
    public void testNewHealthPotion () {
        newWorld = makePathAndWorld();
        newWorld.addUnequippedHealthPotion();
        assertEquals(100, healthPotion.getPrice());
    }

    public LoopManiaWorld makePathAndWorld() {

        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        orderedPath.add(new Pair<Integer, Integer>(0, 0));
        orderedPath.add(new Pair<Integer, Integer>(0, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 0));
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, orderedPath);
        return newWorld;
    }
}
