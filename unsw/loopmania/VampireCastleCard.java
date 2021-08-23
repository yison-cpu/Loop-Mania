package unsw.loopmania;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;

import org.javatuples.Pair;

/**
 * represents a vampire castle card in the backend game world
 */
public class VampireCastleCard extends Card {
    private DragStrategy dragStrategy;

    public VampireCastleCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.dragStrategy = new AdjacentTileDragStrategy();
    }

    /**
     * check if dragging position is adjacent to path tile
     * 
     * @param orderedPath   list of x,y coordinate pairs in the order by which
     *                      moving entities traverse them
     * @param buildingNodeX x position of the dragging position
     * @param buildingNodeY y position of the dragging position
     * @return true if dragging position is adjacent to path tile else false
     */
    public boolean isValidDraggingPosition(List<Pair<Integer, Integer>> orderedPath, int buildingNodeX,
            int buildingNodeY) {
        return this.dragStrategy.isValidDraggingPosition(orderedPath, buildingNodeX, buildingNodeY);
    }

}
