package controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardSubjectProjectionController implements Initializable {

    @FXML
    private Label lblSubjectName;

    @FXML
    private Label lblInfo;

    @FXML
    private Button btnFilter;

    String subjectName;
    String subjectInfo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setSubjectInfo(subjectName);
        setSubjectInfo(subjectInfo);
    }

    public CardSubjectProjectionController(String subjectName, String info) {
        this.subjectName = subjectName;
        this.subjectInfo = info;
    }

    public void setSubjectName(String subjectName) {
        lblSubjectName.setText(subjectName);
    }

    public void setSubjectInfo(String subjectInfo) {
        lblInfo.setText(subjectInfo);
    }

    void btnFilterOnAction(ActionEvent event) {
        System.out.println("Click");
    }
}
