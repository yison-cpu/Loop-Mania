package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javafx.util.Pair;
import unsw.loopmania.LoopManiaWorld;

public class testSword {

    private LoopManiaWorld newWorld;
    private Zombie zombie;
    private Character character;
    private Sword sword;

    // set up for each test
    @BeforeEach
    public void setUp() throws Exception {
        LoopManiaWorld newWorld = makePathAndWorld();
        Character character = new Character(0, 0, 200, 1); // stats don't matter here
        character.setPosition(new Pair<Integer, Integer> (0,1));

        Zombie zombie = new Zombie(10, 2, 3, 100, 0.5); // dmg, battleRadius, supportRadius, health, speed
        zombie.setPosition(new Pair<Integer, Integer> (0,1));

        Sword sword = new Sword(30, 100); // dmg, gold
    }

    // clean up after each test
    @AfterEach
    public void cleanUp() throws Exception {
        newWorld.delete();
        character.delete();
        zombie.delete();
        sword.delete();
    }

    // test sword object is created properly and getters/setters work as expected
    @Test
    public void testNewSword() {
        assertEquals(30, sword.getDamage(), "sword should do 30 damage");
        assertEquals(100, sword.getPrice(), "sword should be bought for 100 gold");
    }

    // test can equip sword and move between inventories
    @Test
    public void testEquipUnequipSword() {
        sword.equip();
        int equipped = 0;
        for (InventoryItem i : character.getEquippedInventory()) {
            if (sword.getId() == i.getId()) {
                equipped = 1;
            }
        }
        assertEquals(1, equipped, "sword should be equipped");

        sword.unequip();
        equipped = 0;
        for (InventoryItem i : character.getEquippedInventory()) {
            if (sword.getId() == i.getId()) {
                equipped = 1;
            }
        }
        assertEquals(0, equipped, "sword should be unequipped");
    }

    // test sword deteriorates as expected
    @Test
    public void testDeteriorateSword() {
        character.attack();
        assertEquals(99, sword.getDeterioration(), "sword should deteriorate by 1 point");
    }

    // integrate above tests and test usage of sword in battle
    @Test
    public void testSwordBattle() {
        character.attack();
        assertEquals(99, sword.getDeterioration(), "sword should deteriorate by 1 point");
        assertEquals(70, zombie.getHealth(), "zombie should lose 30 health");
        character.attack();
        assertEquals(98, sword.getDeterioration(), "sword should deteriorate by 1 point");
        assertEquals(40, zombie.getHealth(), "zombie should lose 30 health");
        character.attack();
        assertEquals(97, sword.getDeterioration(), "sword should deteriorate by 1 point");
        assertEquals(10, zombie.getHealth(), "zombie should lose 30 health");
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