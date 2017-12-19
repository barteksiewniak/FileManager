package filemanager.core;

import javafx.stage.Stage;

public class Context {
    private Stage stage = new Stage();
    private static final Context instance = new Context();

    public static Context getInstance() {
        return instance;
    }

    public Stage setStageContext(Stage stage) {
        this.stage = stage;
        return stage;
    }

    public Stage getStageContext() {
        return stage;
    }
}
