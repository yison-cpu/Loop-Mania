package unsw.loopmania;

import java.util.List;
import java.util.Random;

import org.javatuples.Pair;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * represents a campfire card in the backend game world
 */
public class CampfireCard extends Card {

    private int cardValue = 50;
    private DragStrategy dragStrategy;

    public CampfireCard(SimpleIntegerProperty x, SimpleIntegerProperty y) {
        super(x, y);
        this.dragStrategy = new NonPathTileDragStrategy();
    }

    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }

    /**
     * check if dragging position is any non path tile
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