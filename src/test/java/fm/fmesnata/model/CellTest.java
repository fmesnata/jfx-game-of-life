package fm.fmesnata.model;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CellTest {

    private Cell cell;
    private static final int MAX_SURROUNDING_CELLS = 8;
    public static final Color DEAD_CELL_COLOR = Color.LIGHTGREY;
    public static final Color LIVING_CELL_COLOR = Color.FORESTGREEN;

    @Test
    void a_cell_cannot_be_surrounded_by_more_than_8_cells() {
        cell = Cell.createDeadCell();
        List<Cell> surroundingCells = createSurroundingCells(9);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> cell.nextState(surroundingCells))
                .withMessage("Cell cannot be surrounded by more than 8 cells. Here : 9");
    }

    @Nested
    class LivingCellTest {

        @BeforeEach
        void setup() {
            cell = Cell.createLivingCell();
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        void living_cell_surrounded_with_less_than_2_living_cells_die(int livingCells) {
            List<Cell> surroundingCells = createSurroundingCells(livingCells);

            Cell newState = cell.nextState(surroundingCells);

            assertThat(newState.isAlive()).isFalse();
            assertThat(newState.getFill()).isEqualTo(DEAD_CELL_COLOR);
        }

        @ParameterizedTest
        @ValueSource(ints = {2, 3})
        void living_cell_surrounded_with_2_or_3_living_cells_survive(int livingCells) {
            List<Cell> surroundingCells = createSurroundingCells(livingCells);

            Cell newState = cell.nextState(surroundingCells);

            assertThat(newState.isAlive()).isTrue();
            assertThat(newState.getFill()).isEqualTo(LIVING_CELL_COLOR);
        }

        @ParameterizedTest
        @ValueSource(ints = {4, 5, 6, 7, 8})
        void living_cell_surrounded_with_more_than_4_living_cells_die(int livingCells) {
            List<Cell> surroundingCells = createSurroundingCells(livingCells);

            Cell newState = cell.nextState(surroundingCells);

            assertThat(newState.isAlive()).isFalse();
            assertThat(newState.getFill()).isEqualTo(DEAD_CELL_COLOR);
        }

        @Test
        void change_state_of_living_cell_become_dead() {
            Cell cell = Cell.createLivingCell();
            cell.changeState();
            assertThat(cell.isAlive()).isFalse();
            assertThat(cell.getFill()).isEqualTo(DEAD_CELL_COLOR);
        }
    }

    @Nested
    class DeadCellTest {

        @BeforeEach
        void setup() {
            cell = Cell.createDeadCell();
        }

        @Test
        void dead_cell_surrounded_with_3_living_cells_reborn() {
            List<Cell> surroundingCells = createSurroundingCells(3);

            Cell newState = cell.nextState(surroundingCells).nextState(surroundingCells);

            assertThat(newState.isAlive()).isTrue();
            assertThat(newState.getFill()).isEqualTo(LIVING_CELL_COLOR);
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 1, 2, 4, 5, 6, 7, 8})
        void dead_cell_surrounded_with_less_than_3_or_more_than_3_living_cells_stay_dead(int livingCells) {
            List<Cell> surroundingCells = createSurroundingCells(livingCells);

            Cell newState = cell.nextState(surroundingCells);

            assertThat(newState.isAlive()).isFalse();
            assertThat(newState.getFill()).isEqualTo(DEAD_CELL_COLOR);
        }

        @Test
        void change_state_of_dead_cell_become_alive() {
            Cell cell = Cell.createDeadCell();
            cell.changeState();
            assertThat(cell.isAlive()).isTrue();
            assertThat(cell.getFill()).isEqualTo(LIVING_CELL_COLOR);
        }
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
            return alive ? Cell.createLivingCell() : Cell.createDeadCell();
        }).collect(Collectors.toList());
    }
}
