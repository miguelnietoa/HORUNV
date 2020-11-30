package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import controllers.tablemodel.DragSelectionCellFactory;
import controllers.tablemodel.HourRow;
import database.DatabaseManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.Course;
import model.PossibleSchedule;
import model.Schedule;
import model.User;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CompareSchedulesController implements Initializable {
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

    @FXML
    private JFXButton btnInfo2;

    @FXML
    private JFXButton btnInfo1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buildTableView();
        new Thread() {
            @Override
            public void run() {
                super.run();
                DatabaseManager.getPossibleSchedule();
                DatabaseManager.getSchedulesSharedWithMe();

                List<String> mySchedules = User.getPossibleSchedules().stream().map(pSchedule -> pSchedule.getNombre())
                        .collect(Collectors.toList());
                List<String> schedulesSharedWithMe = User.getSchedulesSharedWithMe().stream()
                        .map(pSchedule -> pSchedule.getNombre() + " (" + DatabaseManager.getUsernameFromCode(pSchedule.getCodigoEstudiante()) + ")")
                        .collect(Collectors.toList());

                List<String> merged = Stream.concat(mySchedules.stream(), schedulesSharedWithMe.stream())
                        .collect(Collectors.toList());
                Platform.runLater(() -> {
                    cbSchedule1.getItems().add("Ninguno");
                    cbSchedule1.getItems().addAll(merged);
                    cbSchedule2.getItems().add("Ninguno");
                    cbSchedule2.getItems().addAll(merged);
                });
            }
        }.start();
    }

    @FXML
    void btnInfo1OnAction(ActionEvent event) {
        showDuplicated(cbSchedule1);
    }

    @FXML
    void btnInfo2OnAction(ActionEvent event) {
        showDuplicated(cbSchedule2);
    }

    private void showDuplicated(JFXComboBox combo){
        PossibleSchedule schedule = getSelectedSchedule(combo);
        if (schedule!=null){
            LinkedList<String> students = DatabaseManager.getStudentDuplicatedSchedule(schedule);
            if (students.isEmpty()){
                showMessage("Coincidencias de Horario","Ningun otro estudiante tiene un horario igual a este.");
            }else {
                String message = "Los siguientes estudiantes tienen este mismo horario:\n";
                for (String student : students) {
                    message = message + "- " + student + "\n";
                }
                showMessage("Coincidencias de Horario", message);
            }
        }else {
            showMessage("Advertencia", "Por favor seleccione un horario para poder mostrar\ncon quien coincide.");
        }
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

        final Callback<TableColumn<HourRow, String>, TableCell<HourRow, String>> cellFactory = new DragSelectionCellFactory();
        mondayID.setCellFactory(cellFactory);
        tuesdayID.setCellFactory(cellFactory);
        wednesdayID.setCellFactory(cellFactory);
        thursdayID.setCellFactory(cellFactory);
        fridayID.setCellFactory(cellFactory);
        saturdayID.setCellFactory(cellFactory);
    }

    private PossibleSchedule getSelectedSchedule(JFXComboBox<String> comboBox) {
        int index = comboBox.getSelectionModel().getSelectedIndex();
        int sizePossible = User.getPossibleSchedules().size();
        int sizeShared = User.getSchedulesSharedWithMe().size();
        PossibleSchedule schedule = null;
        if (index != 0 && index != -1) {
            if (index <= sizePossible) {
                schedule = User.getPossibleSchedules().get(index - 1);
            } else if (index - sizePossible <= sizeShared) {
                schedule = User.getSchedulesSharedWithMe().get(index - sizePossible - 1);
            }
        }
        return schedule;
    }

    private void updateInfo(JFXComboBox<String> cb, Label lblName, Label lblOwner, Label lblCredits) {
        int index = cb.getSelectionModel().getSelectedIndex();
        PossibleSchedule schedule = getSelectedSchedule(cb);
        if (schedule == null) {
            lblName.setText("Nombre: ");
            lblOwner.setText("Dueño: ");
            lblCredits.setText("Créditos utilizados: ");
        } else {
            lblName.setText("Nombre: " + schedule.getNombre());
            lblOwner.setText("Dueño: " + DatabaseManager.getNameStudent(schedule.getCodigoEstudiante()));
            lblCredits.setText("Créditos utilizados: " + schedule.calcTotalCredits());
        }
        showDeleteSchedule();
        if (getSelectedSchedule(cbSchedule1)!=null) {
            showAddSchedule(getSelectedSchedule(cbSchedule1),"-1");
        }
        if (getSelectedSchedule(cbSchedule2)!=null){
            showAddSchedule(getSelectedSchedule(cbSchedule2),"-2");
        }
    }

    public void cbSchedule1OnAction(ActionEvent event) {
        updateInfo(cbSchedule1, lblNameSchedule1, lblOwnerSchedule1, lblCreditsSchedule1);
    }

    public void cbSchedule2OnAction(ActionEvent event) {
        updateInfo(cbSchedule2, lblNameSchedule2, lblOwnerSchedule2, lblCreditsSchedule2);
    }

    public void showAddSchedule(PossibleSchedule schedule, String fromComboBox) {
        for (Course c : schedule.getCourses()) {
            for (Schedule s : c.getSchedules()) {
                for (int[] index : s.getIndices()) {
                    HourRow item = tableView.getItems().get(index[0]);
                    String val = item.getFromIndex(index[1]);
                    if (!val.isEmpty()) {
                        item.setFromIndex(index[1], val + "\n" + c.getSubject().getCode()+fromComboBox);
                    } else {
                        item.setFromIndex(index[1], c.getSubject().getCode()+fromComboBox);
                    }
                }
            }
        }
    }

    public void showDeleteSchedule() {
        for (int i = 0; i < tableView.getColumns().size(); i++) {
            ScheduleController.columnCells(tableView.getColumns().get(i));
        }

        for (int i = 0; i <= 13; i++) {
            HourRow item = tableView.getItems().get(i);
            for (int j = 1; j <= 6; j++) {
                item.setFromIndex(j, "");
            }
        }
    }

    public void showMessage(String title, String message) {
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Text(title));
        layout.setBody(new Text(message));
        JFXButton button = new JFXButton("Okay");
        JFXDialog dialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.BOTTOM);
        button.setOnAction(event1 -> dialog.close());
        button.setStyle("-fx-background-color: #FF533D");
        layout.setActions(button);
        dialog.setContent(layout);
        dialog.show();
    }


}
