package controllers;

import com.jfoenix.controls.JFXListView;
import database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

    private ScheduleController sc;

    public CardSubjectProjectionController(Subject subject, JFXListView<AnchorPane> listViewSubjects,
                                           StackPane stackPane, ScheduleController sc) {
        this.subject = subject;
        this.listViewSubjects = listViewSubjects;
        this.stackPane = stackPane;
        this.sc = sc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblSubjectName.setText(subject.getName());
        lblInfo.setText(subject.getCode() + " | " + subject.getCredits() + " créditos");
        btnAdd.setOnAction(this::btnAddOnAction);
        btnAdd.setVisible(!User.getSelectedSubjects().contains(subject));

    }

    void btnAddOnAction(ActionEvent event) {
        if (User.getCantGeneratedSchedules() == 0 && User.getSelectedSubjects().size() > 0)
            return;

        User.addSelectedSubject(subject);
        int activeIndexOlder = User.getActiveIndexSchedule();
        User.setActiveIndexSchedule(0);
        int cantGeneratedOlder = User.getCantGeneratedSchedules();
        DatabaseManager.cantGeneratedSchedules();
        if (User.getCantGeneratedSchedules() != 0) {
            btnAdd.setVisible(false);
            sc.showDeleteSchedule();
            DatabaseManager.setSchedule(0);
            sc.setCurrentScheduleText(1, User.getCantGeneratedSchedules());

            Course newCourse = null;
            for (Course course : User.getCurrentCourses()) {
                if (course.getSubject().equals(subject)) {
                    newCourse = course;
                    break;
                }
            }
            CardActiveCourseController c = new CardActiveCourseController(newCourse, /*course,*/ listViewSubjects, stackPane, sc);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/components/cardActiveCourse.fxml"));
            loader.setController(c);
            try {
                listViewSubjects.getItems().add(loader.load());
                sc.showAddSchedule();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            sc.showMessage("No hay posibles horarios que cumplan con los filtros, no puedes añadir esta materia.\n" +
                    "Intenta eliminar algunos filtros.");
            User.setCantGeneratedSchedules(cantGeneratedOlder);
            User.setActiveIndexSchedule(activeIndexOlder);
            User.getSelectedSubjects().remove(subject);
        }
    }

}
