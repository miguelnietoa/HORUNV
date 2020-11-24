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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import model.Course;
import model.User;
import model.Subject;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardActiveCourseController implements Initializable {

    private Course course;

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

    @FXML
    private Label lblProfessor;

    @FXML
    private Label lblCapacity;

    @FXML
    private Label lblNrc;

    public CardActiveCourseController(Course course,
                                      JFXListView<AnchorPane> listViewSubjects, StackPane stackPane) {
        this.course = course;
        this.listViewSubjects = listViewSubjects;
        this.stackPane = stackPane;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblSubjectName.setText(course.getSubject().getName());
        lblInfo.setText(course.getSubject().getCode() + " | " + course.getSubject().getCredits() + " créditos");

        //lblProfessor
        lblCapacity.setText("Capacidad: " + course.getTotalStudents());
        lblNrc.setText("NRC: " + course.getNrc());
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
        User.getSelectedSubjects().remove(course.getSubject());
        Platform.runLater(() -> this.listViewSubjects.getItems().remove(btnRemove.getParent()));
        User.setActiveIndexSchedule(0);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
