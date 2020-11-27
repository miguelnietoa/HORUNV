package controllers;


import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
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
import model.Subject;
import model.User;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
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

    @FXML
    private Label currentSchedule;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User.setProjection(DatabaseManager.getProjection(User.getCodeUser()));
        Tooltip.install(btnProjection, new Tooltip("Ver proyección"));
        Tooltip.install(btnSave, new Tooltip("Guardar horario"));
        Tooltip.install(btnCompare, new Tooltip("Comparar horarios"));
        Tooltip.install(btnPdf, new Tooltip("Descargar PDF"));
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
            ProjectionController c = new ProjectionController(listViewSubjects, stackPane, this);
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
        DatabaseManager.addSavedSchedule();

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
        changeSchedule(-1);
    }

    @FXML
    void btnRightMouseClicked(MouseEvent mouseEvent) {
        changeSchedule(1);
    }

    @FXML
    void btnRightMouseEntered(MouseEvent event) {
        btnRight.setImage(rightOn);
    }

    @FXML
    void btnRightMouseExited(MouseEvent event) {
        btnRight.setImage(rightOff);
    }

    private void changeSchedule(int validMov) {
        int i = User.getActiveIndexSchedule();
        i = i + validMov;
        if (i >= 0 && i < User.getCantGeneratedSchedules()) {
            User.setActiveIndexSchedule(i);
            this.showDeleteSchedule();
            DatabaseManager.setSchedule(i);
            this.showAddSchedule();
            this.setCurrentScheduleText(i + 1, User.getCantGeneratedSchedules());
            setCurrentCourseInfo();
        }
    }

    public void setCurrentCourseInfo() {
        for (int j = 0; j < User.getCurrentCourses().size(); j++) {
            AnchorPane item = listViewSubjects.getItems().get(j);
            CardActiveCourseController c = (CardActiveCourseController) item.getUserData();
            c.setCourse(User.getCurrentCourses().get(j));
        }
    }

    private <T> void columnCells(TableColumn<HourRow, T> column) {

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
            LinkedList<Object[]> filters = new LinkedList<>();
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
                    columnCells(tableView.getColumns().get(col));
                }

                item.setFromIndex(col, "");
                //System.out.println(tableView.getSelectionModel().getSelectedCells().get(0));
                //System.out.println("x=" + row + "y=" + col);
                //String dispLastName = (String) pos.getTableColumn().getCellObservableValue(item).getValue();

                // tableView.getItems().set(row,item);

                int start = (row + 6) * 100 + 30;
                int end = start + 100;
                String day = null;
                switch (col) {
                    case 1:
                        day = "L";
                        break;
                    case 2:
                        day = "M";
                        break;
                    case 3:
                        day = "X";
                        break;
                    case 4:
                        day = "J";
                        break;
                    case 5:
                        day = "V";
                        break;
                    case 6:
                        day = "S";
                        break;
                }
                filters.add(new Object[]{start, end, day});
            }
            User.setFilters(filters);
            User.setActiveIndexSchedule(0);
            showDeleteSchedule();
            DatabaseManager.setSchedule(0);
            DatabaseManager.cantGeneratedSchedules();
            int a = User.getCantGeneratedSchedules();
            this.setCurrentScheduleText(User.getCantGeneratedSchedules() == 0 ? 0 : 1, User.getCantGeneratedSchedules());
            showAddSchedule();
            setCurrentCourseInfo();
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

        CardActiveCourseController c = new CardActiveCourseController(course, listViewSubjects, stackPane, this);

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
                showDeleteSchedule();
                DatabaseManager.setSchedule(0);
                DatabaseManager.cantGeneratedSchedules();
                this.setCurrentScheduleText(1, User.getCantGeneratedSchedules());
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
        //showDeleteSchedule();
        LinkedList<Course> currentCourses = User.getCurrentCourses();
        for (Course c : currentCourses) {
            LinkedList<Schedule> schedules = c.getSchedules();
            for (Schedule s : schedules) {
                for (int[] index : s.getIndices()) {
                    HourRow item = tableView.getItems().get(index[0]);
                    String val = item.getFromIndex(index[1]);
                    if (!val.isEmpty()) {
                        item.setFromIndex(index[1], val + "\n" + c.getSubject().getCode());
                    } else {
                        item.setFromIndex(index[1], c.getSubject().getCode());
                    }
                }
            }
        }
    }

    public void showDeleteSchedule() {
        for (int i = 0; i < tableView.getColumns().size(); i++) {
            columnCells(tableView.getColumns().get(i));
        }
        LinkedList<Course> currentCourses = User.getCurrentCourses();
        for (Course c : currentCourses) {
            LinkedList<Schedule> schedules = c.getSchedules();
            for (Schedule s : schedules) {
                for (int[] index : s.getIndices()) {
                    HourRow item = tableView.getItems().get(index[0]);
                    item.setFromIndex(index[1], "");
                }
            }
        }
    }

    public void savePDF(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos pdf (*.pdf)", "*.pdf"));
        File f = fileChooser.showSaveDialog(null);

        if (f != null) {
            String dest = f.getAbsolutePath();
            final String imagepath = "./tempfiles/temp.png";
            File tempImage = new File(imagepath);
            System.out.println(dest);
            if (tempImage != null) {
                try {
                    WritableImage writableImage = new WritableImage((int) tableView.getWidth(), (int) tableView.getHeight());
                    tableView.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", tempImage);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            PdfWriter writer = null;
            try {
                writer = new PdfWriter(dest);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            ImageData data = null;
            try {
                data = ImageDataFactory.create(imagepath);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(data);
            document.add(image);

            for (Course c : User.getCurrentCourses()) {
                Paragraph p = new Paragraph(
                        c.getSubject().getName() + " - " + c.getSubject().getCode() + ":\n  " +
                                c.getNrc() + " | " + c.getProfessor().getFullname() + "\n"
                );
                document.add(p);
            }
            document.close();
        }
    }

    public void setCurrentScheduleText(int start, int total) {
        this.currentSchedule.setText(start + "/" + total);
    }

    public void showMessage(String message) {
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Text("Advertencia"));
        layout.setBody(new Text(message));
        JFXButton button = new JFXButton("Okay");
        JFXDialog dialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.BOTTOM);
        button.setOnAction(event1 -> dialog.close());
        button.setStyle("-fx-background-color: #FF533D");
        layout.setActions(button);
        dialog.setContent(layout);
        dialog.show();
    }
}


