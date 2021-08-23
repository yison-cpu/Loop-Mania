package unsw.loopmania;

public class GoalCycle extends GoalLeaf {

    public GoalCycle(String goal, int quantity) {
        super(goal, quantity);
    }

    @Override
    public boolean completeGoal(Character character) {
        return (character.getCycle() >= this.quantity);
    }
}