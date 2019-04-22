package filemanager.utils;

import javafx.scene.image.Image;

public final class Paths {
    private Paths() {
    }

    public static final class VIEWS {
        private VIEWS() {
        }

        public static final String MAIN_APPLICATION = "/fxmls/MainStage.fxml";
        public static final String FILE_SELECT_DIALOG = "/fxmls/NoFileSelectedDialogController.fxml";
    }

    public static final class IMAGES {
        private IMAGES() {
        }

        public static final Image APPLICATION_ICON = new Image("images/disk-icon.png");
        public static final Image DIR_IMAGE = new Image("images/folder.png");
        public static final Image FILE_IMAGE = new Image("images/file.png");
    }
}
