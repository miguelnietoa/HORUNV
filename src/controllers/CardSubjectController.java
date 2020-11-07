package controllers;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
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
    int credits;
    JFXListView<AnchorPane> listViewSubjects;

    @FXML
    private Button btnRemove;

    @FXML
    private Button btnFilter;

    @FXML
    private Label lblSubjectName;

    @FXML
    private Label lblInfo;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public CardSubjectController(String subjectName, String subjectCode, int credits, JFXListView<AnchorPane> listViewSubjects) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.credits = credits;
        this.listViewSubjects = listViewSubjects;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnRemove.setOnAction(event ->
        {
            Platform.runLater(() -> {
                this.listViewSubjects.getItems().remove(btnRemove.getParent());
            });
        });

        btnFilter.setOnAction(event -> {
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

        });
        lblSubjectName.setText(subjectName);
        lblInfo.setText(subjectCode + " | " + credits);
    }
}
