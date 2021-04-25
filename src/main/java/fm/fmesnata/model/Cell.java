package fm.fmesnata.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class Cell extends Rectangle {

    public static final int UNDERPOPULATION_THRESHOLD = 1;
    public static final int OVERPOPULATION_THRESHOLD = 4;
    private boolean alive;
    public static final Color DEAD_CELL_COLOR = Color.LIGHTGREY;
    public static final Color LIVING_CELL_COLOR = Color.FORESTGREEN;

    public Cell() {
        this.setFill(DEAD_CELL_COLOR);
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public Cell nextState(List<Cell> surroundingCells) {
        if (surroundingCells.size() > 8) {
            throw new IllegalArgumentException("Cell cannot be surrounded by more than 8 cells. Here : " + surroundingCells.size());
        }
        long livingCells = surroundingCells.stream().filter(Cell::isAlive).count();
        Cell cell = new Cell();
        if (isAliveAndSurroundedBy2Or3LivingCells(livingCells) || isDeadAndSurroundedBy3LivingCells(livingCells)) {
            cell.setAlive(true);
            cell.setFill(LIVING_CELL_COLOR);
        }
        return cell;
    }

    public void changeState() {
        if (alive) {
            alive = false;
            setFill(DEAD_CELL_COLOR);
        } else {
            alive = true;
            setFill(LIVING_CELL_COLOR);
        }
    }

    private boolean isAliveAndSurroundedBy2Or3LivingCells(long livingCells) {
        return isAlive() && livingCells > UNDERPOPULATION_THRESHOLD && livingCells < OVERPOPULATION_THRESHOLD;
    }

    private boolean isDeadAndSurroundedBy3LivingCells(long livingCells) {
        return !isAlive() && livingCells == 3;
    }
}
