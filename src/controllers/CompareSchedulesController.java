package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import controllers.tablemodel.HourRow;
import database.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class CompareSchedulesController implements Initializable {

    @FXML
    private TableView<HourRow> tableView;

    @FXML
    private TableColumn<HourRow, String> hourID;

    @FXML
    private TableColumn<HourRow, String> mondayID;

    @FXML
    private TableColumn<HourRow, String> tuesdayID;

    @FXML
    private TableColumn<HourRow, String> wednesdayID;

    @FXML
    private TableColumn<HourRow, String> thursdayID;

    @FXML
    private TableColumn<HourRow, String> fridayID;

    @FXML
    private TableColumn<HourRow, String> saturdayID;

    @FXML
    private JFXComboBox<String> cbSchedule1;

    @FXML
    private Label lblNameSchedule1;

    @FXML
    private Label lblOwnerSchedule1;

    @FXML
    private Label lblCreditsSchedule1;

    @FXML
    private JFXComboBox<String> cbSchedule2;

    @FXML
    private Label lblNameSchedule2;

    @FXML
    private Label lblOwnerSchedule2;

    @FXML
    private Label lblCreditsSchedule2;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buildTableView();
        //DatabaseManager.getPossibleSchedule();
    }

    private void buildTableView() {
        // no reorder columns
        tableView.widthProperty().addListener((source, oldWidth, newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });

        hourID.setCellValueFactory(new PropertyValueFactory<>("hour"));
        mondayID.setCellValueFactory(new PropertyValueFactory<>("monday"));
        tuesdayID.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        wednesdayID.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        thursdayID.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        fridayID.setCellValueFactory(new PropertyValueFactory<>("friday"));
        saturdayID.setCellValueFactory(new PropertyValueFactory<>("saturday"));
        tableView.heightProperty().addListener((obs, oldVal, newVal) ->
                tableView.setFixedCellSize((newVal.doubleValue() / 15)));

        // no allow selection
        tableView.setSelectionModel(null);

        for (int i = 6; i < 20; i++)
            tableView.getItems().add(new HourRow(i + ":30 - " + (i + 1) + ":30", "", "", "", "", "", ""));

    }
}
