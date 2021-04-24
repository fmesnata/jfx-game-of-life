package fm.fmesnata;

import fm.fmesnata.model.Cell;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class App extends Application {

    private static final int CELL_SIZE = 15;
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int COLUMN_COUNT = 40;
    public static final int ROW_COUNT = 75;
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
        VBox columns = new VBox();
        columns.setSpacing(1);
        for (int i = 0; i < COLUMN_COUNT; i++) {
            HBox rows = createRowCells();
            columns.getChildren().addAll(rows);
            columns.setAlignment(Pos.CENTER);
        }
        return columns;
    }

    private HBox createRowCells() {
        HBox row = new HBox();
        row.setSpacing(SPACE_BETWEEN_CELLS);
        row.setAlignment(Pos.CENTER);
        for (int i = 0; i < ROW_COUNT; i++) {
            Cell cell = new Cell();
            cell.setWidth(CELL_SIZE);
            cell.setHeight(CELL_SIZE);
            cell.setFill(Color.GREY);
            row.getChildren().add(cell);
        }
        return row;
    }
}