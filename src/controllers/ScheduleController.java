package controllers;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import controllers.tablemodel.DragSelectionCellFactory;
import controllers.tablemodel.HourRow;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {

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
    private JFXTextField textFieldSearch;

    @FXML
    private JFXListView<AnchorPane> listViewSubjects;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        buildSubjectCards();
        buildAutoCompleteTextField();
        buildTreeTableView();
    }

    private void buildTreeTableView() {
        tableView.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });

        hourID.setCellValueFactory(new PropertyValueFactory<>("hour"));
        mondayID.setCellValueFactory(new PropertyValueFactory<>("monday"));
        tuesdayID.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        wednesdayID.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        thursdayID.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        fridayID.setCellValueFactory(new PropertyValueFactory<>("friday"));
        saturdayID.setCellValueFactory(new PropertyValueFactory<>("saturday"));
        tableView.heightProperty().addListener((obs, oldVal, newVal) -> {
            tableView.setFixedCellSize((newVal.doubleValue() / 15));
        });

        for (int i = 6; i < 20; i++) {
            tableView.getItems().add(new HourRow(i + ":30 - " + (i + 1) + ":30", "", "", "", "", "", ""));
        }

        tableView.setId("my-table");

        tableView.getSelectionModel().getSelectedCells().addListener((InvalidationListener) c -> {

            for (TablePosition pos : tableView.getSelectionModel().getSelectedCells()) {
                int row = pos.getRow();
                int col = pos.getColumn();

                // HourRow item = tableView.getItems().get(row);

                if (col == 0) {
                    Platform.runLater(() -> {
                        tableView.getSelectionModel().clearSelection(row, hourID);
                    });
                    break;
                }

                System.out.println("x=" + row + "y=" + col);
                //String dispLastName = (String) pos.getTableColumn().getCellObservableValue(item).getValue();

            }
        });

        final Callback<TableColumn<HourRow, String>, TableCell<HourRow, String>> cellFactory = new DragSelectionCellFactory();
        hourID.setCellFactory(cellFactory);
        mondayID.setCellFactory(cellFactory);
        tuesdayID.setCellFactory(cellFactory);
        wednesdayID.setCellFactory(cellFactory);
        thursdayID.setCellFactory(cellFactory);
        fridayID.setCellFactory(cellFactory);
        saturdayID.setCellFactory(cellFactory);

        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    private void buildSubjectCards() {
        for (int i = 0; i < 6; i++) {
            CardSubjectController c = new CardSubjectController("Nombre materia " + i, "IST124",
                    3, listViewSubjects);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/components/cardSubject.fxml"));
            loader.setController(c);

            try {
                listViewSubjects.getItems().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void buildAutoCompleteTextField() {
        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.getSuggestions().addAll(
                "Valentina Nieto", "Valentina Imitola", "Valentina Utria",
                "Valentina Reyes", "Valentina Camacho", "Valentina Camelo",
                "Valentina Cavadia", "Valentina Lopez", "Valentina Gonzalez",
                "Valentina Nietoo", "Valentina Imitolaa", "Valentina Utriaa",
                "Valentina Reyees", "Valentina Camachoo", "Valentina Cameelo",
                "Valentina Cavadaia", "Valentina Loopez", "Valentina Gonzalezz"
        );

        // detect selection
        autoCompletePopup.setSelectionHandler(event -> {
            System.out.println(event.getObject());

            // TODO: Add subject to listViewSubjects

            textFieldSearch.setText("");
        });

        // filtering options
        textFieldSearch.textProperty().addListener(observable -> {
            autoCompletePopup.filter(string -> string.toLowerCase().contains(textFieldSearch.getText().toLowerCase()));
            if (autoCompletePopup.getFilteredSuggestions().isEmpty() || textFieldSearch.getText().trim().isEmpty()) {
                autoCompletePopup.hide();
                // if you remove textField.getText.trim().isEmpty() when text field is empty it suggests all options
                // so you can choose
            } else {
                autoCompletePopup.show(textFieldSearch);
            }
        });
    }
}


