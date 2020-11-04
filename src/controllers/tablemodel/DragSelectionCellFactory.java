package controllers.tablemodel;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

public class DragSelectionCellFactory implements Callback<TreeTableColumn<HourRow, String>, TreeTableCell<HourRow, String>> {

    @Override
    public TreeTableCell<HourRow, String> call(TreeTableColumn<HourRow, String> param) {
        return new DragSelectionCell();
    }
}

