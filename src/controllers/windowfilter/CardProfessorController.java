package controllers.windowfilter;

import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import model.Course;
import model.Professor;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNameProfessor(professor.getFullname());
        toggleButtonEnable.setOnAction(this::updateEnableToggleButton);
        toggleButtonEnable.setSelected(professor.isEnable());
    }

    public CardProfessorController(Professor professor) {
        this.professor = professor;
        courses = new ArrayList<>();
    }

    public void setNameProfessor(String nameProfessor) {
        lblNameProfessor.setText(nameProfessor);
    }

    void updateEnableToggleButton(ActionEvent event) {
        professor.setEnable(toggleButtonEnable.isSelected());
        for (Course course : professor.getCourses()) {
            if (professor.isEnable()) {
                course.setEnable(true);
            }
        }
        for (AnchorPane c : courses) {
            int t = c.getChildren().size();
            ToggleButton tg = (ToggleButton) c.getChildren().get(t - 1);
            if (professor.isEnable()) {
                tg.setSelected(true);
            }
            tg.setDisable(!professor.isEnable());
        }

    }

    public void addCourse(AnchorPane course) {
        courses.add(course);
    }

    public void setSelectedPofressorFromSon() {
        boolean sw = false;
        int i = 0;
        while (i < courses.size()&& !sw) {
            AnchorPane c=courses.get(i);
            int t = c.getChildren().size();
            ToggleButton tg = (ToggleButton) c.getChildren().get(t - 1);
            if(tg.isSelected()){
                sw=true;
            }
            i++;
        }
        if(!sw){
            toggleButtonEnable.setSelected(false);
            professor.setEnable(false);
        }else{
            toggleButtonEnable.setSelected(true);
            professor.setEnable(true);
        }
    }

}
