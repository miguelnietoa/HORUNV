package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.ResourceBundle;

import database.DatabaseManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Course;
import model.PossibleSchedule;
import model.Request;
import model.User;

public class CardNotificationsController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXListView<AnchorPane> listNotifications;

    @FXML
    private JFXComboBox<String> comboBoxType;

    @FXML
    private JFXButton btnSend;

    @FXML
    private JFXComboBox<String> comboBoxScheduleSelect;

    @FXML
    private JFXButton btnShare;

    @FXML
    private JFXTextField txtInData;

    private ScheduleController sc;
    private int selectedOption;
    private Request selectedRequest = null;
    private PossibleSchedule possible = null;
    @FXML
    void initialize() {
        this.comboBoxScheduleSelect.setOnAction(this::selectFromSchedulesComboBox);
    }

    @FXML
    void selectFromComboBox(ActionEvent event) {
        this.selectedOption = comboBoxType.getSelectionModel().getSelectedIndex();
        this.txtInData.setEditable(true);
        this.txtInData.setPromptText("Escriba el " + comboBoxType.getValue().toLowerCase());
    }

    void selectFromSchedulesComboBox(ActionEvent event){
        int index = comboBoxScheduleSelect.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            this.possible = User.getPossibleSchedules().get(index);
        }else{
            this.possible = null;
        }
    }

    @FXML
    void btnSendAction(ActionEvent event) {
        Platform.runLater(() -> {
            String data = this.txtInData.getText().trim();
            if (data.isEmpty()) {
                sc.showMessage("Advertencia", "¡Digite un valor válido!");
            } else {
                int userCode;
                if (selectedOption == 0) { // Usercode selected
                    try {
                        userCode = Integer.parseInt(data);
                    } catch (Exception e) {
                        sc.showMessage("Advertencia", "¡Digite un valor válido!");
                        return;
                    }
                } else { // Username selected
                    userCode = DatabaseManager.getInfoUserByUsername(data);
                }
                if (userCode == User.getCodeUser()) {
                    sc.showMessage("Advertencia", "¡No puedes enviar solicitudes a ti mismo!");
                } else if (userCode == -1 || !DatabaseManager.sendShareNotification(userCode)) {
                    sc.showMessage("Advertencia", "¡El estudiante suministrado no existe!");
                } else {
                    this.txtInData.setText("");
                    sc.showMessage("¡Grandioso!", "Solucitud de horario enviada correctamente.");
                }
            }
        });
    }

    @FXML
    void btnShareAction(ActionEvent event) {
        if (possible != null){
            if (this.selectedRequest!= null){
                if (DatabaseManager.isShared(possible,selectedRequest)){
                    sc.showMessage("Alerta","El horario seleccionado ya ha sido compartido con esta persona");
                }else{
                    boolean sw = DatabaseManager.updateConsecutivo(possible,selectedRequest);
                    if (sw){
                        sc.showMessage("Alerta","Horario compartido exitosamente!");
                        Platform.runLater(() -> {
                            this.listNotifications.getItems().remove(listNotifications.getItems().get(User.getRequests().indexOf(selectedRequest)));
                            User.getRequests().remove(selectedRequest);
                        });
                    }else{
                        sc.showMessage("Alerta","Error al compartir tu  horario.\nIntentalo nuevamente!");
                    }
                }
            }else{
                sc.showMessage("Advertencia","Debe seleccionar una solicitud a contestar!");
            }
        } else{
            sc.showMessage("Advertencia","Debe seleccionar un horario para compartir!");
        }
    }

    @FXML
    void listNotificationsAction(MouseEvent event) {
        int index = this.listNotifications.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            this.selectedRequest = User.getRequests().get(index);
        }else{
            this.selectedRequest = null;
        }
    }

    public CardNotificationsController(ScheduleController sc) {
        this.sc = sc;
        loadInfoToComboBox();
        addRequestsToList();
    }

    private void addRequestsToList(){
        DatabaseManager.addRequests();
        Platform.runLater(() -> {
            for (Request request : User.getRequests()) {
                request.setFullNameStudent(DatabaseManager.getNameStudent(request.getCodeStudentRequested()));
                buildRequestCard(request);
            }
        });
    }

    private void loadInfoToComboBox() {
        Platform.runLater(() -> {
            comboBoxType.getItems().add("Código");
            comboBoxType.getItems().add("Usuario");
            DatabaseManager.getPossibleSchedule();
            if (!User.getPossibleSchedules().isEmpty()) {
                for (PossibleSchedule pSchedule : User.getPossibleSchedules()) {
                    comboBoxScheduleSelect.getItems().add(pSchedule.getNombre());
                }
            }
        });
    }
    public void buildRequestCard(Request request) {
        CardRequestController c = new CardRequestController(request,this.listNotifications);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/components/cardRequest.fxml"));
        loader.setController(c);
        try {
            listNotifications.getItems().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
