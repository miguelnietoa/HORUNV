package controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import controllers.windowfilter.WindowFilterController;
import database.DatabaseManager;
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

    ScheduleController sc;

    public CardActiveCourseController(Course course,
                                      JFXListView<AnchorPane> listViewSubjects, StackPane stackPane,ScheduleController sc) {
        this.course = course;
        this.listViewSubjects = listViewSubjects;
        this.stackPane = stackPane;
        this.sc=sc;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblSubjectName.setText(course.getSubject().getName());
        lblInfo.setText(course.getSubject().getCode() + " | " + course.getSubject().getCredits() + " créditos");

        updateInfoCourse();
        btnFilter.setOnAction(this::btnFilterOnAction);
        btnRemove.setOnAction(this::btnRemoveOnAction);
    }

    void btnFilterOnAction(ActionEvent event) {
        try {
            WindowFilterController wfc = new WindowFilterController(course.getSubject(), sc);
            JFXDialogLayout content = new JFXDialogLayout();
            FXMLLoader parent = new FXMLLoader (getClass().getResource("../ui/windowFilter.fxml"));
            parent.setController(wfc);
            content.setBody( (Parent) parent.load());
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.RIGHT);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void btnRemoveOnAction(ActionEvent event) {
        course.getSubject().updateCourses();
        User.getSelectedSubjects().remove(course.getSubject());
        Platform.runLater(() -> this.listViewSubjects.getItems().remove(btnRemove.getParent()));
        User.setActiveIndexSchedule(0);
        sc.showDeleteSchedule();
        User.getSelectedSubjects().remove(course.getSubject());
        User.getCurrentCourses().clear();
        DatabaseManager.setSchedule(0);
        DatabaseManager.cantGeneratedSchedules();
        if (User.getCantGeneratedSchedules()==0) {
            sc.setCurrentScheduleText(0, 0);
        }else{
            sc.setCurrentScheduleText(1, User.getCantGeneratedSchedules());
        }
        sc.showAddSchedule();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
        updateInfoCourse();
    }

    public void updateInfoCourse(){
        lblProfessor.setText(course.getProfessor().getFullname());
        lblCapacity.setText("Capacidad: " + course.getTotalStudents());
        lblNrc.setText("NRC: " + course.getNrc());
    }
}
