package controllers;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardSubjectController implements Initializable {
    private String subjectName;

    private String subjectCode;

    private int subjectCredits;

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


    public CardSubjectController(String subjectName, String subjectCode, int subjectCredits,
                                 JFXListView<AnchorPane> listViewSubjects) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.subjectCredits = subjectCredits;
        this.listViewSubjects = listViewSubjects;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblSubjectName.setText(subjectName);
        lblInfo.setText(subjectCode + " | " + subjectCredits + " créditos");
    }

    @FXML
    void btnFilterOnAction(ActionEvent event) {
        Stage stage = new Stage();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/ui/windowFilter.fxml"));
            stage.setTitle("Hello World");
            stage.setScene(new Scene(root));
            stage.sizeToScene();
            stage.show();
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnRemoveOnAction(ActionEvent event) {
        Platform.runLater(() -> {
            this.listViewSubjects.getItems().remove(btnRemove.getParent());
        });
    }
}
