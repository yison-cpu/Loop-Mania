package test;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javafx.util.Pair;
import unsw.loopmania.LoopManiaWorld;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class testStaff {

    private LoopManiaWorld newWorld;
    private Zombie zombie;
    private Character character;
    private Staff staff;

    // set up for each test
    @BeforeEach
    public void setUp() throws Exception {
        LoopManiaWorld newWorld = makePathAndWorld();
        Character character = new Character(0, 0, 200, 1); // stats don't matter here
        character.setPosition(new Pair<Integer, Integer> (0,1));

        Zombie zombie1 = new zombie(10, 2, 3, 100, 0.5); // dmg, battleRadius, supportRadius, health, speed
        zombie.setPosition(new Pair<Integer, Integer> (0,1));
        Zombie zombie2 = new zombie(10, 2, 3, 100, 0.5);
        zombie2.setPosition(new Pair<Integer, Integer> (0,1));

        Staff staff = new staff(10, 100); // dmg, gold
    }

    // clean up after each test
    @AfterEach
    public void cleanUp() throws Exception {
        newWorld.delete();
        character.delete();
        zombie.delete();
        staff.delete();
    }

    // test staff object is created properly and getters/setters work as expected
    @Test
    public void testNewStaff() {
        assertEquals(10, staff.getDamage(), "staff should do 10 damage");
        assertEquals(150, staff.getPrice(), "staff should be bought for 150 gold");
    }

    // test can equip staff and move between inventories
    @Test
    public void testEquipUnequipStaff() {
        staff.equip();
        int equipped = 0;
        for (InventoryItem i : character.getEquippedInventory()) {
            if (staff.getId() == i.getId()) {
                equipped = 1;
            }
        }
        assertEquals(1, equipped, "staff should be equipped");

        staff.unequip();
        equipped = 0;
        for (InventoryItem i : character.getEquippedInventory()) {
            if (staff.getId() == i.getId()) {
                equipped = 1;
            }
        }
        assertEquals(0, equipped, "staff should be unequipped");
    }

    // test staff deteriorates as expected
    @Test
    public void testDeteriorateStaff() {
        character.attack();
        assertEquals(99, staff.getDeterioration(), "staff should deteriorate by 1 point");
    }
    // integrate above tests and test usage of staff where trance occurs allied soldier reverts back to enemy
    @Test
    public void testStaffTranceReverts() {
        character.attack();
        assertEquals(99, staff.getDeterioration(), "staff should deteriorate by 1 point");
        assertEquals(90, zombie.getHealth(), "zombie should lose 10 health");
        character.tranceAttack();
        assertEquals(true, zombie.getTranced(), "zombie should be tranced");
        zombie2.attack();
        zombie.attack();
        character.attack();
        zombie2.attack();
        zombie.attack();
        character.attack();
        zombie2.attack();
        zombie.attack();
        character.attack();
        assertEquals(false, zombie.getTranced(), "zombie should no longer be tranced"); // revert back after 3 attacks
    }

    // integrate above tests and test usage of staff where trance occurs and battle ends whilst enemy is tranced
    @Test
    public void testStaffTranceEnds() {
        character.attack();
        character.attack();
        character.attack();
        character.attack();
        character.attack();
        character.attack();
        character.attack();
        character.attack(); // should have attacked zombie2
        assertEquals(99, staff.getDeterioration(), "staff should deteriorate by 1 point");
        assertEquals(90, zombie.getHealth(), "zombie should lose 10 health");
        character.tranceAttack(zombie);
        assertEquals(true, zombie.getTranced(), "zombie should be tranced");
        zombie2.attack();
        zombie.attack();
        character.attack();
        zombie2.die();
        zombie.die();
        assertEquals(null, zombie2, "zombie2 killed");
        assertEquals(null, zombie, "zombie killed");
        // should maybe have a battle class or interface?:?
    }

    // integrate above tests and test usage of staff w/o vampires
    @Test
    public void testStaffBattle() {
        character.attack();
        assertEquals(99, staff.getDeterioration(), "staff should deteriorate by 1 point");
        assertEquals(90, zombie.getHealth(), "zombie should lose 10 health");
        character.attack();
        assertEquals(98, staff.getDeterioration(), "staff should deteriorate by 1 point");
        assertEquals(80, zombie.getHealth(), "zombie should lose 10 health");
        character.attack();
        assertEquals(97, staff.getDeterioration(), "staff should deteriorate by 1 point");
        assertEquals(70, zombie.getHealth(), "zombie should lose 10 health");
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