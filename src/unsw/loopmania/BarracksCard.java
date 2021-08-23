package unsw.loopmania;

import java.util.List;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

public class BarracksCard extends Card{
    private DragStrategy dragStrategy;

    public BarracksCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.dragStrategy = new PathTileDragStrategy();
    }

    // this method checks dragging position is non path tile and adjacent to path
    // tile
    public boolean isValidDraggingPosition(List<Pair<Integer, Integer>> orderedPath, int buildingNodeX,
            int buildingNodeY) {

        return this.dragStrategy.isValidDraggingPosition(orderedPath, buildingNodeX, buildingNodeY);
    }
}
