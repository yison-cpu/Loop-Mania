package unsw.loopmania;

public interface Goal {

    /**
     * displays the goal object
     */
    public void displayGoal();

    /**
     * returns the string representation of the goal object
     */
    public String getString();

    /**
     * determine if the character's goal is completed
     * 
     * @param character the character of the LoopMania Game
     */
    public boolean completeGoal(Character character);
}
