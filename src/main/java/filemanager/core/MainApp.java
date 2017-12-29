package filemanager.core;

import filemanager.utils.ApplicationProperties;
import filemanager.utils.Paths;
import filemanager.utils.StageManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MainApp extends Application {

    @FXML
    private StageManager stageManager;
    private static final Logger LOGGER = LogManager.getLogger(MainApp.class);
    private ApplicationProperties properties;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(Paths.VIEWS.MAIN_APPLICATION));
        primaryStage.setTitle("File Manager " + properties.getStringValueFromPropertiesForKey("version_number"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.getIcons().add(Paths.IMAGES.APPLICATION_ICON);
        primaryStage.show();
        Context.getInstance().setStageContext(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public MainApp() {
        LOGGER.info("Application started.");
        stageManager = new StageManager();
        properties = new ApplicationProperties();
    }
}
