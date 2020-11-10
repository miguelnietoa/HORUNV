package controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardSubjectController implements Initializable {

    private String subjectName;

    private String subjectCode;

    private int subjectCredits;

    private StackPane stackPane;

    @FXML
    JFXListView<AnchorPane> listViewSubjects;

    @FXML
    private Button btnRemove;

    @FXML
    private Button btnFilter;

    @FXML
    private Label lblSubjectName;

    @FXML
    private Label lblInfo;

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public void setSubjectCredits(int subjectCredits) {
        this.subjectCredits = subjectCredits;
    }

    public CardSubjectController(String subjectName, String subjectCode, int subjectCredits,
                                 JFXListView<AnchorPane> listViewSubjects, StackPane stackPane) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.subjectCredits = subjectCredits;
        this.listViewSubjects = listViewSubjects;
        this.stackPane = stackPane;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblSubjectName.setText(subjectName);
        lblInfo.setText(subjectCode + " | " + subjectCredits + " créditos");
        btnFilter.setOnAction(this::btnFilterOnAction);
        btnRemove.setOnAction(this::btnRemoveOnAction);
    }

    void btnFilterOnAction(ActionEvent event) {
        try {
            JFXDialogLayout content = new JFXDialogLayout();
            Parent parent = FXMLLoader.load(getClass().getResource("../ui/windowFilter.fxml"));
            content.setBody(parent);
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.RIGHT);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void btnRemoveOnAction(ActionEvent event) {
        Platform.runLater(() -> this.listViewSubjects.getItems().remove(btnRemove.getParent()));
    }
}
