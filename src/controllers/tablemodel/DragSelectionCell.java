package controllers.tablemodel;

import javafx.scene.control.TableCell;
import javafx.scene.control.Tooltip;

public class DragSelectionCell extends TableCell<HourRow, String> {

    public DragSelectionCell() {

        setOnDragDetected(event -> {
            if (getTableColumn().getTableView().getSelectionModel() != null) {
                startFullDrag();
                getTableColumn().getTableView().getSelectionModel().select(getIndex(), getTableColumn());
            }
        });
        setOnMouseDragEntered(event -> {
            if (getTableColumn().getTableView().getSelectionModel() != null){
                getTableColumn().getTableView().getSelectionModel().select(getIndex(), getTableColumn());
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
        if (empty || item.equals("")) {
            setTooltip(null);
        } else if (item.contains("\n")) {
            setTooltip(new Tooltip(item));
            setStyle("-fx-background-color: yellow");
        }

    }
}
