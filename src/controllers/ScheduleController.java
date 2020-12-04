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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Course;
import model.Schedule;
import model.Subject;
import model.User;

import javax.imageio.ImageIO;
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

    private final Image leftOn = new Image(getClass().getClassLoader().getResourceAsStream("assets/left-arrow-hover.png"));
    private final Image leftOff = new Image(getClass().getClassLoader().getResourceAsStream("assets/left-arrow.png"));
    private final Image rightOn = new Image(getClass().getClassLoader().getResourceAsStream("assets/right-arrow-hover.png"));
    private final Image rightOff = new Image(getClass().getClassLoader().getResourceAsStream("assets/right-arrow.png"));
    private boolean column0 = false;

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
            imageAvatar.setImage(new Image(getClass().getClassLoader().getResourceAsStream("assets/man.png")));
        else
            imageAvatar.setImage(new Image(getClass().getClassLoader().getResourceAsStream("assets/woman.png")));

    }

    @FXML
    void btnProjectionOnAction(ActionEvent event) {
        try {
            JFXDialogLayout contentProjection = new JFXDialogLayout();
            ProjectionController c = new ProjectionController(listViewSubjects, stackPane, this);
            FXMLLoader parent = new FXMLLoader(getClass().getClassLoader().getResource("ui/components/projection.fxml"));
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
        showNameSaveSchedule();
    }

    @FXML
    void btnCompareOnAction(ActionEvent event) {
        Stage compareSchedules = new Stage();
        compareSchedules.initModality(Modality.APPLICATION_MODAL);
        Parent root;
        Stage schedule = (Stage) btnCompare.getScene().getWindow();
        double w = schedule.getWidth();
        double h = schedule.getHeight();
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/storeSchedules.fxml"));
            compareSchedules.setTitle("HORUNV - Almacén de Horarios");
            Scene scene = new Scene(root, w, h);
            scene.getStylesheets().add(getClass().getClassLoader().getResource("ui/styles/application.css").toExternalForm());
            compareSchedules.setScene(scene);
            compareSchedules.sizeToScene();
            compareSchedules.showAndWait();
            compareSchedules.setMinWidth(w);
            compareSchedules.setMinHeight(h);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnNotificationsOnAction(ActionEvent event) {
        try {
            JFXDialogLayout contentProjection = new JFXDialogLayout();
            CardNotificationsController c = new CardNotificationsController(this);
            FXMLLoader parent = new FXMLLoader(getClass().getClassLoader().getResource("ui/components/cardNotifications.fxml"));
            parent.setController(c);
            contentProjection.setBody((Parent) parent.load());
            contentProjection.setHeading(new Text("Notificaciones"));
            JFXDialog dialog = new JFXDialog(stackPane, contentProjection, JFXDialog.DialogTransition.BOTTOM);
            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) {
        // close and clear
        User.setUsername(null);
        User.setCodeUser(0);
        User.setFullname(null);
        User.setIdPlan(0);
        User.setPeriod(null);
        User.getProjection().clear();
        User.getSelectedSubjects().clear();
        User.getCurrentCourses().clear();
        User.getRequests().clear();
        User.getPossibleSchedules().clear();
        User.setActiveIndexSchedule(0);
        User.setCantGeneratedSchedules(0);
        User.getFilters().clear();
        // show login
        Stage login = new Stage();
        Parent root;
        Stage schedule = (Stage) btnLogOut.getScene().getWindow();
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("ui/Login.fxml"));
            login.setTitle("HORUNV - Login");
            Scene scene = new Scene(root, schedule.getWidth(), schedule.getHeight());
            scene.getStylesheets().add(getClass().getClassLoader().getResource("ui/styles/application.css").toExternalForm());
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

    public static <T> void columnCells(TableColumn<HourRow, T> column) {

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
            tableView.getItems().add(new HourRow(i + ":30 - " + (i + 1) + ":30", "", "", "", "", "", ""));
        }

        tableView.getSelectionModel().getSelectedCells().addListener((ListChangeListener<? super TablePosition>) c -> {

            LinkedList<Object[]> filters = new LinkedList<>();
            for (TablePosition pos : c.getList()) {
                int row = pos.getRow();
                int col = pos.getColumn();

                HourRow item = tableView.getItems().get(row);

                if (col == 0) {
                    column0 = true;
                    Platform.runLater(() -> {
                        tableView.getSelectionModel().clearSelection(row, hourID);
                    });
                    break;
                } else {
                    column0 = false;
                    columnCells(tableView.getColumns().get(col));
                }

                item.setFromIndex(col, "");
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
            if (a == 0 && !column0 && !User.getSelectedSubjects().isEmpty()) {
                boolean sw = false;
                for (Subject selectedSubject : User.getSelectedSubjects()) {
                    if (!selectedSubject.getPrerequisites().isEmpty()) {
                        sw = true;
                    }
                }
                if (sw) {
                    Platform.runLater(() -> {
                        showMessage("Advertencia ", "El bloqueo de horas elimina una materia que" +
                                " es prerrequisito para tu siguiente semestre.\nIntenta eliminar algunos filtros.");
                    });
                } else {
                    Platform.runLater(() -> {
                        showMessage("Advertencia ", "El bloqueo de horas no genera ningún horario." +
                                "\nIntenta eliminar algunos filtros.");
                    });
                }
            }

            this.setCurrentScheduleText(User.getCantGeneratedSchedules() == 0 ? 0 : 1, User.getCantGeneratedSchedules());
            showAddSchedule();
            setCurrentCourseInfo();
        });

        final Callback<TableColumn<HourRow, String>, TableCell<HourRow, String>> cellFactory = new DragSelectionCellFactory();

        mondayID.setCellFactory(cellFactory);
        tuesdayID.setCellFactory(cellFactory);
        wednesdayID.setCellFactory(cellFactory);
        thursdayID.setCellFactory(cellFactory);
        fridayID.setCellFactory(cellFactory);
        saturdayID.setCellFactory(cellFactory);


        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    public void buildSubjectCard(Course course) {
        CardActiveCourseController c = new CardActiveCourseController(course, listViewSubjects, stackPane, this);
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("ui/components/cardActiveCourse.fxml"));
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
            if (User.getCantGeneratedSchedules() == 0 && User.getSelectedSubjects().size() > 0) {
                showMessage("Advertencia", "No hay posibles horarios que cumplan con los filtros, no puedes añadir esta materia.\n" +
                        "Intenta eliminar algunos filtros.");
                return;
            }

            if (!User.getSelectedSubjects().contains(subject)) {
                User.addSelectedSubject(subject);
                int activeIndexOlder = User.getActiveIndexSchedule();
                User.setActiveIndexSchedule(0);
                int cantGeneratedOlder = User.getCantGeneratedSchedules();
                DatabaseManager.cantGeneratedSchedules();
                if (User.getCantGeneratedSchedules() != 0) {
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
                    showMessage("Advertencia", "No hay posibles horarios que cumplan con los filtros, no puedes añadir esta materia.\n" +
                            "Intenta eliminar algunos filtros.");
                    User.setCantGeneratedSchedules(cantGeneratedOlder);
                    User.setActiveIndexSchedule(activeIndexOlder);
                    User.getSelectedSubjects().remove(subject);
                }


            } else {
                showMessage("Advertencia", "Esta asignatura ya ha sido añadida.");
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

    public void showMessage(String title, String message) {
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Text(title));
        layout.setBody(new Text(message));
        JFXButton button = new JFXButton("Okay");
        JFXDialog dialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.BOTTOM);
        button.setOnAction(event1 -> dialog.close());
        button.setStyle("-fx-background-color: #FF533D");
        layout.setActions(button);
        dialog.setContent(layout);
        dialog.show();
    }

    public void showNameSaveSchedule() {
        if (User.getCantGeneratedSchedules() > 0) {
            JFXDialogLayout layout = new JFXDialogLayout();
            JFXTextField jtf = new JFXTextField();
            Label error = new Label("");
            VBox vBox = new VBox();
            vBox.getChildren().addAll(jtf, error);
            layout.setHeading(new Text("Digite el nombre del horario"));
            layout.setBody(vBox);
            JFXButton button = new JFXButton("Guardar");
            JFXDialog dialog = new JFXDialog(stackPane, layout, JFXDialog.DialogTransition.BOTTOM);
            button.setOnAction(event1 -> {
                String nameSchedule = jtf.getText().trim();
                if (nameSchedule.isEmpty()) {
                    error.setText("Debe digitar un nombre válido");
                } else if (DatabaseManager.thereIsSavedScheduleWithName(nameSchedule)) {
                    error.setText("Ya existe un horario con este nombre.");
                } else {
                    DatabaseManager.addSavedSchedule(nameSchedule);
                    dialog.close();
                }
            });
            button.setStyle("-fx-background-color: #FF533D");
            layout.setActions(button);
            dialog.setContent(layout);
            dialog.show();
        } else {
            showMessage("Advertencia", "No pueden guardar horarios vacios");
        }
    }


}


