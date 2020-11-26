package controllers.windowfilter;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.Course;
import model.Schedule;

import java.net.URL;
import java.util.ResourceBundle;

public class CardCourseController implements Initializable {

    @FXML
    private Label lblCoursesAvailable;
    @FXML
    private Label lblNRC;
    @FXML
    private JFXListView<Label> listViewSchedules;
    @FXML
    private JFXToggleButton toggleButtonEnable;

    private Course course;
    private CardProfessorController cardProfessorController;

    public CardCourseController(Course course,CardProfessorController cardProfessorController) {
        this.course = course;
        this.cardProfessorController=cardProfessorController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNRC("NRC: "+course.getNrc()+"");
        setSchedule();
        toggleButtonEnable.setSelected(course.isEnable());
        toggleButtonEnable.setOnAction(this::clickEnableToggleButton);
        lblCoursesAvailable.setText("Capacidad: "+course.getTotalStudents());
        if(!course.getProfessor().isEnable()){
            toggleButtonEnable.setDisable(true);
        }
    }

    public void setNRC(String nrc) {
        lblNRC.setText(nrc);
    }

    public void clickEnableToggleButton(ActionEvent event){
        course.setEnable(toggleButtonEnable.isSelected());
        cardProfessorController.setSelectedPofressorFromSon();
    }

    public void setSchedule(){
        for (Schedule schedule : course.getSchedules()) {
            Label l = new Label(schedule.getScheduleInfo());
            listViewSchedules.getItems().add(l);
        }
    }

}
