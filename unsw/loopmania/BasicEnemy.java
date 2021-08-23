package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * a basic form of enemy in the world
 */
public abstract class BasicEnemy extends MovingEntity implements Subject {

    private int id;
    private int battleRadius;
    private int supportRadius;
    private int health;
    private int speed;
    private boolean inBattle;
    private boolean tranced;
    private int damage;
    private int experience;
    private boolean isBoss;
    private LoopManiaWorld world;
    private ArrayList<Observer> observers = new ArrayList<Observer>();

    /**
     * creates a new basic enemy - but will never really use this so abstract
     * 
     * @param position
     * @param id
     */
    public BasicEnemy(PathPosition position, int id) {
        super(position);
        this.inBattle = false;
        this.tranced = false;
        this.id = id;
        this.isBoss = false;
    }

    /**
     * move the enemy
     */
    public void move() {
        // enemy behaviour
        // this basic enemy moves in a random direction - either up or down the path
        // battle/support radius movement is covered in loopmaniaworld file
        int directionChoice = (new Random()).nextInt(2);
        if (directionChoice == 0) {
            moveUpPath();
        } else if (directionChoice == 1) {
            moveDownPath();
        }
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);

    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);

    }

    @Override
    public void notifyObservers() {
        TrapBuilding Removetrap = null;
        for (Observer o : observers) {
            if (o instanceof TrapBuilding) {
                TrapBuilding trap = (TrapBuilding) o;
                if (MathHelper.inPosition(this.getX(), this.getY(), trap.getX(), trap.getY())) {
                    Removetrap = trap;
                    o.update(this);
                    break;
                }
            }
        }
        List<BasicEnemy> enemies = world.getEnemies();
        for (BasicEnemy e : enemies) {
            e.removeObserver(Removetrap);
        }
    }

    /**
     * attack method of enemy
     * 
     * @param character the character it is attacking
     */
    public abstract void attack(Character character);

    /**
     * function to randomly drop items after defeating
     * 
     * @return
     */
    public abstract void drop(Character character);

    /**
     * decide drop item or not when defeat
     * 
     * @return true if drop else false
     */
    public abstract boolean dropItem();

    /**
     * decide drop card or not when defeat
     * 
     * @return true if drop else false
     */
    public abstract boolean dropCard();

    /**
     * levels up the enemies attacking power by factor of 2
     */
    public void levelUp() {
        this.setDamage(this.getDamage() * 2);
    }

    /**
     * gets the battle radius of the enemy
     * 
     * @return
     */
    public int getBattleRadius() {
        return this.battleRadius;
    }

    /**
     * sets the battle radius of the enemy
     * 
     * @param health
     */
    public void setBattleRadius(int battleRadius) {
        this.battleRadius = battleRadius;
    }

    /**
     * gets the support radius of the enemy
     * 
     * @return
     */
    public int getSupportRadius() {
        return this.supportRadius;
    }

    /**
     * sets the support radius of the enemy
     * 
     * @param supportRadius
     */
    public void setSupportRadius(int supportRadius) {
        this.supportRadius = supportRadius;
    }

    /**
     * gets the health of the enemy
     * 
     * @return
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * sets the health of the enemy
     * 
     * @param health
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * gets the speed of the enemy
     * 
     * @return
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * sets the speed of the enemy
     * 
     * @param speed
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * gets the id of the enemy
     * 
     * @return
     */
    public int getId() {
        return this.id;
    }

    /**
     * returns whether or not the enemy is currently in battle
     * 
     * @return
     */
    public boolean isInBattle() {
        return this.inBattle;
    }

    /**
     * sets the battle status of the enemy
     * 
     * @param inBattle
     */
    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }

    /**
     * returns whether or not the enemy is tranced
     * 
     * @return
     */
    public boolean isTranced() {
        return this.tranced;
    }

    /**
     * sets the trance status of the enemy
     * 
     * @param tranced
     */
    public void setTranced(boolean tranced) {
        this.tranced = tranced;
    }

    /**
     * returns whether or not the enemy is a boss
     * 
     * @return
     */
    public boolean isBoss() {
        return this.isBoss;
    }

    /**
     * sets the boss status of the enemy
     * 
     * @param isBoss
     */
    public void setIsBoss(boolean isBoss) {
        this.isBoss = isBoss;
    }

    /**
     * set damage
     * 
     * @param damage
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * get damage
     * 
     * @return
     */
    public int getDamage() {
        return this.damage;
    }

    public LoopManiaWorld getWorld() {
        return this.world;
    }

    public void setWorld(LoopManiaWorld world) {
        this.world = world;
    }

}
