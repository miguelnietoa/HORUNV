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
import model.Student;
import model.Subject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardSubjectController implements Initializable {

    private Subject subject;

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

    public CardSubjectController(Subject subject,
                                 JFXListView<AnchorPane> listViewSubjects, StackPane stackPane) {
        this.subject = subject;
        this.listViewSubjects = listViewSubjects;
        this.stackPane = stackPane;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblSubjectName.setText(subject.getName());
        lblInfo.setText(subject.getCode() + " | " + subject.getCredits() + " créditos");
        btnFilter.setOnAction(this::btnFilterOnAction);
        btnRemove.setOnAction(this::btnRemoveOnAction);
    }

    void btnFilterOnAction(ActionEvent event) {
        System.out.println("filter " + subject.getName());
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
        Student.getSelectedSubjects().remove(subject);
        Platform.runLater(() -> this.listViewSubjects.getItems().remove(btnRemove.getParent()));
    }
}
