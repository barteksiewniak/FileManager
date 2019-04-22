package filemanager.controllers.menubar;

import filemanager.utils.StageManager;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

import static filemanager.utils.Paths.VIEWS.COMPRESS_FILE_DIALOG;

public class MenuBarController {
    private StageManager stageManager;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem closeMenuItem;
    @FXML
    private MenuItem compressMenuItem;

    public MenuBarController() {
        this.stageManager = new StageManager();
    }

    public void compressSelected() {
        try {
            stageManager.openDialog(COMPRESS_FILE_DIALOG);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
