package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class LoginController {
    private Image image_relleno = new Image("\\assets\\circulo_relleno.png");
    private Image image_rellenont = new Image("\\assets\\circulo_sin_relleno.png");

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane layer2;

    @FXML
    private JFXTextField txtuser;

    @FXML
    private JFXPasswordField psdpass;

    @FXML
    private JFXButton btnlogin;

    @FXML
    private Label lblStatus2;

    @FXML
    private Label lblStatus1;

    @FXML
    private ImageView btnslide1;

    @FXML
    private ImageView slide1;

    @FXML
    private ImageView slide2;

    @FXML
    private ImageView btnslide2;

    @FXML
    void btnslide1Action(MouseEvent event) {

        Platform.runLater(() -> {
            slide1.setVisible(true);
            slide2.setVisible(false);
            btnslide1.setImage(image_relleno);
            btnslide2.setImage(image_rellenont);
        });
    }


    @FXML
    void btnslide2Clicked(MouseEvent event) {
        Platform.runLater(() -> {
            slide2.setVisible(true);
            slide1.setVisible(false);
            btnslide2.setImage(image_relleno);
            btnslide1.setImage(image_rellenont);
        });
    }

    @FXML
    void iniciarSesion(ActionEvent event) {
        Platform.runLater(() -> {
            lblStatus1.setVisible(false);
            lblStatus2.setVisible(false);
            String user = txtuser.getText();
            String pass = psdpass.getText();
            if (user.length() == 0 || pass.length() == 0) {
                lblStatus1.setVisible(true);
            } else {

            }
        });
    }

    @FXML
    void initialize() {
        assert layer2 != null : "fx:id=\"layer2\" was not injected: check your FXML file 'Login.fxml'.";
        assert txtuser != null : "fx:id=\"txtuser\" was not injected: check your FXML file 'Login.fxml'.";
        assert psdpass != null : "fx:id=\"psdpass\" was not injected: check your FXML file 'Login.fxml'.";
        assert btnlogin != null : "fx:id=\"btnlogin\" was not injected: check your FXML file 'Login.fxml'.";
        assert lblStatus2 != null : "fx:id=\"lblStatus2\" was not injected: check your FXML file 'Login.fxml'.";
        assert lblStatus1 != null : "fx:id=\"lblStatus1\" was not injected: check your FXML file 'Login.fxml'.";
        assert btnslide1 != null : "fx:id=\"btnslide1\" was not injected: check your FXML file 'Login.fxml'.";
        assert slide1 != null : "fx:id=\"slide1\" was not injected: check your FXML file 'Login.fxml'.";
        assert slide2 != null : "fx:id=\"slide2\" was not injected: check your FXML file 'Login.fxml'.";
        assert btnslide2 != null : "fx:id=\"btnslide2\" was not injected: check your FXML file 'Login.fxml'.";
    }
}
