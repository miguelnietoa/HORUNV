package controllers.windowfilter;

import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CardProfessorController implements Initializable {

    @FXML
    public Label lblNameProfessor;
    @FXML
    public JFXToggleButton toggleButtonEnable;

    private String nameProfessor;
    private boolean enableToggleButton;
    ArrayList<AnchorPane> courses;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setNameProfessor(nameProfessor);
        toggleButtonEnable.setOnAction(this::updateEnableToggleButton);
    }

    public CardProfessorController(String nameProfessor, boolean enableToggleButton) {
        this.nameProfessor = nameProfessor;
        this.enableToggleButton = enableToggleButton;
        courses = new ArrayList<>();
    }

    public void setNameProfessor(String nameProfessor) {
        lblNameProfessor.setText(nameProfessor);
    }

    void updateEnableToggleButton(ActionEvent event) {
        enableToggleButton = toggleButtonEnable.isSelected();
        for (AnchorPane c : courses) {
            int t = c.getChildren().size();
            ToggleButton tg=(ToggleButton)c.getChildren().get(t - 1);
            tg.setDisable(!enableToggleButton);
        }
    }

    public void addCourse(AnchorPane course) {
        courses.add(course);
    }

}
