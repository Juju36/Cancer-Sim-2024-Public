package Simulation;


/**
 *This is a cancer cell. It is the most complex cell as it can attack tissue or immune cells, or grow into a dead cell.
 * For attacking tissue, it is a 1 hit replace it with a dead cell.
 * Immune cells are cooler. Each hit from a cancer cell lowers its strength by 1. When an immune cell reaches 0 strength
 * it dies!
 *
 * It has a priority of action. If it can grow, it will grow. If it can kill a tissue cell, it will do that. Why?
 * Easiest way to grow is to kill a week tissue cell. If no other option, will attack immune cells. Path of
 * least resistance to growing basically.
 *
 * Growing means turning a dead cell into a CancerCell.
 */

import Util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class CancerCell extends Cell{

    public CancerCell(Pair coords){
        super(1,coords.getX(), coords.getY(),3);
    }

    @Override
    public void interactNeighbors(ArrayList<Cell> neighbors) {
        Random random = new Random();

        // Create copies of the neighbors list for safe iteration
        ArrayList<Cell> neighborsCopy = new ArrayList<>(neighbors);

        // Keep track of nearby dead, immune, or tissue cells
        ArrayList<Cell> deadCells = new ArrayList<>();
        ArrayList<Cell> immuneCells = new ArrayList<>();
        ArrayList<Cell> tissueCells = new ArrayList<>();

        // Temporary lists to avoid ConcurrentModificationException
        ArrayList<Cell> cellsToRemove = new ArrayList<>();
        ArrayList<Cell> cellsToAdd = new ArrayList<>();

        for (Cell cell : neighborsCopy) {
            // Check if a cell is adjacent and either a dead cell (id 0), immune cell (id 4), or tissue cell (id 1)
            if(checkAdjacent(cell) && cell.getId() == 0) {
                deadCells.add(cell); // Add this adjacent cell to the list of nearby dead cells
            }
            if (checkAdjacent(cell) && cell.getId() == 4) {
                immuneCells.add(cell); // Add this adjacent cell to the list of nearby immune cells
            }
            if (checkAdjacent(cell) && cell.getId() == 1) {
                tissueCells.add(cell); // Add this adjacent cell to the list of nearby tissue cells
            }
        }

        if (!deadCells.isEmpty()){
            Cell target = deadCells.get(random.nextInt(deadCells.size()));

            // Replace the dead cell with a cancer cell
            Pair cancerCellCoords = new Pair(target.getX(), target.getY());
            cellsToRemove.add(target);
            cellsToAdd.add(new CancerCell(cancerCellCoords));
        }

        else if (!tissueCells.isEmpty() && tissueCells.size() > immuneCells.size()){
            Cell target = tissueCells.get(random.nextInt(tissueCells.size()));

            // Replace the tissue cell with a cancer cell
            Pair deadCellCoords = new Pair(target.getX(), target.getY());
            cellsToRemove.add(target);
            cellsToAdd.add(new DeadCell(deadCellCoords));
        }

        else if (!immuneCells.isEmpty()){
            Cell target = immuneCells.get(random.nextInt(immuneCells.size()));

            // Lower immune cell's current strength by 1
            int currentStrength = target.getStrength();
            target.setStrength(currentStrength - 1);

            // If the immune cell's strength is 0, replace it with a dead cell
            if (target.getStrength() == 0){
                Pair deadCellCoords = new Pair(target.getX(), target.getY());
                cellsToRemove.add(target);
                cellsToAdd.add(new DeadCell(deadCellCoords));
            }
        }

        // Apply all changes to the neighbors list
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
