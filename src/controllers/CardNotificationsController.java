package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.awt.*;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import database.DatabaseManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import model.PossibleSchedule;
import model.User;

public class CardNotificationsController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXListView<?> listNotifications;

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
    void numberContrains(KeyEvent event) {

    }

    @FXML
    void selectFromComboBox(ActionEvent event) {
        this.selectedOption = comboBoxType.getSelectionModel().getSelectedIndex();
        System.out.println(this.selectedOption);
        this.txtInData.setEditable(true);
        this.txtInData.setPromptText("Escriba el " + comboBoxType.getValue());
    }

    @FXML
    void btnSendAction(ActionEvent event) {
        Platform.runLater(() -> {
            String data = this.txtInData.getText().trim();
            if (!data.isEmpty()){
                int codigo;
                if (selectedOption == 0){
                    codigo = Integer.parseInt(data);
                }else{
                    codigo = DatabaseManager.getInfoUserByUsername(data);
                }
                if (codigo != -1) {
                    DatabaseManager.sendShareNotification(codigo);
                }else{
                    sc.showMessage("El usuario suministrado no existe!");
                }
            }else{
                sc.showMessage("Digite un valor valido!");
            }
        });
    }

    public CardNotificationsController(ScheduleController sc) {
        this.sc = sc;
        loadInfoToComboBox();
    }

    private void loadInfoToComboBox() {
        Platform.runLater(() -> {
            comboBoxType.getItems().add("Codigo");
            comboBoxType.getItems().add("Usuario");
            DatabaseManager.getPossibleSchedule();
            if (!User.getPossibleSchedules().isEmpty()) {
                for (PossibleSchedule pSchedule : User.getPossibleSchedules()) {
                    comboBoxScheduleSelect.getItems().add(pSchedule.getNombre());
                }
            }
        });
    }
}
