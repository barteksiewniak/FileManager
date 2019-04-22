package filemanager.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StageManager {
    public void changeScene(ActionEvent event, String fxml) throws IOException {
        Parent child = FXMLLoader.load(getClass().getResource(fxml));
        Stage parent = (Stage) ((Node) event.getSource()).getScene().getWindow();
        parent.hide();
        parent.setScene(new Scene(child));
        parent.show();
    }

    public void openDialog(String fxml, double width, double height) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = new Stage();
        stage.setScene(new Scene(root, width, height));
        stage.show();
    }

    public void openDialog(String fxml) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}