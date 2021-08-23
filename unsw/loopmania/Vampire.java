package unsw.loopmania;

import java.util.Random;

public class Vampire extends BasicEnemy {

    static final int BATTLE_RADIUS = 5;
    static final int SUPPORT_RADIUS = 6;
    static final int HEALTH = 75;
    static final int SPEED = 1;
    static final int DAMAGE = 20;
    private int experience = 0;
    private int id;

    public Vampire(PathPosition position, int id) {
        super(position, id);
        super.setBattleRadius(BATTLE_RADIUS);
        super.setSupportRadius(SUPPORT_RADIUS);
        super.setHealth(HEALTH);
        super.setDamage(DAMAGE);
        super.setSpeed(SPEED);
    }

    /**
     * function to randomly drop items after defeating vampire
     * 
     * @return
     */
    public void drop(Character character) {
        character.setGold(character.getGold() + 40);
        character.setExperience(character.getExperience() + 20);
    }

    /**
     * decide drop item or not when defeat
     * 
     * @return true if drop else false
     */
    public boolean dropItem() {
        // one in one
        return true;
    }

    /**
     * decide drop card or not when defeat
     * 
     * @return true if drop else false
     */
    public boolean dropCard() {
        // one in two
        return (new Random().nextInt(2) == 0);
    }

    /**
     * attack method of a vampire
     * 
     * @param character the character objeact of the loop mania game
     */
    public void attack(Character character) {
        // 1/5 chance of critical bite
        int randomCritical = (new Random()).nextInt(5);
        if (randomCritical <= 3) {
            character.takeDamage(DAMAGE);

        } else if (randomCritical == 4) {
            // getEquipped return a equipped object which has the details of the items that
            // the character had equipped
            if (character.getEquippedInventory().getShield() != null) {
                // when the character have a shield, critical bites have a 60% lower chance of
                // happening
                // if the character avoided a critical attack BECAUSE of the shield, the shield
                // deteriorate,
                // in this case the character doesn't take any damage
                if (character.getEquippedInventory().getShield().isBlockCriticalBite()) {
                    // for random number of vampire attack (at most 3)
                    int randomAttack = (new Random()).nextInt(3);
                    // attack with additional damage of 1
                    for (int i = 0; i <= randomAttack; i++) {
                        character.takeDamage(DAMAGE + 1);
                    }
                } else {
                    character.getEquippedInventory().getShield().deteriorate();
                }
            }
        }
    }

    /**
     * runs away from the campfire when in range
     */
    public void runAway() {

    }

}
