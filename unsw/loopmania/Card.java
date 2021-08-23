package unsw.loopmania;

import java.util.List;
import java.util.Random;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * a Card in the world which doesn't move
 */
public abstract class Card extends StaticEntity {
    public Card(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
    }

    /**
     * the card is being discarded as being the oldest card due to the character
     * receives a new card or other reasons the character may receive gold,
     * experience and items
     * 
     * @param world the backend world of LoopMania Game
     */
    public void discardCard(LoopManiaWorld world, int price) {
        Character character = world.getCharacter();
        // the character has 1/3 chance of receiving an item
        // 1/2 card value of gold, 0-50 of xp
        int randomReward = (new Random()).nextInt(21);
        character.setGold((int) (character.getGold() + 0.5 * price));
        int randomExperience = (new Random()).nextInt(50);
        character.setExperience(character.getExperience() + (randomExperience / 100));
        if (randomReward == 15) {
            world.addUnequippedSword();
        } else if (randomReward == 16) {
            world.addUnequippedStake();
        } else if (randomReward == 17) {
            world.addUnequippedArmour();
        } else if (randomReward == 18) {
            world.addUnequippedShield();
        } else if (randomReward == 19) {
            world.addUnequippedHelmet();
        } else if (randomReward == 20) {
            world.addUnequippedStaff();
        } else if (randomReward == 21) {
            world.addUnequippedHealthPotion();
        }
    }

    abstract boolean isValidDraggingPosition(List<Pair<Integer, Integer>> orderedPath, int buildingNodeX,
            int buildingNodeY);
}
