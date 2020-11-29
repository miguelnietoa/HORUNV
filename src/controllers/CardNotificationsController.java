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


    @FXML
    void initialize() {
        assert listNotifications != null : "fx:id=\"listNotifications\" was not injected: check your FXML file 'cardNotifications.fxml'.";
        assert comboBoxType != null : "fx:id=\"comboBoxType\" was not injected: check your FXML file 'cardNotifications.fxml'.";
        assert btnSend != null : "fx:id=\"btnSend\" was not injected: check your FXML file 'cardNotifications.fxml'.";
        assert comboBoxScheduleSelect != null : "fx:id=\"comboBoxScheduleSelect\" was not injected: check your FXML file 'cardNotifications.fxml'.";
        assert btnShare != null : "fx:id=\"btnShare\" was not injected: check your FXML file 'cardNotifications.fxml'.";
        assert txtInData != null : "fx:id=\"txtInData\" was not injected: check your FXML file 'cardNotifications.fxml'.";
    }

    @FXML
    void selectFromComboBox(ActionEvent event) {
        this.selectedOption = comboBoxType.getSelectionModel().getSelectedIndex();
        this.txtInData.setEditable(true);
        this.txtInData.setPromptText("Escriba el " + comboBoxType.getValue().toLowerCase());
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
                    sc.showMessage("¡Grandioso!", "Solucitud de horario enviada correctamente.");
                }
            }
        });
    }

    @FXML
    void btnShareAction(ActionEvent event) {

    }

    @FXML
    void listNotificationsAction(MouseEvent event) {

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
        CardRequestController c = new CardRequestController(request);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/components/cardRequest.fxml"));
        loader.setController(c);
        try {
            listNotifications.getItems().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
