package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javafx.util.Pair;
import jdk.jfr.Timestamp;
import unsw.loopmania.LoopManiaWorld;

public class testCharacter {

    private LoopManiaWorld newWorld;
    private Character character;

    // set up for each test
    @BeforeEach
    public void setUp() throws Exception {
        LoopManiaWorld newWorld = makePathAndWorld();
        Sword sword = new Sword(30, 100);
        sword.setPosition(new Pair<Integer, Integer> (0,1));
        Character character = new Character(0, 0, 200, 1); // stats don't matter here
        character.setPosition(new Pair<Integer, Integer> (0,1));        
    }

    // clean up after each test
    @AfterEach
    public void cleanUp() throws Exception {
        newWorld.delete();
        character.delete();
    }

    // test new character creation and spawn
    @Test
    public void testCharacterSpawn() {
        Character character = new Character(0, 100, 200, 1, false); // exp, gold, health, speed, inBattle
        assertEquals(0, character.getExperience(), "Character should have 0 experience");
        assertEquals(100, character.getGold(), "Character should have 100 gold");
        assertEquals(200, character.getHealth(), "Character should have 200 gold");
        assertEquals(1, character.getSpeed(), "Character should move at 1 tile per second");
        assertEquals(false, character.getInBattle(), "Character should not be in battle");

    }

    // test picking up items and gold
    @Test
    public void testCharacterPickup() {
        int pickedUp = 0;
        for (InventoryItem i : character.getInventory()) {
            if (sword.getId() == i.getId()) {
                pickedUp = 1;
            }
        }

        assertEquals(1, pickedUp, "sword should have been picked up by character");
    }

    // test health
    @Test
    public void testCharacterBattle() {
        Zombie zombie = new zombie(10, 2, 3, 100, 0.5); // dmg, battleRadius, supportRadius, health, speed
        zombie.setPosition(new Pair<Integer, Integer> (0,1));
        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, zombie.getInBattle(), "zombie should be in battle");

        zombie.attack();
        assertEquals(190, character.getHealth(), "character should have lost 10 health");
        character.attack();
        assertEquals(70, zombie.getHealth(), "zombie should lose 30 health");
    }
    


    // test enterCastle
    @Test
    public void testCharacterEnterCastle() {
        character.setPosition(new Pair<Integer, Integer> (0,0));
        
        assertEquals(true, character.getInCastle(), "Character should be in hero's castle");

    }

    // test speed
    @Test
    public void testCharacterSpeed() {
        character.setPosition(new Pair<Integer, Integer> (0,1));
        assertEquals(1, character.getSpeed(), "character should move at 1 tile per second");
        newWorld.unpause(2); //unpause for 1 second, if -1 argument then remain unpaused this will be what the human player uses
        assertEquals(1, character.getX(), "character should have moved to the next tile (1,1)");
        assertEquals(1, character.getY(), "character should have moved to the next tile (1,1)");
    }

    public LoopManiaWorld makePathAndWorld() {
        List<Pair<Integer, Integer>> orderedPath = new ArrayList<>();
        /* 0   1   2  3   4
        |   |   |   |   |   |  0
        |   |   |   |   |   |  1
        |   |   |   |   |   |  2
        |   |   |   |   |   |  3
        |   |   |   |   |   |  4
        */
        orderedPath.add(new Pair<Integer, Integer>(0, 0));
        orderedPath.add(new Pair<Integer, Integer>(0, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 0));
        orderedPath.add(new Pair<Integer, Integer>(2, 0));
        orderedPath.add(new Pair<Integer, Integer>(0, 2));
        orderedPath.add(new Pair<Integer, Integer>(2, 2));
        orderedPath.add(new Pair<Integer, Integer>(2, 1));
        orderedPath.add(new Pair<Integer, Integer>(1, 2));
        orderedPath.add(new Pair<Integer, Integer>(3, 0));
        orderedPath.add(new Pair<Integer, Integer>(3, 1));
        orderedPath.add(new Pair<Integer, Integer>(3, 2));
        orderedPath.add(new Pair<Integer, Integer>(3, 3));
        orderedPath.add(new Pair<Integer, Integer>(0, 3));
        orderedPath.add(new Pair<Integer, Integer>(1, 3));
        orderedPath.add(new Pair<Integer, Integer>(2, 3));
        orderedPath.add(new Pair<Integer, Integer>(0, 4));
        orderedPath.add(new Pair<Integer, Integer>(1, 4));
        orderedPath.add(new Pair<Integer, Integer>(2, 4));
        orderedPath.add(new Pair<Integer, Integer>(3, 4));
        orderedPath.add(new Pair<Integer, Integer>(4, 4));
        orderedPath.add(new Pair<Integer, Integer>(4, 0));
        orderedPath.add(new Pair<Integer, Integer>(4, 1));
        orderedPath.add(new Pair<Integer, Integer>(4, 2));
        orderedPath.add(new Pair<Integer, Integer>(4, 3));
        
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, orderedPath);
        HerosCastle herosCastle = new HerosCastle(0,0);
        return newWorld;
    }
}