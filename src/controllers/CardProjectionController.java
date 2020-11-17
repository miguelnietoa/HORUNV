package controllers;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CardProjectionController implements Initializable {

    @FXML
    private JFXListView<AnchorPane> listViewProjection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addCardSubjectProjections();
    }

    public void buildCardSubjectProjections() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/components/cardSubjectProjection.fxml"));

        try {
            listViewProjection.getItems().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCardSubjectProjections() {
        for (int i = 0; i < 10; i++) {
            buildCardSubjectProjections();
        }
    }
}
