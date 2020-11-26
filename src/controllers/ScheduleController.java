package controllers;

import com.jfoenix.controls.*;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import controllers.tablemodel.DragSelectionCellFactory;
import controllers.tablemodel.HourRow;
import database.DatabaseManager;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Course;
import model.Schedule;
import model.User;
import model.Subject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ScheduleController implements Initializable {

    private final Image leftOn = new Image("/assets/left-arrow-hover.png");
    private final Image leftOff = new Image("/assets/left-arrow.png");
    private final Image rightOn = new Image("/assets/right-arrow-hover.png");
    private final Image rightOff = new Image("/assets/right-arrow.png");

    @FXML
    private StackPane stackPane;

    @FXML
    private TableView<HourRow> tableView;

    @FXML
    private TableColumn<HourRow, String> hourID;

    @FXML
    private TableColumn<HourRow, String> mondayID;

    @FXML
    private TableColumn<HourRow, String> tuesdayID;

    @FXML
    private TableColumn<HourRow, String> wednesdayID;

    @FXML
    private TableColumn<HourRow, String> thursdayID;

    @FXML
    private TableColumn<HourRow, String> fridayID;

    @FXML
    private TableColumn<HourRow, String> saturdayID;

    @FXML
    private JFXTextField textFieldSearch;

    @FXML
    private JFXListView<AnchorPane> listViewSubjects;

    @FXML
    private JFXButton btnProjection;

    @FXML
    private ImageView btnLeft;

    @FXML
    private ImageView btnRight;

    @FXML
    private JFXButton btnNotifications;

    @FXML
    private JFXButton btnLogOut;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnCompare;

    @FXML
    private JFXButton btnPdf;

    @FXML
    private Label lblFullname;

    @FXML
    private ImageView imageAvatar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User.setProjection(DatabaseManager.getProjection(User.getCodeUser()));
        Tooltip.install(btnProjection, new Tooltip("Ver proyección"));
        Tooltip.install(btnSave, new Tooltip("Guardar horario"));
        Tooltip.install(btnCompare, new Tooltip("Comparar horarios"));
        Tooltip.install(btnPdf, new Tooltip("Descargar Imagen"));
        buildAutoCompleteTextField();
        buildTableView();
        lblFullname.setText(User.getFullname());
        if (User.getGender() == 'M')
            imageAvatar.setImage(new Image("\\assets\\man.png"));
        else
            imageAvatar.setImage(new Image("\\assets\\woman.png"));

    }

    @FXML
    void btnProjectionOnAction(ActionEvent event) {
        try {
            JFXDialogLayout contentProjection = new JFXDialogLayout();
            ProjectionController c = new ProjectionController(listViewSubjects, stackPane,this);
            FXMLLoader parent = new FXMLLoader(getClass().getResource("/ui/components/projection.fxml"));
            parent.setController(c);
            contentProjection.setBody((Parent) parent.load());
            contentProjection.setHeading(new Text("Proyección"));
            JFXDialog dialog = new JFXDialog(stackPane, contentProjection, JFXDialog.DialogTransition.BOTTOM);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        System.out.println("ali");
    }

    @FXML
    void btnNotificationsOnAction(ActionEvent event) {
        try {
            JFXDialogLayout content = new JFXDialogLayout();
            Parent parent = FXMLLoader.load(getClass().getResource("/ui/components/cardNotifications.fxml"));
            content.setBody(parent);
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.BOTTOM);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {
        Stage login = new Stage();
        Parent root;
        Stage schedule = (Stage) btnLogOut.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getResource("../ui/Login.fxml"));
            login.setTitle("HORUNV - Login");
            Scene scene = new Scene(root, schedule.getWidth(), schedule.getHeight());
            scene.getStylesheets().add(getClass().getResource("/ui/styles/application.css").toExternalForm());
            login.setScene(scene);
            login.sizeToScene();
            login.show();
            login.setMinWidth(schedule.getWidth());
            login.setMinHeight(schedule.getHeight());
            schedule.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnLeftMouseEntered(MouseEvent event) {
        btnLeft.setImage(leftOn);
    }

    @FXML
    void btnLeftMouseExited(MouseEvent event) {
        btnLeft.setImage(leftOff);
    }

    @FXML
    void btnLeftMouseClicked(MouseEvent mouseEvent) {

    }

    @FXML
    void btnRightMouseClicked(MouseEvent mouseEvent) {
    }

    @FXML
    void btnRightMouseEntered(MouseEvent event) {
        btnRight.setImage(rightOn);
    }

    @FXML
    void btnRightMouseExited(MouseEvent event) {
        btnRight.setImage(rightOff);
    }


    private <T> void columnCells(TableColumn<HourRow, T> column, int col) {

        Callback<TableColumn<HourRow, T>, TableCell<HourRow, T>> existingCellFactory
                = column.getCellFactory();
        column.setCellFactory(c -> {
            TableCell<HourRow, T> cell = existingCellFactory.call(c);
            return cell;
        });

    }

    private void buildTableView() {
        tableView.widthProperty().addListener((source, oldWidth, newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((observable, oldValue, newValue) -> header.setReordering(false));
        });

        hourID.setCellValueFactory(new PropertyValueFactory<>("hour"));
        mondayID.setCellValueFactory(new PropertyValueFactory<>("monday"));
        tuesdayID.setCellValueFactory(new PropertyValueFactory<>("tuesday"));
        wednesdayID.setCellValueFactory(new PropertyValueFactory<>("wednesday"));
        thursdayID.setCellValueFactory(new PropertyValueFactory<>("thursday"));
        fridayID.setCellValueFactory(new PropertyValueFactory<>("friday"));
        saturdayID.setCellValueFactory(new PropertyValueFactory<>("saturday"));
        tableView.heightProperty().addListener((obs, oldVal, newVal) ->
                tableView.setFixedCellSize((newVal.doubleValue() / 15)));

        for (int i = 6; i < 20; i++) {
            //tableView.getItems().add(new HourRow(i + ":30 - " + (i + 1) + ":30", "", "", "", "", "", ""));
            tableView.getItems().add(new HourRow(i + ":30 - " + (i + 1) + ":30", "", "", "", "", "", ""));
        }

        tableView.getSelectionModel().getSelectedCells().addListener((ListChangeListener<? super TablePosition>) c -> {

            for (TablePosition pos : c.getList()) {
                int row = pos.getRow();
                int col = pos.getColumn();

                HourRow item = tableView.getItems().get(row);

                if (col == 0) {

                    Platform.runLater(() -> {

                        tableView.getSelectionModel().clearSelection(row, hourID);
                    });
                    break;
                } else {
                    columnCells(tableView.getColumns().get(col), col);
                }

                item.setFromIndex(col, "");
                //System.out.println(tableView.getSelectionModel().getSelectedCells().get(0));
                //System.out.println("x=" + row + "y=" + col);
                //String dispLastName = (String) pos.getTableColumn().getCellObservableValue(item).getValue();

                // tableView.getItems().set(row,item);

            }
        });

        final Callback<TableColumn<HourRow, String>, TableCell<HourRow, String>> cellFactory = new DragSelectionCellFactory();
        // hourID.setCellFactory(cellFactory);
        mondayID.setCellFactory(cellFactory);
        tuesdayID.setCellFactory(cellFactory);
        wednesdayID.setCellFactory(cellFactory);
        thursdayID.setCellFactory(cellFactory);
        fridayID.setCellFactory(cellFactory);
        saturdayID.setCellFactory(cellFactory);


        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    private void buildSubjectCard(Course course) {

        CardActiveCourseController c = new CardActiveCourseController(course, listViewSubjects, stackPane,this);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/components/cardActiveCourse.fxml"));
        loader.setController(c);
        try {
            listViewSubjects.getItems().add(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildAutoCompleteTextField() {
        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.getSuggestions().addAll(
                User.getProjection().values().stream().map(
                        subject -> subject.getName() + " (" + subject.getCode() + ")"
                ).collect(Collectors.toList())
        );

        // detect selection
        autoCompletePopup.setSelectionHandler(event -> {
            String codeSubject = event.getObject().split("\\(")[1].substring(0, 7);
            Subject subject = User.getProjection().get(codeSubject);
            if (!User.getSelectedSubjects().contains(subject)) {
                User.addSelectedSubject(subject);
                User.setActiveIndexSchedule(0);
                DatabaseManager.setSchedule(0);
                Course newCourse = null;
                for (Course course : User.getCurrentCourses()) {
                    if (course.getSubject().equals(subject)) {
                        newCourse = course;
                        break;
                    }
                }
                buildSubjectCard(newCourse);
                showAddSchedule();
            } else {
                JFXDialogLayout layout = new JFXDialogLayout();
                layout.setHeading(new Text("Advertencia"));
                layout.setBody(new Text("Esta asignatura ya ha sido añadida."));
                JFXButton button = new JFXButton("Okay");
                JFXDialog dialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.BOTTOM);
                button.setOnAction(event1 -> dialog.close());
                button.setStyle("-fx-background-color: #FF533D");
                layout.setActions(button);
                dialog.setContent(layout);
                dialog.show();
            }
            textFieldSearch.setText("");
        });

        // filtering options
        textFieldSearch.textProperty().addListener(observable -> {
            autoCompletePopup.filter(string -> string.toLowerCase().contains(textFieldSearch.getText().toLowerCase()));
            if (autoCompletePopup.getFilteredSuggestions().isEmpty() || textFieldSearch.getText().trim().isEmpty()) {
                autoCompletePopup.hide();
                // if you remove textField.getText.trim().isEmpty() when text field is empty it suggests all options
                // so you can choose YEH
            } else {
                autoCompletePopup.show(textFieldSearch);
            }
        });
    }

    public void showAddSchedule() {
        showDeleteSchedule();
        LinkedList<Course> currentCourses = User.getCurrentCourses();
        for (Course c : currentCourses) {
            LinkedList<Schedule> schedules = c.getSchedules();
            for (Schedule s : schedules) {
                for (int[] index : s.getIndices()) {
                    HourRow item = tableView.getItems().get(index[0]);
                    String val=item.getFromIndex(index[1]);
                    if(!val.isEmpty()) {
                        item.setFromIndex(index[1], val+"\n"+c.getSubject().getCode());
                    }else{
                        item.setFromIndex(index[1], c.getSubject().getCode());
                    }
                }
            }
        }
    }

    public void showDeleteSchedule() {
        LinkedList<Course> currentCourses = User.getCurrentCourses();
        for (Course c : currentCourses) {
            LinkedList<Schedule> schedules = c.getSchedules();
            for (Schedule s : schedules) {
                for (int[] index : s.getIndices()) {
                    HourRow item = tableView.getItems().get(index[0]);
                    String val=item.getFromIndex(index[0]);
                    item.setFromIndex(index[1],"");
                }
            }
        }
    }

    public void savePDF(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de imagen (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage((int) tableView.getWidth(), (int) tableView.getHeight());
                tableView.snapshot(null, writableImage);
                RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                ImageIO.write(renderedImage, "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}


