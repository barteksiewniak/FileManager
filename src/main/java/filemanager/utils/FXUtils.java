package filemanager.utils;

import filemanager.core.Context;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class FXUtils {
    private FXUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Stage getStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static Stage getPrimaryStage() {
        return Context.getInstance().getStageContext();
    }
}
