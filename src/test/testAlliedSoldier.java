package test;

import unsw.loopmania.*;
import unsw.loopmania.Character;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.IntegerProperty;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Pair;

public class testAlliedSoldier {
    private LoopManiaWorld newWorld;
    private Character character;
    private AlliedSoldier alliedSoldier;
    // private barracks//

    @BeforeEach
    public void setup() throws Exception {
        // create new world and spawn AlliedSoldier
        LoopManiaWorld newWorld = new LoopManiaWorld(10, 10, new ArrayList<>());
        // create new character and new Campfirecard
        Character character = new Character(new PathPosition(0, orderedPath));
        newWorld.setCharacter(character);
        // Need to create a barracks to spawn alliedSoldier;
        BarracksCard barCard = new BarracksCard(new SimpleIntegerProperty(newWorld.getCardEntities.size()),
                new SimpleIntegerProperty(0));
        newWorld.loadBarracksCard();
    }

    @Test
    // Test on the AlliedSoldier spawn;
    public void testAlliedSoldierSpawn() {
        int numOfAlliedSoldierBefore = alliedSoldier.getNumber();
        character.getPosition();
        character.setPosition(new Pair<Integer, Integer>(0, 1));
        Building barracks = newWorld.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        int numOfAlliedSoldierAfter = alliedSoldier.getNumber();

        assertTrue(numOfAlliedSoldierAfter == numOfAlliedSoldierBefore + 1);

    }

    @Test
    public void testAlliedSoldierAttack() {
        Zombie zombie = new Zombie(new PathPosition(0, orderedPath), 0);
        Zombie zombie_1 = new Zombie(10, 2, 3, 100, 0.5);
        newWorld.addEnemy(zombie);
        // Attack without allied solider
        int health1 = zombie.getHealth();
        character.setPosition(new Pair<Integer, Integer>(0, 1));
        character.attack();
        int health2 = zombie.getHealth();
        int healthDiff1 = health1 - health2;
        Building barracks = newWorld.convertCardToBuildingByCoordinates(0, 0, 0, 0);

        character.attack();
        alliedSoldier.attack();

        int health3 = zombie.getHealth();
        int healthDiff2 = health2 - health3;

        // Assume allied attack value is half of the character attack number;
        assertTrue(healthDiff2 == 1.5 * healthDiff1);
    }

    // Random chance of critical bite can change an allied solider into zombie;
    // Test on allied soilder into a zombine;

    @Test
    public void test_AlliedSoldier_change_zombie() {
        Zombie zombie = new Zombie(new PathPosition(0, orderedPath), 0);
        Zombie zombie_1 = new Zombie(10, 2, 3, 100, 0.5);

        newWorld.addEnemy(zombie);
        int numOfZombie = zombie.getNumber();

        // character get allied solider;
        character.getPosition();
        character.setPosition(new Pair<Integer, Integer>(0, 1));

        Building barracks = newWorld.convertCardToBuildingByCoordinates(0, 0, 0, 0);
        int numOfAlliedSoldier = alliedSoldier.getNumber();
        zombie.attack(character);
        int numOfAlliedSoldierAfter = alliedSoldier.getNumber();
        int numOfZombieAfter = zombie.getNumber();

        // check for number increase in zombie and decrease in allied solider;
        assertTrue(numOfZombieAfter - numOfZombie == 1);
        assertTrue(numOfAlliedSoldier - numOfAlliedSoldierAfter == 1);
    }

    @AfterEach
    public void cleanUp() throws Exception {
        newWorld.delete();
        character.delete();
        alliedSoldier.delete();
    }

}