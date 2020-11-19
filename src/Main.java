import database.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseManager.getConnection();
        Parent root = FXMLLoader.load(getClass().getResource("ui/Login.fxml"));
        primaryStage.setTitle("HORUNV - Login");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/ui/styles/application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }

    @Override
    public void stop() throws Exception {
        DatabaseManager.closeConnection();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
