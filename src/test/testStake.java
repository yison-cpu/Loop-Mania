package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javafx.util.Pair;
import unsw.loopmania.LoopManiaWorld;

public class testStake {

    private LoopManiaWorld newWorld;
    private Zombie zombie;
    private Character character;
    private Stake stake;

    // set up for each test
    @BeforeEach
    public void setUp() throws Exception {
        LoopManiaWorld newWorld = makePathAndWorld();
        Character character = new Character(0, 0, 200, 1); // stats don't matter here
        character.setPosition(new Pair<Integer, Integer> (0,1));

        Stake stake = new Stake(20, 150); // dmg, gold
    }

    // clean up after each test
    @AfterEach
    public void cleanUp() throws Exception {
        newWorld.delete();
        character.delete();
        zombie.delete();
        stake.delete();
    }

    // test stake object is created properly and getters/setters work as expected
    @Test
    public void testNewStake() {
        assertEquals(20, stake.getDamage(), "stake should do base 20 damage");
        assertEquals(150, stake.getPrice(), "stake should be bought for 150 gold");
    }

    // test can equip stake and move between inventories
    @Test
    public void testEquipUnequipStake() {
        stake.equip();
        int equipped = 0;
        for (InventoryItem i : character.getEquippedInventory()) {
            if (stake.getId() == i.getId()) {
                equipped = 1;
            }
        }
        assertEquals(1, equipped, "stake should be equipped");

        stake.unequip();
        equipped = 0;
        for (InventoryItem i : character.getEquippedInventory()) {
            if (stake.getId() == i.getId()) {
                equipped = 1;
            }
        }
        assertEquals(0, equipped, "stake should be unequipped");
    }

    // test stake deteriorates as expected
    @Test
    public void testDeteriorateStake() {
        character.attack();
        assertEquals(99, stake.getDeterioration(), "stake should deteriorate by 1 point");
    }

    // integrate above tests and test usage of stake w vampires
    @Test
    public void testStakeVampireBattle() {
        Vampire vampire = new Vampire(20, 3, 4, 150, 2);
        vampire.setPosition(new Pair<Integer, Integer> (0,1));

        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, vampire.getInBattle(), "vampire should be in battle");

        character.attack();
        assertEquals(90, vampire.getHealth(), "vampire should lose 20*3 = 60 health"); // should do triple damage to vampire
        assertEquals(99, stake.getDeterioration(), "stake should deteriorate by 1 point");
        character.attack();
        assertEquals(30, vampire.getHealth(), "vampire should lose 20*3 = 60 health");
        assertEquals(98, stake.getDeterioration(), "stake should deteriorate by 1 point");
    }

    // integrate above tests and test usage of stake w/o vampires
    @Test
    public void testStakeNonVampireBattle() {
        Zombie zombie = new Zombie(10, 2, 3, 100, 0.5); // dmg, battleRadius, supportRadius, health, speed
        zombie.setPosition(new Pair<Integer, Integer> (0,1));

        assertEquals(true, character.getInBattle(), "character should be in battle");
        assertEquals(true, zombie.getInBattle(), "zombie should be in battle");

        character.attack();
        assertEquals(80, zombie.getHealth(), "zombie should lose 20 health"); 
        assertEquals(99, stake.getDeterioration(), "stake should deteriorate by 1 point");
        character.attack();
        assertEquals(60, zombie.getHealth(), "zombie should lose 20 health");
        assertEquals(98, stake.getDeterioration(), "stake should deteriorate by 1 point");
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