package unsw.loopmania;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayNameGenerator.Simple;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * represents the main character in the backend of the game world
 */
public class Character extends MovingEntity implements Subject {

    static final int DAMAGE = 5;
    static final double MAX_HEALTH = 1000.0;
    private SimpleIntegerProperty cycle;
    private SimpleIntegerProperty experience;
    private SimpleIntegerProperty gold;
    private SimpleDoubleProperty health;
    private SimpleIntegerProperty baseDamage;
    private int speed;
    private int steps;
    private int damageMultiplier = 1;
    private int scalarDecrease = 0;
    private UnequippedInventory unequipped;
    private EquippedInventory equipped;
    private boolean inBattle;
    private SimpleBooleanProperty inCastle;
    private LoopManiaWorld world;
    private ArrayList<Observer> observers = new ArrayList<Observer>();

    // TODO = potentially implement relationships between this class and other
    // classes
    public Character(PathPosition position) {
        super(position);
        // starts with nothing
        this.cycle = new SimpleIntegerProperty(0);
        this.experience = new SimpleIntegerProperty(0);
        this.gold = new SimpleIntegerProperty(0);
        this.health = new SimpleDoubleProperty(MAX_HEALTH);
        this.baseDamage = new SimpleIntegerProperty(damageMultiplier * DAMAGE);
        this.inCastle = new SimpleBooleanProperty(false);
        this.steps = 0;
        this.speed = 1;
        this.equipped = new EquippedInventory();
        this.unequipped = new UnequippedInventory();
        this.inBattle = false;
    }

    /**
     * 
     * @param i
     * @param g
     * @param e
     */
    public void pickup(Item i, Gold g, Experience e) {
        if (i != null) {
            ArrayList<Item> list = unequipped.getList();
            list.add(0, i); // add to front of list
            if (list.size() > 10) {
                list.remove(list.size() - 1);
                setGold(getGold() + 50);
                setExperience(getExperience() + 50);
            }
        }
        setGold(getGold() + g.get());
        setExperience(getExperience() + e.get());
    }

    /**
     * move clockwise through the path
     */
    public void moveDownPath() {
        super.moveDownPath();
        if (steps % world.getOrderedPath().size() == 0) {
            setCycle(getCycle() + 1);
        }
        steps++;
    }

    /*
     * public void equip(Item item) { equippedInventoryItems.equip(item); }
     */
    public String equip(Item i) {
        String error = "Item equipped";
        if (i instanceof Armour) {
            if (equipped.getArmour() == null) {
                equipped.setArmour((Armour) i);
            } else {
                error = "Armour already equipped";
            }
        } else if (i instanceof Weapon) {
            if (equipped.getWeapon() == null) {
                equipped.setWeapon((Weapon) i);
            } else {
                error = "Weapon already equipped";
            }
        } else if (i instanceof Helmet) {
            if (equipped.getHelmet() == null) {
                equipped.setHelmet((Helmet) i);
            } else {
                error = "Helmet already equipped";
            }
        } else if (i instanceof BasicShield) {
            if (equipped.getShield() == null) {
                equipped.setShield((BasicShield) i);
            } else {
                error = "Shield already equipped";
            }
        }
        return error;
    }

    /**
     * assumes inventory item has already been checked to exist in equipped
     * inventory then removes the item from equipped inventory
     * 
     * @param i
     */
    public void unequip(Item i) {
        if (i instanceof Armour) {
            equipped.removeArmour();
        } else if (i instanceof Weapon) {
            equipped.removeWeapon();
        } else if (i instanceof Helmet) {
            equipped.removeHelmet();
        } else if (i instanceof Shield) {
            equipped.removeShield();
        }
    }

    /**
     * attacks an enemy depending on equipped items
     * 
     * @param e
     */
    public void attack(BasicEnemy e) {

        // check type of weapon - use function within each weapon class
        Weapon w = equipped.getWeapon();
        Helmet h = equipped.getHelmet();

        if (h != null) {
            scalarDecrease = 5;
        }
        if (w != null) {
            w.attack(e, damageMultiplier, scalarDecrease);
        } else {
            // no weapon equipped - 5 damage * multiplier
            e.setHealth(e.getHealth() - DAMAGE * damageMultiplier + scalarDecrease);
        }
    }

    /**
     * take the damage done by an enemy
     * 
     * @param e
     */
    public void takeDamage(int damage) {

        Armour a = equipped.getArmour();
        double multiplier = 1;
        if (a != null) {
            multiplier = 0.5;
            a.deteriorate();
        }

        BasicShield s = equipped.getShield();
        int scalarDecrease = 0;
        double shieldMultiplier = 1;

        // if there is a shield equipped
        if (s != null) {
            // if it is a normal shield
            if (s instanceof Shield) {
                scalarDecrease = 5;
                s.deteriorate();
            } else {
                // tree stump
                shieldMultiplier = 0.75;
                s.deteriorate();
            }
        }
        setHealth(this.getHealth() - (damage * multiplier * shieldMultiplier) + scalarDecrease);
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
        for (Observer o : observers) {
            if (o instanceof VampireCastleBuilding) {
                if (getCycle() % 5 == 0 && isInCastle()) {
                    o.update(this);
                }
            } else if (o instanceof ZombiePitBuilding) {
                if (isInCastle()) {
                    o.update(this);
                }
            } else if (o instanceof TowerBuilding) {
                TowerBuilding tower = (TowerBuilding) o;
                if (MathHelper.inRadius(this.getX(), this.getY(), tower.getX(), tower.getY(),
                        TowerBuilding.TOWER_RADIUS) && inBattle) {
                    o.update(this);
                }
            } else if (o instanceof VillageBuilding) {
                VillageBuilding village = (VillageBuilding) o;
                if (MathHelper.inPosition(this.getX(), this.getY(), village.getX(), village.getY())) {
                    if (getCycle() != village.getVillageCycle()) {
                        o.update(this);
                    }
                }
            } else if (o instanceof BarracksBuilding) {
                BarracksBuilding village = (BarracksBuilding) o;
                if (MathHelper.inPosition(this.getX(), this.getY(), village.getX(), village.getY())) {
                    o.update(this);
                }
            } else if (o instanceof CampfireBuilding) {
                CampfireBuilding campfire = (CampfireBuilding) o;
                if (MathHelper.inRadius(this.getX(), this.getY(), campfire.getX(), campfire.getY(),
                        CampfireBuilding.CAMPFIRE_RADIUS)) {
                    o.update(this);
                }
            } else if (o instanceof HeroCastle) {
                HeroCastle heroCastle = (HeroCastle) o;
                // if the character inCastle, always set to false
                if (isInCastle()) {
                    setInCastle(false);
                }
                // if coordinate is castle and not inCastle
                if (MathHelper.inPosition(this.getX(), this.getY(), heroCastle.getX(), heroCastle.getY())) {
                    // if the character cycle and herocastle castle doesn't match, increment
                    // herocastle cycle and set character in castle
                    if (getCycle() != heroCastle.getHeroCastleCycle()) {
                        o.update(this);
                    }
                }
            }
        }

    }

    public EquippedInventory getEquippedInventory() {
        return this.equipped;
    }

    public int getCycle() {
        return this.cycle.getValue();
    }

    public SimpleIntegerProperty cycleProperty() {
        return this.cycle;
    }

    public void setCycle(int cycle) {
        this.cycle.set(cycle);
    }

    public int getExperience() {
        return this.experience.getValue();
    }

    public SimpleIntegerProperty experienceProperty() {
        return this.experience;
    }

    public void setExperience(int experience) {
        this.experience.set(experience);
    }

    public int getGold() {
        return this.gold.getValue();
    }

    public SimpleIntegerProperty goldProperty() {
        return this.gold;
    }

    public void setGold(int gold) {
        this.gold.set(gold);
    }

    public double getHealth() {
        return this.health.getValue();
    }

    public SimpleDoubleProperty healthProperty() {
        return this.health;
    }

    /**
     * pre: health can be any number including negative number
     * 
     * post: this.health = health
     * 
     * inv: health <= 100
     * 
     * @param health
     */
    public void setHealth(double health) {
        if (health <= MAX_HEALTH) {
            this.health.set(health);
        } else {
            this.health.set(MAX_HEALTH);
        }
    }

    public int getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(int damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
        baseDamage.set(damageMultiplier * DAMAGE);
    }

    public int getscalarDecrease() {
        return scalarDecrease;
    }

    public void setScalarDecrease(int scalarDecrease) {
        this.scalarDecrease = scalarDecrease;
    }

    public int getSpeed() {
        return this.speed;
    }

    public boolean isInBattle() {
        return this.inBattle;
    }

    public void setInBattle(boolean inBattle) {
        this.inBattle = inBattle;
    }

    public boolean isInCastle() {
        return this.inCastle.get();
    }

    public void setInCastle(boolean inCastle) {
        this.inCastle.set(inCastle);
    }

    public SimpleBooleanProperty inCastleProperty() {
        return this.inCastle;
    }

    public LoopManiaWorld getWorld() {
        return this.world;
    }

    public void setWorld(LoopManiaWorld world) {
        this.world = world;
    }

    public SimpleIntegerProperty damageProperty() {
        return baseDamage;
    }
}
