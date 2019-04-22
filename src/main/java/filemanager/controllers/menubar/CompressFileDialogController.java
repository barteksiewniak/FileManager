package filemanager.controllers.menubar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class CompressFileDialogController {
    @FXML
    public void handlePathParameterForCompress(ActionEvent actionEvent) {
        compressForPath("sout");
    }

    private void compressForPath(String path) {
        System.out.println(path);
    }
}
