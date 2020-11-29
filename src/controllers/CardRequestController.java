package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.itextpdf.kernel.pdf.PageLabelNumberingStyle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.Request;

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
    private ImageView btnDeleteRequest;

    @FXML
    private Label lblDate;

    @FXML
    void btnDeleteRequest(MouseEvent event) {

    }

    private Request request;

    public CardRequestController(Request request) {
        this.request = request;
        Platform.runLater(() -> {
            this.lblName.setText(request.getFullNameStudent());
            this.lblDate.setText(request.getDateHour()+"");
        });
    }

    @FXML
    void initialize() {
        assert cardRequestAnchorPane != null : "fx:id=\"cardRequestAnchorPane\" was not injected: check your FXML file 'cardRequest.fxml'.";
        assert lblName != null : "fx:id=\"lblName\" was not injected: check your FXML file 'cardRequest.fxml'.";
        assert btnDeleteRequest != null : "fx:id=\"btnDeleteRequest\" was not injected: check your FXML file 'cardRequest.fxml'.";
        assert lblDate != null : "fx:id=\"lblDate\" was not injected: check your FXML file 'cardRequest.fxml'.";

    }

    public Request getRequest() {
        return request;
    }
}