package unsw.loopmania;

public class GoalExperience extends GoalLeaf {

    public GoalExperience(String goal, int quantity) {
        super(goal, quantity);
    }

    @Override
    public boolean completeGoal(Character character) {
        return (character.getExperience() >= this.quantity);
    }
}
