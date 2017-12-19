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
}