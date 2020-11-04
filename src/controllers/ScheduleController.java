package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import controllers.tablemodel.DragSelectionCellFactory;
import controllers.tablemodel.HourRow;
import javafx.application.Platform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {

    @FXML
    private JFXTreeTableView<HourRow> treeTableView;

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
        JFXTreeTableColumn<HourRow, String> hourCol = new JFXTreeTableColumn<>("Hour");
        //HourRowCol.setPrefWidth(150);
        hourCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<HourRow, String> param) -> {
            if (hourCol.validateValue(param)) {
                return param.getValue().getValue().hourProperty();
            } else {
                return hourCol.getComputedValue(param);
            }
        });

        JFXTreeTableColumn<HourRow, String> mondayCol = new JFXTreeTableColumn<>("Monday");
        mondayCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<HourRow, String> param) -> {
            if (mondayCol.validateValue(param)) {
                return param.getValue().getValue().mondayProperty();
            } else {
                return mondayCol.getComputedValue(param);
            }
        });

        JFXTreeTableColumn<HourRow, String> tuesdayCol = new JFXTreeTableColumn<>("Tuesday");
        tuesdayCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<HourRow, String> param) -> {
            if (tuesdayCol.validateValue(param)) {
                return param.getValue().getValue().tuesdayProperty();
            } else {
                return tuesdayCol.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<HourRow, String> wednesdayCol = new JFXTreeTableColumn<>("Wednesday");
        wednesdayCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<HourRow, String> param) -> {
            if (tuesdayCol.validateValue(param)) {
                return param.getValue().getValue().wednesdayProperty();
            } else {
                return wednesdayCol.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<HourRow, String> thursdayCol = new JFXTreeTableColumn<>("Thursday");
        //mondayCol.setPrefWidth(150);
        thursdayCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<HourRow, String> param) -> {
            if (thursdayCol.validateValue(param)) {
                return param.getValue().getValue().thursdayProperty();
            } else {
                return thursdayCol.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<HourRow, String> fridayCol = new JFXTreeTableColumn<>("Friday");
        fridayCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<HourRow, String> param) -> {
            if (thursdayCol.validateValue(param)) {
                return param.getValue().getValue().fridayProperty();
            } else {
                return fridayCol.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<HourRow, String> saturdayCol = new JFXTreeTableColumn<>("Saturday");
        saturdayCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<HourRow, String> param) -> {
            if (thursdayCol.validateValue(param)) {
                return param.getValue().getValue().saturdayProperty();
            } else {
                return saturdayCol.getComputedValue(param);
            }
        });

        // Finished
        ObservableList<HourRow> rows = FXCollections.observableArrayList();

        for (int i = 6; i < 20; i++) {
            rows.add(
                    new HourRow(
                            String.format("%d:30-%d:30", i, i + 1), "", "", "", "", "", "")
            );
        }

        final TreeItem<HourRow> root = new RecursiveTreeItem<>(rows, RecursiveTreeObject::getChildren);


        treeTableView.setRoot(root);

        treeTableView.setShowRoot(false);
        treeTableView.getColumns().setAll(hourCol, mondayCol, tuesdayCol, wednesdayCol, thursdayCol, fridayCol, saturdayCol);

        final Callback<TreeTableColumn<HourRow, String>, TreeTableCell<HourRow, String>> cellFactory = new DragSelectionCellFactory();

        treeTableView.getColumns().forEach(tableColumn -> {
            tableColumn.setSortable(false);
        });

        mondayCol.setCellFactory(cellFactory);
        tuesdayCol.setCellFactory(cellFactory);
        wednesdayCol.setCellFactory(cellFactory);
        thursdayCol.setCellFactory(cellFactory);
        fridayCol.setCellFactory(cellFactory);
        saturdayCol.setCellFactory(cellFactory);

        System.out.println(fridayCol.getClass());
        // preventColumnReordering
        Platform.runLater(() -> {
            for (Node header : treeTableView.lookupAll(".column-header")) {
                header.addEventFilter(MouseEvent.MOUSE_DRAGGED, Event::consume);
            }
        });


        treeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeTableView.getSelectionModel().setCellSelectionEnabled(true);
        //TODO: No select cells in hour column 


    }

    private void buildSubjectCards() {
        for (int i = 0; i < 6; i++) {
            CardSubjectController c = new CardSubjectController("Nombre materia " + i, "IST124", 3);
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


