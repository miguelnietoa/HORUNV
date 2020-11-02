package controllers;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {

    @FXML
    private JFXTreeTableView<Hour> treeTableView;

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
        JFXTreeTableColumn<Hour, String> hourCol = new JFXTreeTableColumn<>("Hour");
        //hourCol.setPrefWidth(150);
        hourCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Hour, String> param) -> {
            if (hourCol.validateValue(param)) {
                return param.getValue().getValue().hour;
            } else {
                return hourCol.getComputedValue(param);
            }
        });

        JFXTreeTableColumn<Hour, String> mondayCol = new JFXTreeTableColumn<>("Monday");
        mondayCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Hour, String> param) -> {
            if (mondayCol.validateValue(param)) {
                return param.getValue().getValue().monday;
            } else {
                return mondayCol.getComputedValue(param);
            }
        });

        JFXTreeTableColumn<Hour, String> tuesdayCol = new JFXTreeTableColumn<>("Tuesday");
        tuesdayCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Hour, String> param) -> {
            if (tuesdayCol.validateValue(param)) {
                return param.getValue().getValue().tuesday;
            } else {
                return tuesdayCol.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Hour, String> wednesdayCol = new JFXTreeTableColumn<>("Wednesday");
        wednesdayCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Hour, String> param) -> {
            if (tuesdayCol.validateValue(param)) {
                return param.getValue().getValue().wednesday;
            } else {
                return wednesdayCol.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Hour, String> thursdayCol = new JFXTreeTableColumn<>("Thursday");
        //mondayCol.setPrefWidth(150);
        thursdayCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Hour, String> param) -> {
            if (thursdayCol.validateValue(param)) {
                return param.getValue().getValue().thursday;
            } else {
                return thursdayCol.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Hour, String> fridayCol = new JFXTreeTableColumn<>("Friday");
        fridayCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Hour, String> param) -> {
            if (thursdayCol.validateValue(param)) {
                return param.getValue().getValue().friday;
            } else {
                return fridayCol.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Hour, String> saturdayCol = new JFXTreeTableColumn<>("Saturday");
        saturdayCol.setCellValueFactory((TreeTableColumn.CellDataFeatures<Hour, String> param) -> {
            if (thursdayCol.validateValue(param)) {
                return param.getValue().getValue().saturday;
            } else {
                return saturdayCol.getComputedValue(param);
            }
        });

        // Finished
        ObservableList<Hour> rows = FXCollections.observableArrayList();

        for (int i = 6; i < 20; i++) {
            rows.add(
                new Hour(
                        String.format("%d:30-%d:30", i, i+1), "", "", "", "", "", "")
            );
        }

        final TreeItem<Hour> root = new RecursiveTreeItem<>(rows, RecursiveTreeObject::getChildren);


        treeTableView.setRoot(root);

        treeTableView.setShowRoot(false);
        treeTableView.getColumns().setAll(hourCol, mondayCol, tuesdayCol, wednesdayCol, thursdayCol, fridayCol, saturdayCol);
        treeTableView.getColumns().forEach(tableColumn -> {
            tableColumn.setSortable(false);
        });

        // preventColumnReordering
        Platform.runLater(() -> {
            for (Node header : treeTableView.lookupAll(".column-header")) {
                header.addEventFilter(MouseEvent.MOUSE_DRAGGED, Event::consume);
            }
        });
        treeTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        treeTableView.getSelectionModel().setCellSelectionEnabled(true);

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

class Hour extends RecursiveTreeObject<Hour> {

    StringProperty hour;
    StringProperty monday;
    StringProperty tuesday;
    StringProperty wednesday;
    StringProperty thursday;
    StringProperty friday;
    StringProperty saturday;

    public Hour(String hour, String monday, String tuesday, String wednesday,
                String thursday, String friday, String saturday) {
        this.hour = new SimpleStringProperty(hour);
        this.monday = new SimpleStringProperty(monday);
        this.tuesday = new SimpleStringProperty(tuesday);
        this.wednesday = new SimpleStringProperty(wednesday);
        this.thursday = new SimpleStringProperty(thursday);
        this.friday = new SimpleStringProperty(friday);
        this.saturday = new SimpleStringProperty(saturday);
    }
}
