package fm.fmesnata.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class CellTest {

    private Cell cell;
    private static final int MAX_SURROUNDING_CELLS = 8;

    @BeforeEach
    void setup() {
        cell = new Cell();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void living_cell_surrounded_with_less_than_2_living_cells_die(int livingCells) {
        List<Cell> surroundingCells = createSurroundingCells(livingCells);

        Cell newState = cell.nextState(surroundingCells);

        assertThat(newState.isAlive()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    public void living_cell_surrounded_with_2_or_3_living_cells_survive(int livingCells) {
        List<Cell> surroundingCells = createSurroundingCells(livingCells);

        Cell newState = cell.nextState(surroundingCells);

        assertThat(newState.isAlive()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 5, 6, 7, 8})
    public void living_cell_surrounded_with_more_than_4_living_cells_die(int livingCells) {
        List<Cell> surroundingCells = createSurroundingCells(livingCells);

        Cell newState = cell.nextState(surroundingCells);

        assertThat(newState.isAlive()).isFalse();
    }

    private List<Cell> createSurroundingCells(int numberOfLivingCells) {
        List<Cell> livingCells = createCells(numberOfLivingCells, true);
        List<Cell> deadCells = createCells(MAX_SURROUNDING_CELLS - numberOfLivingCells, false);
        List<Cell> surroundingCells = new ArrayList<>();
        surroundingCells.addAll(livingCells);
        surroundingCells.addAll(deadCells);
        return surroundingCells;
    }

    private List<Cell> createCells(int number, boolean alive) {
        return IntStream.rangeClosed(1, number).mapToObj(i -> {
            Cell c = new Cell();
            c.setAlive(alive);
            return c;
        }).collect(Collectors.toList());
    }
}
