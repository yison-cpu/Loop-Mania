package unsw.loopmania;

import java.util.Random;

public class Zombie extends BasicEnemy {

    private int id;
    static final int BATTLE_RADIUS = 3;
    static final int SUPPORT_RADIUS = 5;
    static final int HEALTH = 50;
    static final int SPEED = 1;
    static final int DAMAGE = 10;
    private int experience;
    private boolean inBattle;
    private boolean tranced;
    private boolean isBoss;

    /**
     * constructs a zombie object
     * 
     * @param position
     * @param id
     */
    public Zombie(PathPosition position, int id) {
        super(position, id);
        this.inBattle = false;
        this.tranced = false;
        this.isBoss = false;
        this.id = id;
        super.setBattleRadius(BATTLE_RADIUS);
        super.setSupportRadius(SUPPORT_RADIUS);
        super.setHealth(HEALTH);
        super.setDamage(DAMAGE);
        super.setSpeed(SPEED);
    }

    /**
     * function to randomly drop items after defeating zombie
     * 
     * @return
     */
    public void drop(Character character) {
        character.setGold(character.getGold() + 20);
        character.setExperience(character.getExperience() + 10);
    }

    /**
     * decide drop item or not when defeat
     * 
     * @return true if drop else false
     */
    public boolean dropItem() {
        // one in three
        return (new Random().nextInt(4) == 0);
    }

    /**
     * decide drop card or not when defeat
     * 
     * @return true if drop else false
     */
    public boolean dropCard() {
        // one in five
        return (new Random().nextInt(6) == 0);
    }

    public void attack(Character character) {

        character.takeDamage(DAMAGE);
    }

}