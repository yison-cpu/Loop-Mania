package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javafx.util.Pair;
import unsw.loopmania.LoopManiaWorld;

public class testanduril {

    private LoopManiaWorld newWorld;
    private Zombie zombie;
    private Character character;
    private Anduril anduril;

    // set up for each test
    @BeforeEach
    public void setUp() throws Exception {
        LoopManiaWorld newWorld = makePathAndWorld();
        Character character = new Character(0, 0, 200, 1); // stats don't matter here
        character.setPosition(new Pair<Integer, Integer> (0,1));

        Doggie doggie = new Doggie();
        doggie.setPosition(new Pair<Integer, Integer> (0,1));

        Anduril anduril = new Anduril(30, 100); // dmg, gold
    }

    // clean up after each test
    @AfterEach
    public void cleanUp() throws Exception {
        newWorld.delete();
        character.delete();
        zombie.delete();
        anduril.delete();
    }

    // test anduril object is created properly and getters/setters work as expected
    @Test
    public void testNewAnduril() {
        assertEquals(30, anduril.getDamage(), "anduril should do 30 damage");
        assertEquals(100, anduril.getPrice(), "anduril should be bought for 100 gold");
    }

    // test can equip anduril and move between inventories
    @Test
    public void testEquipUnequipAnduril() {
        anduril.equip();
        int equipped = 0;
        for (InventoryItem i : character.getEquippedInventory()) {
            if (anduril.getId() == i.getId()) {
                equipped = 1;
            }
        }
        assertEquals(1, equipped, "anduril should be equipped");

        anduril.unequip();
        equipped = 0;
        for (InventoryItem i : character.getEquippedInventory()) {
            if (anduril.getId() == i.getId()) {
                equipped = 1;
            }
        }
        assertEquals(0, equipped, "anduril should be unequipped");
    }

    // test anduril deteriorates as expected
    @Test
    public void testDeteriorateAnduril() {
        character.attack();
        assertEquals(99, anduril.getDeterioration(), "anduril should deteriorate by 1 point");
    }

    // integrate above tests and test usage of anduril in battle
    @Test
    public void testAndurilBattle() {
        character.attack();
        assertEquals(99, anduril.getDeterioration(), "anduril should deteriorate by 1 point");
        assertEquals(60, doggie.getHealth(), "doggie should lose 30*3 = 90 health");
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