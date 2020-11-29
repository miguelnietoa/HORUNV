package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.itextpdf.kernel.pdf.PageLabelNumberingStyle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import database.DatabaseManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import model.Request;
import model.User;

public class CardRequestController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane cardRequestAnchorPane;

    @FXML
    private Label lblName;

    @FXML
    private JFXButton btnDeleteRequest;

    @FXML
    private Label lblDate;

    private Request request;
    private JFXListView<AnchorPane> listview;

    @FXML
    void initialize() {
        btnDeleteRequest.setOnAction(this::deleteRequest);
        this.lblName.setText(request.getFullNameStudent());
        this.lblDate.setText(request.getDateHour()+"");
    }

    public CardRequestController(Request request, JFXListView<AnchorPane> listview) {
        this.request = request;
        this.listview = listview;
    }
    private void deleteRequest(ActionEvent event) {
        DatabaseManager.deleteRequest(this.request);
        Platform.runLater(() -> {this.listview.getItems().remove(this.btnDeleteRequest.getParent());});
        User.getRequests().remove(this.request);
    }
    public Request getRequest() {
        return request;
    }
}