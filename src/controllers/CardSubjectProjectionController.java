package controllers;

import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import model.Student;
import model.Subject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardSubjectProjectionController implements Initializable {

    @FXML
    private Label lblSubjectName;

    @FXML
    private Label lblInfo;

    @FXML
    private Button btnAdd;

    private Subject subject;

    private JFXListView<AnchorPane> listViewSubjects;

    private StackPane stackPane;

    public CardSubjectProjectionController(Subject subject, JFXListView<AnchorPane> listViewSubjects,
                                           StackPane stackPane) {
        this.subject = subject;
        this.listViewSubjects = listViewSubjects;
        this.stackPane = stackPane;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblSubjectName.setText(subject.getName());
        lblInfo.setText(subject.getCode() + " | " + subject.getCredits() + " créditos");
        btnAdd.setOnAction(this::btnAddOnAction);
        btnAdd.setVisible(!Student.getSelectedSubjects().contains(subject));

    }

    void btnAddOnAction(ActionEvent event) {
        System.out.println("Se añade la materia " + subject.getCode());
        btnAdd.setVisible(false);
        Student.addSelectedSubject(subject);
        CardSubjectController c = new CardSubjectController(subject, listViewSubjects, stackPane);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/components/cardSubject.fxml"));
        loader.setController(c);
        try {
            listViewSubjects.getItems().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
