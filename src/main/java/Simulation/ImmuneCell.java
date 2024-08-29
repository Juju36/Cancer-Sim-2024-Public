package Simulation;

import Util.Pair;
import java.util.Random;

/**
 * The immune cell! It kills cancer, and has a chance to attack multiple cancer cells per turn!
 */

import java.util.ArrayList;

public class ImmuneCell extends Cell{

    public ImmuneCell(Pair coords) {
        super(3,coords.getX(),coords.getY(),4);
    }

    @Override
    public void interactNeighbors(ArrayList<Cell> neighbors){

        Random random = new Random();

        // Create copies of the neighbors list for safe iteration
        ArrayList<Cell> neighborsCopy = new ArrayList<>(neighbors);

        ArrayList<Cell> cancerCells = new ArrayList<>(); // List to keep track of nearby cancer cells

        // Temporary lists to avoid the ConcurrentModificationException issue
        ArrayList<Cell> cellsToRemove = new ArrayList<>();
        ArrayList<Cell> cellsToAdd = new ArrayList<>();

        for (Cell cell : neighborsCopy){
            // Check if the cell is adjacent and is a cancer cell (which has id 2)
            if (checkAdjacent(cell) && cell.getId() == 2) {
                cancerCells.add(cell); // Add this adjacent cancer cell to the list of nearby cancer cells
            }
        }

        // While there are nearby cancer cells
        while(!cancerCells.isEmpty()){

            Cell target = cancerCells.remove(random.nextInt(cancerCells.size()));

            // Replace the cancer cell with a new dead cell
            Pair deadCellCoords = new Pair(target.getX(), target.getY());
            cellsToRemove.add(target);
            cellsToAdd.add(new DeadCell(deadCellCoords));

            // Optional - 50% chance to continue attacking cancer cells
            if(random.nextDouble() > 0.5) {
                break;
            }
        }

        // Apply all the changes
        neighbors.removeAll(cellsToRemove);
        neighbors.addAll(cellsToAdd);
    }

    private boolean checkAdjacent(Cell cell) {
        int dx = Math.abs(this.getX() - cell.getX());
        int dy = Math.abs(this.getY() - cell.getY());
        //Check if the cell is adjacent and not at position of the immune cell
        return (dx <= 1 && dy <= 1 && !(dx == 0 && dy == 0));
    }
}
