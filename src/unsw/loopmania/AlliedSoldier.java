package unsw.loopmania;

public class AlliedSoldier extends MovingEntity {
    private int health;
    private int speed;

    
    public AlliedSoldier(PathPosition position) {
        super(position);
        // values are placeholders, subject to change
        health = 100;
        speed = 10;
    }


    public int getHealth() {
        return health;
    }


    public void setHealth(int health) {
        this.health = health;
    }


    public int getSpeed() {
        return speed;
    }


    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // need to implement more methods. 
    
}
