package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import unsw.loopmania.Helmet;
import unsw.loopmania.LoopManiaWorld;
import unsw.loopmania.PathPosition;
import unsw.loopmania.Zombie;
import unsw.loopmania.Character;

public class testHelmet {

    private LoopManiaWorld newWorld;
    private Zombie zombie;
    private Character character;
    private Helmet helmet;

    // set up for each test
    @BeforeEach
    public void setUp() throws Exception {
        LoopManiaWorld newWorld = makePathAndWorld();
        Character character = new Character(new PathPosition(0, newWorld.getOrderedPath()));
        character.setPosition(new Pair<Integer, Integer> (0,1));

        Zombie zombie = new Zombie(new PathPosition(0, newWorld.getOrderedPath()), 1);
        zombie.setPosition(new Pair<Integer, Integer> (0,1));

        Helmet helmet = new Helmet(x, y)
    }

    // clean up after each test
    @AfterEach
    public void cleanUp() throws Exception {
        newWorld.delete();
        character.delete();
        zombie.delete();
        helmet.delete();
    }

    // test helmet constructor
    @Test
    public void testNewHelmet() {
        assertEquals(50, helmet.getHealth(), "Helmet should have 50 health");
        assertEquals(200, helmet.getPrice(), "Helmet should be worth 200 gold");
    }

    // test can equip and unequip helmet and move between inventories
    @Test
    public void testEquipUnequipHelmet() {
        character.equip(helmet);
        int equipped = 0;
        for (InventoryItem i : character.getEquippedInventory()) {
            if (helmet.getId() == i.getId()) {
                equipped = 1;
            }
        }
        assertEquals(1, equipped, "helmet should be equipped");

        helmet.unequip();
        equipped = 0;
        for (InventoryItem i : character.getEquippedInventory()) {
            if (helmet.getId() == i.getId()) {
                equipped = 1;
            }
        }
        assertEquals(0, equipped, "helmet should be unequipped");
    }

    // test helmet deteriorates as expected
    @Test
    public void testDeteriorateHelmet() {
        character.attack();
        assertEquals(99, helmet.getDeterioration(), "helmet should deteriorate by 1 point");
    }

    // test helmet protects character as expected
    @Test
    public void testHelmetBattle() {
        zombie.attack(character);
        assertEquals(40, helmet.getHealth(), "helmet should take damage instead of character");
        assertEquals(200, character.getHealth(), "character should not take damage");
        zombie.attack(character);
        zombie.attack(character);
        zombie.attack(character);
        zombie.attack(character);
        helmet.deteriorate();
        zombie.attack(character);
        assertEquals(190, character.getHealth(), "character now takes damage");
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