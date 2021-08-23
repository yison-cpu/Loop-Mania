package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.javatuples.Pair;
import unsw.loopmania.Doggie;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Character;


public class testDoggie {
    
    /*
    @BeforeEach
    public void setUp() throws Exception {
        LoopManiaWorld newWorld = makePathAndWorld();
        Character character = new Character(new PathPosition(0, newWorld.getOrderedPath()));

        Doggie doggie = new Doggie(new PathPosition(0, newWorld.getOrderedPath()), 1) 
    }
    */

    /* //clean up after each test
    @AfterEach
    public void cleanUp() throws Exception {
        newWorld.delete();
        character.delete();
        doggie.delete();
    }*/

    @Test
    @DisplayName("Testing doggie constructor")
    public void testNewDoggie() {
        LoopManiaWorld newWorld = makePathAndWorld();
        Character character = new Character(new PathPosition(0, newWorld.getOrderedPath()));
        Doggie doggie = new Doggie(new PathPosition(0, newWorld.getOrderedPath()), 1);
        assertEquals(5, doggie.getDamage(), "doggie's damage should be 5");
        assertEquals(4, doggie.getBattleRadius(), "doggie's battle radius should be 4");
        assertEquals(1, doggie.getSupportRadius(), "doggie's support radius should be 1");
        assertEquals(150, doggie.getHealth(), "doggie's health should be 50");
        assertEquals(1, doggie.getSpeed(), "doggie's speed should be 1 tile/second");

        character.setCycle(2);

        assertEquals(10, doggie.getDamage(), "doggie's damage should now be 10");
    }

    @Test
    public void testDoggieBattleSupportRadius() {
        LoopManiaWorld newWorld = makePathAndWorld();
        Character character = new Character(new PathPosition(0, newWorld.getOrderedPath()));
        Doggie doggie = new Doggie(new PathPosition(0, newWorld.getOrderedPath()), 1);
        assertEquals(true, character.isInBattle(), "character should be in battle");
        assertEquals(true, doggie.isInBattle(), "doggie should be in battle");

    }


    @Test
    public void testDoggieAttack() {
        LoopManiaWorld newWorld = makePathAndWorld();
        Character character = new Character(new PathPosition(0, newWorld.getOrderedPath()));
        Doggie doggie = new Doggie(new PathPosition(0, newWorld.getOrderedPath()), 1);
        assertEquals(true, character.isInBattle(), "character should be in battle");
        assertEquals(true, doggie.isInBattle(), "doggie should be in battle");
        doggie.attack(character);
        assertEquals(195, character.getHealth(), "character should have lost 5 health");
    }

    @Test
    public void testDoggieTakeDamage() {
        LoopManiaWorld newWorld = makePathAndWorld();
        Character character = new Character(new PathPosition(0, newWorld.getOrderedPath()));
        Doggie doggie = new Doggie(new PathPosition(0, newWorld.getOrderedPath()), 1);
        assertEquals(true, character.isInBattle(), "character should be in battle");
        assertEquals(true, doggie.isInBattle(), "doggie should be in battle");
        character.attack(doggie);
        assertEquals(145, doggie.getHealth(), "doggie should have lost 5 health");
    }

    @Test
    public void testDoggieStun() {
        // TODO
        // implement test to test when player is stunned
        // also have to test random
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

