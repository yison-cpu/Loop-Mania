package unsw.loopmania;

import java.util.List;
import java.util.Random;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

/* 
 * Represent ZombiePitCard in backend of Loop Mania
 * @parm x
 * @parm y
 */

public class ZombiePitCard extends Card {
    // Basic implement fpr ZombiePitCard;
    private DragStrategy dragStrategy;

    public ZombiePitCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.dragStrategy = new AdjacentTileDragStrategy();
    }

    @Override
    boolean isValidDraggingPosition(List<Pair<Integer, Integer>> orderedPath, int buildingNodeX, int buildingNodeY) {

        // TODO Auto-generated method stub
        return this.dragStrategy.isValidDraggingPosition(orderedPath, buildingNodeX, buildingNodeY);
    }
}

// More code is needed
