package unsw.loopmania;

public class GoalAnd implements Goal {

    Goal goal1;
    Goal goal2;

    public GoalAnd(Goal goal1, Goal goal2) {
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    @Override
    public void displayGoal() {
        System.out.println(this.getString());
    }

    @Override
    public String getString() {
        String requirement = "{ \"goal\": \"AND\", \"subgoals\":\n [ { " + goal1.getString() + ",\n" + goal2.getString()
                + "\n]\n}";
        return requirement;
    }

    @Override
    public boolean completeGoal(Character character) {
        return (goal1.completeGoal(character) && goal2.completeGoal(character));
    }
}
