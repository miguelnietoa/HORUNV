package controllers;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CardSubjectController implements Initializable {
    String subjectName;
    String subjectCode;
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
            System.out.println("Filter #" + subjectName);
        });
        lblSubjectName.setText(subjectName);
    }
}
