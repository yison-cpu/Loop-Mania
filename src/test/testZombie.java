package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import javafx.util.Pair;
import jdk.jfr.Timestamp;
import unsw.loopmania.LoopManiaWorld;

public class testZombie {

    private LoopManiaWorld newWorld;
    private Zombie zombie;
    private Character character;

    // set up for each test
    @BeforeEach
    public void setUp() throws Exception {
        LoopManiaWorld newWorld = makePathAndWorld();
        Character character = new Character(0, 0, 200, 1); // stats don't matter here
        character.setPosition(new Pair<Integer, Integer> (0,1));

        Zombie zombie = new zombie(10, 2, 3, 100, 0.5); // dmg, battleRadius, supportRadius, health, speed
        zombie.setPosition(new Pair<Integer, Integer> (0,1));
    }

    // clean up after each test
    @AfterEach
    public void cleanUp() throws Exception {
        newWorld.delete();
        character.delete();
        zombie.delete();
    }

    // test zombie object is created properly and getters/setters work as expected
    @Test
    @DisplayName("Testing zombie constructor")
    public void testNewZombie() {
        assertEquals(10, zombie.getDamage(), "zombie's damage should be 10");
        assertEquals(2, zombie.getBattleRadius(), "zombie's battle radius should be 2");
        assertEquals(3, zombie.getSupportRadius(), "zombie's support radius should be 3");
        assertEquals(100, zombie.getHealth(), "zombie's health should be 100");
        assertEquals(0.5, zombie.getSpeed(), "zombie's speed should be 0.5 tile/second");

        newWorld.incrementLoopCounter();

        assertEquals(20, zombie.getDamage(), "zombie's damage should now be 20");
    }

    // test zombie attacks when character within battle/support radius
    @Test
    public void testZombieBattleSupportRadius() {
        // in battle
        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, zombie.getInBattle(), "zombie should be in battle");

        // in support radius
        zombie.setPosition(new Pair<Integer, Integer> (3,3));
        character.setPosition(new Pair<Integer, Integer> (0,0));
        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, zombie.getInBattle(), "zombie should be in battle");

        // outside of radius
        zombie.setPosition(new Pair<Integer, Integer> (4,4));
        character.setPosition(new Pair<Integer, Integer> (0,0));
        assertEquals(false, character.getInBattle(), "character should not be in battle");
        assertEquals(false, zombie.getInBattle(), "zombie should not be in battle");
    }

    // test zombie moves at expected rate
    @Test
    public void testZombieSpeed() {
        zombie.setPosition(new Pair<Integer, Integer> (0,1));
        assertEquals(1, zombie.getSpeed(), "zombie should move at 1 tile per second");
        newWorld.unpause(2); //unpause for 1 second, if -1 argument then remain unpaused this will be what the human player uses
        assertEquals(1, zombie.getX(), "zombie should have moved to the next tile (1,1)");
        assertEquals(1, zombie.getY(), "zombie should have moved to the next tile (1,1)");
    }

    // test zombie attacks with correct damage (test after multiple loops preferably to ensure it increases)
    @Test
    public void testZombieAttack() {
        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, zombie.getInBattle(), "zombie should be in battle");

        zombie.attack();
        assertEquals(190, character.getHealth(), "character should have lost 5 health");
    }

    // test zombie crit attack turns allied soldier into zombie
    @Test
    public void testZombieCritAttack() {
        AlliedSoldier soldier = new AlliedSoldier();
        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, zombie.getInBattle(), "zombie should be in battle");
        assertEquals(true, soldier.getInBattle(), "allied soldier should be in battle");

        zombie.critAttack();
        assertEquals(true, soldier.getZombified(), "allied soldier should be a zombie"); // need UML relationship between allied soldier and zombie!
    }

    // test consisting of battle radius, support radius, attack, taking damage, dropping loot
    @Test
    public void testZombieBattle() {
        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, zombie.getInBattle(), "zombie should be in battle");

        zombie.attack();
        assertEquals(190, character.getHealth(), "character should have lost 10 health");
        character.attack();
        assertEquals(90, zombie.getHealth(), "zombie should have lost 10 health");
        zombie.attack();
        assertEquals(180, character.getHealth(), "character should have lost 5 health");
        character.attack();
        assertEquals(80, zombie.getHealth(), "zombie should have lost 10 health");
        character.attack();
        assertEquals(70, zombie.getHealth(), "zombie should have lost 10 health");
        character.attack();
        assertEquals(60, zombie.getHealth(), "zombie should have lost 10 health");
        character.attack();
        assertEquals(50, zombie.getHealth(), "zombie should have lost 10 health");
        character.attack();
        assertEquals(40, zombie.getHealth(), "zombie should have lost 10 health");
        character.attack();
        assertEquals(30, zombie.getHealth(), "zombie should have lost 10 health");
        character.attack();
        assertEquals(20, zombie.getHealth(), "zombie should have lost 10 health");
        character.attack();
        assertEquals(10, zombie.getHealth(), "zombie should have lost 10 health");
        character.attack();
        assertEquals(0, zombie.getHealth(), "zombie should have lost 10 health");
        zombie.die();
        assertEquals(null, zombie, "zombie should have been destroyed");
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
        return newWorld;
    }
}