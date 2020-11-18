package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import database.DatabaseManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Student;

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
        new Thread() {
            @Override
            public void run() {
                super.run();
                String username = txtUsername.getText().trim();
                String password = txtPassword.getText().trim();
                if (username.isEmpty() || password.isEmpty()) {
                    Platform.runLater(() -> {
                        lblError.setText("Debes ingresar un usuario y contraseña");
                        lblError.setVisible(true);
                    });
                } else {
                    if (DatabaseManager.isLogged(username, password)) {
                        System.out.println(Student.getCodeUser());
                        System.out.println(Student.getFullname());
                        System.out.println(Student.getGender());
                        System.out.println(Student.getIdPlan());
                        System.out.println(Student.getPeriod());
                        // Show Schedule window
                        Platform.runLater(() -> {
                            Stage schedule = new Stage();
                            Parent root;
                            Stage login = (Stage) btnLogin.getScene().getWindow();
                            try {
                                root = FXMLLoader.load(getClass().getResource("../ui/schedule.fxml"));
                                schedule.setTitle("HORUNV - Arma tu horario");
                                Scene scene = new Scene(root, login.getWidth(), login.getHeight());
                                scene.getStylesheets().add(getClass().getResource("/ui/styles/application.css").toExternalForm());
                                schedule.setScene(scene);
                                schedule.sizeToScene();
                                schedule.show();
                                schedule.setMinWidth(schedule.getWidth());
                                schedule.setMinHeight(schedule.getHeight());
                                login.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    } else {
                        Platform.runLater(() -> {
                            lblError.setText("Usuario o contraseña incorrecta");
                            lblError.setVisible(true);
                        });
                    }
                }
            }
        }.start();


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
