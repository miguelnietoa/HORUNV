package controllers.windowfilter;

import com.jfoenix.controls.JFXTreeView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WindowFilterController implements Initializable {

    @FXML
    private JFXTreeView<AnchorPane> treeViewProfessors;
    @FXML
    private Label lblNameCourse;

    private TreeItem<AnchorPane> root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        treeViewProfessors.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        buildSubjectCard();

    }

    private void buildSubjectCard() {
        for (int i = 0; i < 10; i++) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/components/cardProfessor.fxml"));

            try {
                if (i == 0) {
                    root = new TreeItem<>(loader.load());

                    root.setExpanded(true);
                } else {
                    root.getChildren().add(new TreeItem<>(loader.load()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            treeViewProfessors.setRoot(root);
            treeViewProfessors.setShowRoot(false);

        }
        for (int i = 0; i < 5; i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/components/cardCourse.fxml"));
            try {
                root.getChildren().get(0).getChildren().add(new TreeItem<>(loader.load()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}