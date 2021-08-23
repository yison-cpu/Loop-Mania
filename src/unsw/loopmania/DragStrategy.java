package unsw.loopmania;

import java.util.List;

import org.javatuples.Pair;

interface DragStrategy {

    /**
     * check if dragging position is correspondence to the building's valid position
     * 
     * @precondition 0 <= buildingNodeX < world.width and 0 <= buildingNodeY <
     *               world.height
     * @param orderedPath   list of x,y coordinate pairs in the order by which
     *                      moving entities traverse them
     * @param buildingNodeX x position of the dragging position
     * @param buildingNodeY y position of the dragging position
     * @return true if dragging position is adjacent to path tile else false
     */
    public boolean isValidDraggingPosition(List<Pair<Integer, Integer>> orderedPath, int buildingNodeX,
            int buildingNodeY);

}
