package controllers.tablemodel;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class DragSelectionCellFactory implements Callback<TableColumn<HourRow, String>, TableCell<HourRow, String>> {

    @Override
    public TableCell<HourRow, String> call(final TableColumn<HourRow, String> col) {
        return new DragSelectionCell();
    }
}

