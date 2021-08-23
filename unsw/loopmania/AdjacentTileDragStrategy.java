package unsw.loopmania;

import java.util.List;

import org.javatuples.Pair;

public class AdjacentTileDragStrategy implements DragStrategy {

    /**
     * check if dragging position is adjacent to path tile
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
            int buildingNodeY) {

        Pair<Integer, Integer> dragLocation = new Pair<>(buildingNodeX, buildingNodeY);
        // check not pathtile
        if (orderedPath.contains(dragLocation)) {
            return false;
        }
        // check adjacent to pathtile
        // check the four neighbours, if is pathtile, add to spawn candidates
        // UP
        Pair<Integer, Integer> pos;
        if (orderedPath.contains(new Pair<Integer, Integer>(buildingNodeX, buildingNodeY + 1))) {
            return true;
            // DOWN
        }
        if (orderedPath.contains(new Pair<Integer, Integer>(buildingNodeX, buildingNodeY - 1))) {
            return true;
            // LEFT
        }
        if (orderedPath.contains(new Pair<Integer, Integer>(buildingNodeX - 1, buildingNodeY))) {
            return true;
            // RIGHT
        }
        if (orderedPath.contains(new Pair<Integer, Integer>(buildingNodeX + 1, buildingNodeY))) {
            return true;
        }
        return false;
    }
}