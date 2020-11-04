package controllers.tablemodel;

import javafx.event.EventHandler;
import javafx.scene.control.TreeTableCell;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

public class DragSelectionCell extends TreeTableCell<HourRow, String> {

    public DragSelectionCell() {
        setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startFullDrag();
                getTableColumn().getTreeTableView().getSelectionModel().select(getIndex(), getTableColumn());
            }
        });
        setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {

            @Override
            public void handle(MouseDragEvent event) {
                getTableColumn().getTreeTableView().getSelectionModel().select(getIndex(), getTableColumn());
            }

        });
    }
    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
        } else {
            setText(item);
        }
    }

}