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

public class testSlug {

    private LoopManiaWorld newWorld;
    private Slug slug;
    private Character character;

    // set up for each test
    @BeforeEach
    public void setUp() throws Exception {
        LoopManiaWorld newWorld = makePathAndWorld();
        Character character = new Character(0, 0, 200, 1); // stats don't matter here
        character.setPosition(new Pair<Integer, Integer> (0,1));

        Slug slug = new Slug(5, 1, 1, 50, 1); // dmg, battleRadius, supportRadius, health, speed
        slug.setPosition(new Pair<Integer, Integer> (0,1));
    }

    // clean up after each test
    @AfterEach
    public void cleanUp() throws Exception {
        newWorld.delete();
        character.delete();
        slug.delete();
    }

    // test slug object is created properly and getters/setters work as expected
    @Test
    @DisplayName("Testing slug constructor")
    public void testNewSlug() {
        assertEquals(5, slug.getDamage(), "slug's damage should be 5");
        assertEquals(1, slug.getBattleRadius(), "slug's battle radius should be 1");
        assertEquals(1, slug.getSupportRadius(), "slug's support radius should be 1");
        assertEquals(50, slug.getHealth(), "slug's health should be 50");
        assertEquals(1, slug.getSpeed(), "slug's speed should be 1 tile/second");

        newWorld.incrementLoopCounter();

        assertEquals(10, slug.getDamage(), "slug's damage should now be 10");
    }

    // test slug attacks when character within battle/support radius
    @Test
    public void testSlugBattleSupportRadius() {
        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, slug.getInBattle(), "slug should be in battle");
    }

    // test slug moves at expected rate
    @Test
    public void testSlugSpeed() {
        newWorld.pause();
        assertEquals(1, slug.getSpeed(), "slug should move at 1 tile per second");
        newWorld.unpause(1); //unpause for 1 second, if -1 argument then remain unpaused this will be what the human player uses
        assertEquals(1, slug.getX(), "slug should have moved to the next tile (1,1)");
        assertEquals(1, slug.getY(), "slug should have moved to the next tile (1,1)");
    }

    // test slug attacks with correct damage (test after multiple loops preferably to ensure it increases)
    @Test
    public void testSlugAttack() {
        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, slug.getInBattle(), "slug should be in battle");

        slug.attack();
        assertEquals(195, character.getHealth(), "character should have lost 5 health");
    }

    // test consisting of battle radius, support radius, attack, taking damage, dropping loot
    @Test
    public void testSlugBattle() {
        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, slug.getInBattle(), "slug should be in battle");

        slug.attack();
        assertEquals(195, character.getHealth(), "character should have lost 5 health");
        character.attack();
        assertEquals(40, slug.getHealth(), "slug should have lost 10 health");
        slug.attack();
        assertEquals(190, character.getHealth(), "character should have lost 5 health");
        character.attack();
        assertEquals(30, slug.getHealth(), "slug should have lost 10 health");
        character.attack();
        assertEquals(20, slug.getHealth(), "slug should have lost 10 health");
        character.attack();
        assertEquals(10, slug.getHealth(), "slug should have lost 10 health");
        character.attack();
        assertEquals(0, slug.getHealth(), "slug should have lost 10 health");
        slug.die();
        assertEquals(null, slug, "slug should have been destroyed");
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