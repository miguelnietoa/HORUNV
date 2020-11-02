package controllers;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {

    @FXML
    private JFXTextField textFieldSearch;

    @FXML
    private JFXListView<AnchorPane> listViewSubjects;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        buildSubjectCards();

        // text field
        buildAutoCompleteTextField();
    }

    private void buildSubjectCards() {
        for (int i = 0; i < 6; i++) {
            CardSubjectController c = new CardSubjectController("Nombre materia " + i, "IST124", 3);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/components/cardSubject.fxml"));
            loader.setController(c);

            try {
                listViewSubjects.getItems().add(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void buildAutoCompleteTextField() {
        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
        autoCompletePopup.getSuggestions().addAll(
                "Valentina Nieto", "Valentina Imitola", "Valentina Utria",
                "Valentina Reyes", "Valentina Camacho", "Valentina Camelo",
                "Valentina Cavadia", "Valentina Lopez", "Valentina Gonzalez",
                "Valentina Nietoo", "Valentina Imitolaa", "Valentina Utriaa",
                "Valentina Reyees", "Valentina Camachoo", "Valentina Cameelo",
                "Valentina Cavadaia", "Valentina Loopez", "Valentina Gonzalezz"
        );

        // detect selection
        autoCompletePopup.setSelectionHandler(event -> {
            System.out.println(event.getObject());

            // TODO: Add subject to listViewSubjects

            textFieldSearch.setText("");
        });

        // filtering options
        textFieldSearch.textProperty().addListener(observable -> {
            autoCompletePopup.filter(string -> string.toLowerCase().contains(textFieldSearch.getText().toLowerCase()));
            if (autoCompletePopup.getFilteredSuggestions().isEmpty() || textFieldSearch.getText().trim().isEmpty()) {
                autoCompletePopup.hide();
                // if you remove textField.getText.trim().isEmpty() when text field is empty it suggests all options
                // so you can choose
            } else {
                autoCompletePopup.show(textFieldSearch);
            }
        });
    }
}
