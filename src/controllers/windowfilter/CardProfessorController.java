package controllers.windowfilter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXToggleButton;
import controllers.ScheduleController;
import database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.*;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CardProfessorController implements Initializable {

    @FXML
    public Label lblNameProfessor;
    @FXML
    public JFXToggleButton toggleButtonEnable;
    Professor professor;
    ArrayList<AnchorPane> courses;
    private ScheduleController sc;
    private Subject subject;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNameProfessor(professor.getFullname());
        toggleButtonEnable.setOnAction(this::updateEnableToggleButton);
        toggleButtonEnable.setSelected(professor.isEnable());
    }

    public CardProfessorController(Professor professor, ScheduleController sc, Subject sb) {
        this.professor = professor;
        courses = new ArrayList<>();
        this.sc = sc;
        this.subject = sb;
    }

    public void setNameProfessor(String nameProfessor) {
        lblNameProfessor.setText(nameProfessor);
    }

    void updateEnableToggleButton(ActionEvent event) {
        updateProfessorState();
        checkAllProfesors();
    }

    public void addCourse(AnchorPane course) {
        courses.add(course);
    }

    public void setSelectedPofressorFromSon() {
        boolean sw = false;
        int i = 0;
        while (i < courses.size() && !sw) {
            AnchorPane c = courses.get(i);
            int t = c.getChildren().size();
            ToggleButton tg = (ToggleButton) c.getChildren().get(t - 1);
            if (tg.isSelected()) {
                sw = true;
            }
            i++;
        }
        if (!sw) {
            toggleButtonEnable.setSelected(false);
            professor.setEnable(false);
        } else {
            toggleButtonEnable.setSelected(true);
        }
        checkAllProfesors();
    }

    private void updateProfessorState(){
        professor.setEnable(toggleButtonEnable.isSelected());
        for (Course course : professor.getCourses()) {
            course.setEnable(professor.isEnable());
        }
        for (AnchorPane c : courses) {
            int t = c.getChildren().size();
            ToggleButton tg = (ToggleButton) c.getChildren().get(t - 1);
            tg.setSelected(professor.isEnable());
        }
    }
    private void checkAllProfesors(){
        boolean sw = false;
        for (Professor professor : subject.getProfessors()) {
            if (professor.isEnable()){
                sw = true;
                break;
            }
        }
        if (sw){
            sc.showDeleteSchedule();
            User.setActiveIndexSchedule(0);
            DatabaseManager.setSchedule(0);
            DatabaseManager.cantGeneratedSchedules();
            sc.showAddSchedule();
            if (User.getCantGeneratedSchedules() == 0){
                sc.setCurrentScheduleText(0, 0);
            }else{
                sc.setCurrentScheduleText(1, User.getCantGeneratedSchedules());
            }
            sc.setCurrentCourseInfo();
        }else{
            sc.showMessage("No puedes bloquear a todos los profesores!");
            toggleButtonEnable.setSelected(true);
            updateProfessorState();
        }
    }
}
