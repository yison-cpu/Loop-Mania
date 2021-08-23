package unsw.loopmania;

import java.util.Random;

public class Slug extends BasicEnemy {

    private int id;
    static final int BATTLE_RADIUS = 3;
    static final int SUPPORT_RADIUS = 1;
    static final int HEALTH = 25;
    static final int SPEED = 1;
    static final int DAMAGE = 5;
    private int experience;
    private boolean inBattle;
    private boolean tranced;
    private boolean isBoss;

    /**
     * constructs a slug object
     * 
     * @param position
     * @param id
     */
    public Slug(PathPosition position, int id) {
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
     * function to randomly drop items after defeating slug
     * 
     * @return
     */
    public void drop(Character character) {
        character.setGold(character.getGold() + 10);
        character.setExperience(character.getExperience() + 5);
    }

    /**
     * decide drop item or not when defeat
     * 
     * @return true if drop else false
     */
    public boolean dropItem() {
        // one in five
        return (new Random().nextInt(6) == 0);
    }

    /**
     * decide drop card or not when defeat
     * 
     * @return true if drop else false
     */
    public boolean dropCard() {
        // one in seven
        return (new Random().nextInt(8) == 0);
    }

    public void attack(Character character) {

        character.takeDamage(DAMAGE);

    }

}