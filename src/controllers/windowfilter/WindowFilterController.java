package controllers.windowfilter;

import com.jfoenix.controls.JFXTreeView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import model.Course;
import model.Professor;
import model.Subject;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WindowFilterController implements Initializable {

    @FXML
    private JFXTreeView<AnchorPane> treeViewProfessors;
    @FXML
    private Label lblNameCourse;

    private TreeItem<AnchorPane> root;

    private Subject subject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        treeViewProfessors.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lblNameCourse.setText(subject.getName()+"\n"+subject.getCode());
        buildSubjectCard();
    }

    public WindowFilterController(Subject subject) {
        this.subject = subject;
    }

    private void buildSubjectCard() {
        root = new TreeItem<>(new AnchorPane());
        root.setExpanded(true);
        for (Professor professor : subject.getProfessors()) {
            CardProfessorController c = new CardProfessorController(professor);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/components/cardProfessor.fxml"));
            loader.setController(c);
            try {
                TreeItem<AnchorPane> treeItem = new TreeItem<>(loader.load());
                root.getChildren().add(treeItem);
                for (Course course : professor.getCourses()) {
                    if (course.getSubject() == subject) {
                        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/ui/components/cardCourse.fxml"));
                        CardCourseController c1= new CardCourseController(course,c);
                        loader1.setController(c1);
                        try {
                            AnchorPane course1 = loader1.load();
                            treeItem.getChildren().add(new TreeItem<AnchorPane>(course1));
                            c.addCourse(course1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            treeViewProfessors.setRoot(root);
            treeViewProfessors.setShowRoot(false);
        }
    }
}