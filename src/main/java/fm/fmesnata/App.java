package fm.fmesnata;

import fm.fmesnata.model.Cell;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class App extends Application {

    private static final int CELL_SIZE = 15;
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final int COLUMN_COUNT = 75;
    private static final int ROW_COUNT = 40;
    private static final int SPACE_BETWEEN_CELLS = 1;
    private static final int DEFAULT_FPS = 10;
    public static final int ONE_SECOND = 1000;
    private Cell[][] cells;
    private Timeline animation;
    private BorderPane borderPane;

    @Override
    public void start(Stage stage) {
        borderPane = new BorderPane();
        cells = initCells();
        VBox cellsBoard = createCellsBoard(cells);
        borderPane.setTop(createToolBar());
        borderPane.setCenter(cellsBoard);
        Scene scene = new Scene(borderPane, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private Cell[][] initCells() {
        Cell[][] cellsArray = new Cell[ROW_COUNT][COLUMN_COUNT];
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                cellsArray[row][col] = Cell.createDeadCell();
            }
        }
        return cellsArray;
    }

    private VBox createCellsBoard(Cell[][] cells) {
        VBox rows = new VBox(1);
        for (Cell[] c : cells) {
            HBox cols = createCellsColumns(c);
            rows.getChildren().addAll(cols);
            rows.setAlignment(Pos.CENTER);
        }
        return rows;
    }

    private HBox createCellsColumns(Cell[] cols) {
        HBox col = new HBox();
        col.setSpacing(SPACE_BETWEEN_CELLS);
        col.setAlignment(Pos.CENTER);
        for (Cell cell : cols) {
            cell.setWidth(CELL_SIZE);
            cell.setHeight(CELL_SIZE);
            cell.setOnMouseClicked(event -> cell.changeState());
            col.getChildren().add(cell);
        }
        return col;
    }

    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();
        Label label = new Label("FPS");
        TextField animationDurationField = new TextField(String.valueOf(DEFAULT_FPS));
        animationDurationField.getText();
        Button start = new Button("Start");
        Button stop = new Button("Stop");
        Button reset = new Button("Reset");
        reset.setDisable(true);
        start.setOnMouseClicked(event -> {
            int fps;
            try {
                fps = Integer.parseInt(animationDurationField.getText());
            } catch (NumberFormatException e) {
                fps = DEFAULT_FPS;
            }
            animation = createAnimation(ONE_SECOND / fps);
            start.setDisable(true);
            reset.setDisable(true);
            animation.play();
            stop.setDisable(false);
        });

        stop.setDisable(true);
        stop.setOnMouseClicked(event -> {
            stop.setDisable(true);
            animation.stop();
            start.setDisable(false);
            reset.setDisable(false);
        });

        reset.setOnMouseClicked(event -> {
            cells = initCells();
            borderPane.setCenter(createCellsBoard(cells));
        });
        toolBar.getItems().addAll(label, animationDurationField, start, stop, reset);
        return toolBar;
    }

    private Timeline createAnimation(int milliseconds) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(milliseconds), event -> updateDisplay()));
        timeline.setCycleCount(Animation.INDEFINITE);
        return timeline;
    }

    private void updateDisplay() {
        Cell[][] newCellsArray = new Cell[ROW_COUNT][COLUMN_COUNT];
        for (int row = 0; row < ROW_COUNT; row++) {
            for (int col = 0; col < COLUMN_COUNT; col++) {
                Cell currentCell = cells[row][col];
                List<Cell> surroundingCells = new ArrayList<>(8);

                if (row > 0 && row < ROW_COUNT - 1) {
                    Cell topCenterCell = cells[row-1][col];
                    Cell bottomCenterCell = cells[row+1][col];
                    surroundingCells.addAll(List.of(topCenterCell, bottomCenterCell));
                    if (col > 0) {
                        Cell topLeftCell = cells[row-1][col-1];
                        Cell middleLeftCell = cells[row][col-1];
                        Cell bottomLeftCell = cells[row+1][col-1];
                        surroundingCells.addAll(List.of(topLeftCell, middleLeftCell, bottomLeftCell));
                    }
                    if (col < COLUMN_COUNT - 1) {
                        Cell topRightCell = cells[row-1][col+1];
                        Cell middleRightCell = cells[row][col+1];
                        Cell bottomRightCell = cells[row+1][col+1];
                        surroundingCells.addAll(List.of(topRightCell, middleRightCell, bottomRightCell));
                    }
                } else if (row < ROW_COUNT - 1) {
                    Cell bottomCenterCell = cells[row+1][col];
                    surroundingCells.addAll(List.of(bottomCenterCell));
                    if (col > 0) {
                        Cell middleLeftCell = cells[row][col-1];
                        Cell bottomLeftCell = cells[row+1][col-1];
                        surroundingCells.addAll(List.of(middleLeftCell, bottomLeftCell));
                    }
                    if (col < COLUMN_COUNT - 1) {
                        Cell middleRightCell = cells[row][col+1];
                        Cell bottomRightCell = cells[row+1][col+1];
                        surroundingCells.addAll(List.of(middleRightCell, bottomRightCell));
                    }
                } else {
                    Cell topCenterCell = cells[row-1][col];
                    surroundingCells.addAll(List.of(topCenterCell));
                    if (col > 0) {
                        Cell topLeftCell = cells[row-1][col-1];
                        Cell middleLeftCell = cells[row][col-1];
                        surroundingCells.addAll(List.of(topLeftCell, middleLeftCell));
                    }
                    if (col < COLUMN_COUNT - 1) {
                        Cell topRightCell = cells[row-1][col+1];
                        Cell middleRightCell = cells[row][col+1];
                        surroundingCells.addAll(List.of(topRightCell, middleRightCell));
                    }
                }
                Cell newCell = currentCell.nextState(surroundingCells);
                newCellsArray[row][col] = newCell;
            }
        }
        cells = newCellsArray;
        VBox cellsContainer = createCellsBoard(newCellsArray);
        borderPane.setCenter(cellsContainer);
    }
}