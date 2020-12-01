package controllers.windowfilter;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import controllers.ScheduleController;
import database.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import model.Course;
import model.Schedule;
import model.User;

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

    private ScheduleController sc;

    private Course course;
    private CardProfessorController cardProfessorController;

    public CardCourseController(Course course,CardProfessorController cardProfessorController, ScheduleController sc) {
        this.course = course;
        this.cardProfessorController=cardProfessorController;
        this.sc = sc;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNRC("NRC: "+course.getNrc()+"");
        setSchedule();
        toggleButtonEnable.setSelected(course.isEnable());
        toggleButtonEnable.setOnAction(this::clickEnableToggleButton);
        lblCoursesAvailable.setText("Capacidad: "+course.getTotalStudents());
        if(!course.getProfessor().isEnable()){
            toggleButtonEnable.setSelected(false);
            course.setEnable(false);
        }
    }

    public void setNRC(String nrc) {
        lblNRC.setText(nrc);
    }

    public void clickEnableToggleButton(ActionEvent event){
        course.setEnable(toggleButtonEnable.isSelected());
        cardProfessorController.setSelectedPofressorFromSon();
        sc.showDeleteSchedule();
        User.setActiveIndexSchedule(0);
        DatabaseManager.setSchedule(0);
        DatabaseManager.cantGeneratedSchedules();
        sc.showAddSchedule();
        if (User.getCantGeneratedSchedules() == 0){
            sc.setCurrentScheduleText(0, User.getCantGeneratedSchedules());
        }else{
            sc.setCurrentScheduleText(1, User.getCantGeneratedSchedules());
        }
        sc.setCurrentCourseInfo();
    }

    public void setSchedule(){
        for (Schedule schedule : course.getSchedules()) {
            Label l = new Label(schedule.getScheduleInfo());
            listViewSchedules.getItems().add(l);
        }
    }

}
