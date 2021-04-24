package fm.fmesnata;

import fm.fmesnata.model.Cell;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class App extends Application {

    private static final int CELL_SIZE = 15;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int COLUMN_COUNT = 75;
    public static final int ROW_COUNT = 40;
    public static final int SPACE_BETWEEN_CELLS = 1;

    @Override
    public void start(Stage stage) {
        BorderPane borderPane = new BorderPane();
        VBox cellsContainer = createCellsContainer();
        borderPane.setCenter(cellsContainer);
        Scene scene = new Scene(borderPane, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private VBox createCellsContainer() {
        VBox rows = new VBox();
        rows.setSpacing(1);
        for (int i = 0; i < ROW_COUNT; i++) {
            HBox cols = createCellsColumns();
            rows.getChildren().addAll(cols);
            rows.setAlignment(Pos.CENTER);
        }
        return rows;
    }

    private HBox createCellsColumns() {
        HBox col = new HBox();
        col.setSpacing(SPACE_BETWEEN_CELLS);
        col.setAlignment(Pos.CENTER);
        for (int i = 0; i < COLUMN_COUNT; i++) {
            Cell cell = new Cell();
            cell.setWidth(CELL_SIZE);
            cell.setHeight(CELL_SIZE);
            cell.setFill(Color.GREY);
            cell.setOnMouseClicked(changeCellState(cell));
            col.getChildren().add(cell);
        }
        return col;
    }

    private EventHandler<MouseEvent> changeCellState(Cell cell) {
        return event -> {
            if (cell.isAlive()) {
                cell.setAlive(false);
                cell.setFill(Color.GREY);
            } else {
                cell.setAlive(true);
                cell.setFill(Color.FORESTGREEN);
            }
        };
    }
}