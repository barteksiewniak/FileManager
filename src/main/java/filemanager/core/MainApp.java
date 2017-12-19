package filemanager.core;

import filemanager.utils.Paths;
import filemanager.utils.StageManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MainApp extends Application {

    @FXML
    private StageManager stageManager;
    private static final Logger LOGGER = LogManager.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(Paths.VIEWS.MAIN_APPLICATION));
        primaryStage.setTitle("File Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.getIcons().add(new Image(Paths.IMAGES.APPLICATION_ICON));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public MainApp() {
        LOGGER.info("Application started.");
        stageManager = new StageManager();
    }
}
