package unsw.loopmania;

public class EquippedInventory {
    private Armour armour;
    private Weapon weapon;
    private Helmet helmet;
    private BasicShield shield;

    public Armour getArmour() {
        return armour;
    }
    public void setArmour(Armour armour) {
        this.armour = armour;
    }
    public void removeArmour() {
        this.armour = null;
    }
    public boolean isEquippedArmour() {
        return !(armour == null);
    }
    public Weapon getWeapon() {
        return weapon;
    }
    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
    public void removeWeapon() {
        this.weapon = null;
    }
    public boolean isEquippedWeapon() {
        return !(weapon == null);
    }
    public Helmet getHelmet() {
        return helmet;
    }
    public void setHelmet(Helmet helmet) {
        this.helmet = helmet;
    }
    public void removeHelmet() {
        this.helmet = null;
    }
    public boolean isEquippedHelmet() {
        return !(helmet == null);
    }
    public BasicShield getShield() {
        return shield;
    }
    public void setShield(BasicShield shield) {
        this.shield = shield;
    }
    public void removeShield() {
        this.shield = null;
    }
    public boolean isEquippedShield() {
        return !(shield == null);
    }

}
