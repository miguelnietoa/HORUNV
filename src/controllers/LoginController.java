package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private final Image filledCircle = new Image("\\assets\\filled_circle.png");
    private final Image unfilledCircle = new Image("\\assets\\unfilled_circle.png");

    @FXML
    private JFXTextField txtUsername;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXButton btnLogin;

    @FXML
    private Label lblError;

    @FXML
    private AnchorPane paneSlide;

    @FXML
    private ImageView slide1;

    @FXML
    private ImageView slide2;

    @FXML
    private ImageView btnSlide2;

    @FXML
    private ImageView btnSlide1;

    @FXML
    private void btnSlide1OnAction() {
        Platform.runLater(() -> {
            slide1.setVisible(true);
            slide2.setVisible(false);
            btnSlide1.setImage(filledCircle);
            btnSlide2.setImage(unfilledCircle);
        });
    }


    @FXML
    private void btnSlide2OnAction() {
        Platform.runLater(() -> {
            slide2.setVisible(true);
            slide1.setVisible(false);
            btnSlide2.setImage(filledCircle);
            btnSlide1.setImage(unfilledCircle);
        });
    }

    @FXML
    void btnLoginOnAction() {
        Platform.runLater(() -> {
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                lblError.setText("Debes ingresar un usuario y contraseña");
                lblError.setVisible(true);
            } else {
                Platform.runLater(() -> {
                    Stage newStage = new Stage();
                    Parent root;
                    try {
                        root = FXMLLoader.load(getClass().getResource("../ui/schedule.fxml"));
                        newStage.setTitle("HORUNV - Arma tu horario");
                        newStage.setScene(new Scene(root));
                        newStage.sizeToScene();
                        newStage.show();
                        newStage.setMinWidth(newStage.getWidth());
                        newStage.setMinHeight(newStage.getHeight());
                        Stage stage1 = (Stage) btnLogin.getScene().getWindow();
                        stage1.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                // if incorrect credentials
                //lblError.setText("Usuario o contraseña incorrecta");
                //lblError.setVisible(true);
            }
        });
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblError.setVisible(false);
        slide1.fitWidthProperty().bind(paneSlide.widthProperty());
        slide1.fitHeightProperty().bind(paneSlide.heightProperty());
        slide2.fitWidthProperty().bind(paneSlide.widthProperty());
        slide2.fitHeightProperty().bind(paneSlide.heightProperty());
    }
}
