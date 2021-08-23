package unsw.loopmania;

public class GoalGold extends GoalLeaf {

    public GoalGold(String goal, int quantity) {
        super(goal, quantity);
    }

    @Override
    public boolean completeGoal(Character character) {
        return (character.getGold() >= this.quantity);
    }
}
