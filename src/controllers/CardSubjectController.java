package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CardSubjectController implements Initializable {
    String subjectName;
    String subjectCode;
    int credits;

    @FXML
    private Button btnRemove;

    @FXML
    private Button btnFilter;

    @FXML
    private Label lblSubjectName;

    @FXML
    private Label lblInfo;


    public CardSubjectController(String subjectName, String subjectCode, int credits) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.credits = credits;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnRemove.setOnAction(event -> System.out.println("Remove #" + subjectName));

        btnFilter.setOnAction(event -> {
            System.out.println("Filter #" + subjectName);
        });
        lblSubjectName.setText(subjectName);
    }
}
