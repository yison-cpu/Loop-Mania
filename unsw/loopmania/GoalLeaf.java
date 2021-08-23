package unsw.loopmania;

public abstract class GoalLeaf implements Goal {

    private String goal;
    protected int quantity;

    public GoalLeaf(String goal, int quantity) {
        this.goal = goal;
        this.quantity = quantity;
    }

    @Override
    public void displayGoal() {
        System.out.println(this.getString());
    }

    @Override
    public String getString() {
        return "{\"goal\": \"" + goal + ", \"quantity\": " + quantity + " }";
    }

    @Override
    public boolean completeGoal(Character character) {
        return true;
    }

}
