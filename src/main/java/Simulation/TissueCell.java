package Simulation;


import Util.Pair;

/**
 * A tissue cell. It wants to grow, but not as much as cancer. Has a chance to turn a dead
 * cell into a live one every time step
 */

import java.util.ArrayList;
import java.util.Random;

public class TissueCell extends Cell{

    public TissueCell(Pair coords){
        super(0,coords.getX(),coords.getY(),1);
    }

    @Override
    public void interactNeighbors(ArrayList<Cell> neighbors) {
        Random random = new Random();
        ArrayList<Cell> deadCells = new ArrayList<>(); // Keep track of nearby dead cells

        for (Cell cell : neighbors) {
            // Check if a cell is adjacent and a dead cell (which has id 0)
            if(checkAdjacent(cell) && cell.getId() == 0) {
                deadCells.add(cell); // Add this adjacent cell to the list of nearby dead cells
            }
        }

        if(random.nextDouble() < 0.7 && !deadCells.isEmpty()){
            Cell target = deadCells.get(random.nextInt(deadCells.size()));

            // Replace the dead cell with a tissue cell
            Pair tissueCellCoords = new Pair(target.getX(), target.getY());
            neighbors.remove(target);
            neighbors.add(new TissueCell(tissueCellCoords));
        }
    }

    private boolean checkAdjacent(Cell cell) {
        int dx = Math.abs(this.getX() - cell.getX());
        int dy = Math.abs(this.getY() - cell.getY());
        //Check if the cell is adjacent and not at position of the tissue cell
        return (dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0));
    }
}
