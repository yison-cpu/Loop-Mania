package test;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;

import unsw.loopmania.LoopManiaWorld;

public class testVampire {

    // set up for each test
    @BeforeEach
    public void setUp() throws Exception {
        LoopManiaWorld newWorld = makePathAndWorld();
        Character character = new Character(0, 0, 200, 1); // stats don't matter here
        character.setPosition(new Pair<Integer, Integer>(0, 1));

        Vampire vampire = new vampire(20, 2, 3, 150, 1); // dmg, battleRadius, supportRadius, health, speed
        vampire.setPosition(new Pair<Integer, Integer>(0, 1));
    }

    // clean up after each test
    @AfterEach
    public void cleanUp() throws Exception {
        newWorld.delete();
        character.delete();
        vampire.delete();
    }

    @Test
    @DisplayName("Testing vampire constructor")
    public void test_new_vampire() {
        assertEquals(20, vampire.getDamage(), "vampire's damage should be 20");
        assertEquals(2, vampire.getBattleRadius(), "vampire's battle radius should be 2");
        assertEquals(3, vampire.getSupportRadius(), "vampire's support radius should be 3");
        assertEquals(150, vampire.getHealth(), "vampire's health should be 150");
        assertEquals(1, vampire.getSpeed(), "vampire's speed should be 1 tile/second");

        newWorld.incrementLoopCounter();

        assertEquals(40, vampire.getDamage(), "vampire's damage should now be 20");
    }

    @Test
    public void test_vampire_battle_support_radius() {
        // in battle
        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, vampire.getInBattle(), "vampire should be in battle");

        // in support radius
        vampire.setPosition(new Pair<Integer, Integer>(3, 3));
        character.setPosition(new Pair<Integer, Integer>(0, 0));
        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, vampire.getInBattle(), "vampire should be in battle");

        // outside of radius
        vampire.setPosition(new Pair<Integer, Integer>(4, 4));
        character.setPosition(new Pair<Integer, Integer>(0, 0));
        assertEquals(false, character.getInBattle(), "character should not be in battle");
        assertEquals(false, vampire.getInBattle(), "vampire should not be in battle");
    }

    // test vampire moves at expected rate
    @Test
    public void test_vampire_speed() {
        vampire.setPosition(new Pair<Integer, Integer>(0, 1));
        assertEquals(1, vampire.getSpeed(), "vampire should move at 1 tile per second");
        newWorld.unpause(2); // unpause for 1 second, if -1 argument then remain unpaused this will be what
                             // the human player uses
        assertEquals(1, vampire.getX(), "vampire should have moved to the next tile (1,1)");
        assertEquals(1, vampire.getY(), "vampire should have moved to the next tile (1,1)");
    }

    // test vampire attacks with correct damage (test after multiple loops
    // preferably to ensure it increases)
    @Test
    public void test_vampire_attack() {
        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, vampire.getInBattle(), "vampire should be in battle");

        vampire.attack();
        assertEquals(190, character.getHealth(), "character should have lost 5 health");
    }

    @Test
    @DisplayName("Testing vampire interaction with campfire")
    public void test_vampire_interact_campfire() {
        LoopManiaWorld newWorld = makePathAndWorld();

        CampfireCard campfireCard = new CampfireCard(new SimpleIntegerProperty(newWorld.getCardEntities().size()),
                new SimpleIntegerProperty(0));
        newWorld.loadCampfireCard();

        Vampire vampire = new Vampire(new PathPosition(0, orderedPath));
        newWorld.addEnemy(vampire);

        CampfireBuilding CampfireBuilding = newWorld.convertCardToBuildingByCoordinates(0, 0, 0, 2);

        assertEquals(true, vampire.isRunning(), "vampire should be running away from the campfire");

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
