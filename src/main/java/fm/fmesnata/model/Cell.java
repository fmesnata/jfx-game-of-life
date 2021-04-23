package fm.fmesnata.model;

import java.util.List;

public class Cell {

    public static final int UNDERPOPULATION_THRESHOLD = 1;
    public static final int OVERPOPULATION_THRESHOLD = 4;
    private boolean alive;

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public Cell nextState(List<Cell> surroundingCells) {
        long livingCells = surroundingCells.stream().filter(Cell::isAlive).count();
        Cell cell = new Cell();
        cell.setAlive(false);
        if (livingCells > UNDERPOPULATION_THRESHOLD && livingCells < OVERPOPULATION_THRESHOLD) {
            cell.setAlive(true);
        }
        return cell;
    }
}
