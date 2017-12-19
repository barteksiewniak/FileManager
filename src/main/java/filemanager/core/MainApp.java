package filemanager.core;

import filemanager.utils.Paths;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
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
}
