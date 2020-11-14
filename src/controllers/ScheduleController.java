package controllers;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import controllers.tablemodel.DragSelectionCellFactory;
import controllers.tablemodel.HourRow;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {

    @FXML
    private StackPane stackPane;

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

    @FXML
    private ImageView btnProjection;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tooltip.install(btnProjection, new Tooltip("Ver proyección"));
        buildAutoCompleteTextField();
        buildTableView();
    }

    @FXML
    void btnProjectionOnAction(MouseEvent event) {
        System.out.println("click");
    }

    private void buildTableView() {
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

        for (int i = 6; i < 20; i++) {
            tableView.getItems().add(new HourRow(i + ":30 - " + (i + 1) + ":30", "", "", "", "", "", ""));
        }

        tableView.getSelectionModel().getSelectedCells().addListener((ListChangeListener<? super TablePosition>) c -> {
            for (TablePosition pos : c.getList()) {
                int row = pos.getRow();
                int col = pos.getColumn();

                // HourRow item = tableView.getItems().get(row);

                if (col == 0) {
                    Platform.runLater(() -> tableView.getSelectionModel().clearSelection(row, hourID));
                    break;
                }

                //System.out.println("x=" + row + "y=" + col);
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

    private void buildSubjectCard(String name, String code, int credits) {

        CardSubjectController c = new CardSubjectController(name, code, credits, listViewSubjects, stackPane);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/components/cardSubject.fxml"));
        loader.setController(c);
        try {
            listViewSubjects.getItems().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
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
            buildSubjectCard(event.getObject(), "IST1232", 4);
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


