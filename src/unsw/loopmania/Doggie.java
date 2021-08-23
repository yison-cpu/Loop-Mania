package unsw.loopmania;

import java.util.Random;

public class Doggie extends BasicEnemy {

    private int id;
    static final int BATTLE_RADIUS = 4;
    static final int SUPPORT_RADIUS = 1;
    static final int HEALTH = 150;
    static final int SPEED = 1;
    static final int DAMAGE = 5;
    static final double STUN_CHANCE = 0.2;
    static final int STUN_TIME_IN_MILLIS = 3000;
    private int experience;
    private boolean inBattle;
    private boolean tranced;
    private boolean isBoss;

    public Doggie(PathPosition position, int id) {
        super(position, id);
        this.inBattle = false;
        this.tranced = false;
        this.id = id;
        this.isBoss = true;
        super.setBattleRadius(BATTLE_RADIUS);
        super.setSupportRadius(SUPPORT_RADIUS);
        super.setHealth(HEALTH);
        super.setDamage(DAMAGE);
        super.setSpeed(SPEED);
    }

    /**
     * decide to drop and item when defeated
     * 
     * @return true to drop item
     */
    public boolean dropItem() {
        // always true - should always drop a doggiecoin
        return true;
    }

    /**
     * decide to drop a card when defeated
     * 
     * @return false
     */
    public boolean dropCard() {
        // doesn't drop card - 100 gold, 100 experience and a doggie coin.
        return false;
    }

    public void attack(Character character) {
        // TODO: have to check if doggie still attacks player when player is stunned
        // if stunChance is true, stun and attack character, else, normal attack
        if (stunChance()) {
            character.takeDamage(DAMAGE);
            stun(character);
            character.takeDamage(DAMAGE);
        } else {
            character.takeDamage(DAMAGE);
        }
    }

    /**
     * function to generate random number to see if doggie will perform stun attack
     * 
     * @return true = stun attack, false = normal attack
     */
    public boolean stunChance() {
        Random random = new Random();
        double res = random.nextDouble();
        if (res < STUN_CHANCE) {
            return true;
        }
        return false;
    }

    /**
     * function to stun character
     * 
     * @param character
     */
    public void stun(Character character) {
        int currDamageMultiplier = character.getDamageMultiplier();
        int currScalarDecrease = character.getscalarDecrease();
        // set stun time
        long finishTime = System.currentTimeMillis() + STUN_TIME_IN_MILLIS;
        // while stunned, player does no damage
        while (System.currentTimeMillis() <= finishTime) {
            character.setDamageMultiplier(0);
            character.setScalarDecrease(0);
        }
        // revert character back to prev damage values
        character.setDamageMultiplier(currDamageMultiplier);
        character.setScalarDecrease(currScalarDecrease);
    }

    /**
     * spawn doggiecoin upon defeat and drop gold and xp
     * 
     * @param character
     */
    public void drop(Character character) {
        // TODO: add doggiecoin object into characters inventory
        // Doggiecoin doggiecoin = new Doggiecoin();
        // character.addInventoryItem(doggiecoin);
        character.setGold(character.getGold() + 10);
        character.setExperience(character.getExperience() + 5);
    }
}
