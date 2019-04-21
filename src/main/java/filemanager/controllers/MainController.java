package filemanager.controllers;

import filemanager.core.FileAndFolderGatherer;
import filemanager.core.FileToModelListConverter;
import filemanager.core.HDDSpaceTracker;
import filemanager.model.FileModel;
import filemanager.model.FocusDisplay;
import filemanager.model.PositionType;
import filemanager.utils.Constants;
import filemanager.utils.Paths;
import filemanager.utils.PreferencesManager;
import filemanager.utils.StageManager;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainController {
    @FXML
    private Label hddSpaceInfoLeft;
    @FXML
    private Label hddSpaceInfoRight;
    @FXML
    private Label currentActiveDisplayPath;
    @FXML
    private TableView<FileModel> leftDisplay;
    @FXML
    private TableView<FileModel> rightDisplay;
    @FXML
    private ComboBox<String> driveSelectLeft;
    @FXML
    private ComboBox<String> driveSelectRight;
    @FXML
    private MenuBar menuBar;

    private FileAndFolderGatherer fileAndFolderGatherer;
    private FileToModelListConverter converter;
    private FocusDisplay focusedDisplay;
    private String selectedLeftDisplayDir;
    private String selectedRightDisplayDir;
    private StageManager stageManager;
    private List<TableView<FileModel>> displays;
    private HDDSpaceTracker hddSpaceTracker;
    private PreferencesManager preferencesManager;

    @FXML
    public void initialize() {
        createNecessaryObjects();
        preferencesManager.setDefaultPreferences();
        prepareDataForHDDSpaceLabel();
        fillDisplayWindowsWithData();
        fillDataForDriveSelector();
        displayFocusListener();
        selectDriveListener();
    }

    private void createNecessaryObjects() {
        createAndFillDisplaysList();
        fileAndFolderGatherer = new FileAndFolderGatherer();
        converter = new FileToModelListConverter();
        stageManager = new StageManager();
        preferencesManager = new PreferencesManager();
        hddSpaceTracker = new HDDSpaceTracker(preferencesManager);
    }

    private void prepareDataForHDDSpaceLabel() {
        hddSpaceTracker.setSpaceInfoForLeftDisplay(hddSpaceInfoLeft);
        hddSpaceTracker.setSpaceInfoForRightDisplay(hddSpaceInfoRight);
    }

    private void createAndFillDisplaysList() {
        displays = new ArrayList<>();
        displays.add(leftDisplay);
        displays.add(rightDisplay);
    }

    private void fillDisplaysWithData() {
        ObservableList<FileModel> itemsForLeftDisplay =
                FXCollections.observableArrayList(converter.convert
                        (fileAndFolderGatherer.getStructureForPath(preferencesManager.getPreferencesForKey(Constants.Prefs.LAST_SELECTED_DRIVE_FOR_LEFT_DISPLAY))));
        ObservableList<FileModel> itemsForRightDisplay =
                FXCollections.observableArrayList(converter.convert
                        (fileAndFolderGatherer.getStructureForPath(preferencesManager.getPreferencesForKey(Constants.Prefs.LAST_SELECTED_DRIVE_FOR_RIGHT_DISPLAY))));

        displays.forEach(display -> {
            if ("leftDisplay".equals(display.getId())) {
                display.setItems(itemsForLeftDisplay);
            } else {
                display.setItems(itemsForRightDisplay);
            }
            display.getSelectionModel().select(0);
        });
    }

    private void fillDisplaysWithDataForSelector(TableView<FileModel> display, String path) {
        ObservableList<FileModel> items =
                FXCollections.observableArrayList(converter.convert(fileAndFolderGatherer.getStructureForPath(path)));
        display.setItems(items);
        display.getSelectionModel().select(0);
    }

    private void fillDisplayWindowsWithData() {
        fillDisplaysWithData();
        createHeadersForTables(displays);
    }

    private void createHeadersForTables(List<TableView<FileModel>> tables) {
        tables.forEach(table -> {
            TableColumn<FileModel, FileModel> file = new TableColumn<>("name");
            file.setMinWidth(150);
            file.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FileModel, FileModel>,
                    ObservableValue<FileModel>>() {
                @Override
                public ObservableValue<FileModel> call(TableColumn.CellDataFeatures<FileModel, FileModel> features) {
                    return new ReadOnlyObjectWrapper(features.getValue());
                }
            });
            file.setCellFactory(new Callback<TableColumn<FileModel, FileModel>, TableCell<FileModel, FileModel>>() {
                @Override
                public TableCell<FileModel, FileModel> call(TableColumn<FileModel, FileModel> file) {
                    return new TableCell<FileModel, FileModel>() {
                        final ImageView image = new ImageView();

                        @Override
                        public void updateItem(final FileModel file, boolean empty) {
                            super.updateItem(file, empty);
                            if (file != null) {
                                HBox box = new HBox();
                                box.setSpacing(5);
                                VBox vbox = new VBox();
                                vbox.getChildren().add(new Label(file.getName()));
                                image.setFitHeight(20);
                                image.setFitWidth(20);
                                if (file.getType() == PositionType.FOLDER) {
                                    image.setImage(Paths.IMAGES.DIR_IMAGE);
                                } else {
                                    image.setImage(Paths.IMAGES.FILE_IMAGE);
                                }
                                box.getChildren().addAll(image, vbox);
                                setGraphic(box);
                            }
                        }
                    };
                }
            });
            TableColumn extension = new TableColumn("extension");
            extension.setCellValueFactory(new PropertyValueFactory<FileModel, String>("extension"));
            TableColumn size = new TableColumn("size");
            size.setCellValueFactory(new PropertyValueFactory<FileModel, String>("size"));
            TableColumn date = new TableColumn("date");
            date.setCellValueFactory(new PropertyValueFactory<FileModel, String>("lastModifiedTime"));
            file.prefWidthProperty().bind(table.widthProperty().multiply(0.5));
            extension.prefWidthProperty().bind(table.widthProperty().multiply(0.1));
            size.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
            date.prefWidthProperty().bind(table.widthProperty().multiply(0.2));
            table.getColumns().addAll(file, extension, size, date);
        });
    }

    private void selectDriveListener() {
        driveSelectLeft.valueProperty().addListener((ov, oldValue, newValue) -> {
            leftDriveDetector(newValue);
        });

        driveSelectRight.valueProperty().addListener((ov, oldValue, newValue) -> {
            rightDriveDetector(newValue);
        });
    }

    private void leftDriveDetector(String newValue) {
        if (newValue.equals(Constants.C_DRIVE)) {
            leftDriveListenerConfig(Constants.C_DRIVE);
        } else if (newValue.equals(Constants.D_DRIVE)) {
            leftDriveListenerConfig(Constants.D_DRIVE);
        }
        prepareDataForHDDSpaceLabel();
    }

    private void rightDriveDetector(String newValue) {
        if (newValue.equals(Constants.C_DRIVE)) {
            rightDriveListenerConfig(Constants.C_DRIVE);
        } else if (newValue.equals(Constants.D_DRIVE)) {
            rightDriveListenerConfig(Constants.D_DRIVE);
        }
        prepareDataForHDDSpaceLabel();
    }

    private void leftDriveListenerConfig(String leftDisplayDir) {
        currentActiveDisplayPath.textProperty().setValue(leftDisplayDir);
        fillDisplaysWithDataForSelector(leftDisplay, leftDisplayDir);
        leftDisplay.refresh();
        preferencesManager.updateValueInPreferencesForKey(leftDisplayDir,
                Constants.Prefs.LAST_SELECTED_DRIVE_FOR_LEFT_DISPLAY);
    }

    private void rightDriveListenerConfig(String rightDisplayDir) {
        currentActiveDisplayPath.textProperty().setValue(rightDisplayDir);
        fillDisplaysWithDataForSelector(rightDisplay, rightDisplayDir);
        rightDisplay.refresh();
        preferencesManager.updateValueInPreferencesForKey(rightDisplayDir,
                Constants.Prefs.LAST_SELECTED_DRIVE_FOR_RIGHT_DISPLAY);
    }

    private void displayFocusListener() {
        leftDisplay.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                focusedDisplay = FocusDisplay.LEFT;
                leftDisplay.getSelectionModel().select(0);
                rightDisplay.getSelectionModel().clearSelection();
                currentActiveDisplayPath.textProperty().setValue(selectedLeftDisplayDir);
            }
        });

        rightDisplay.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                focusedDisplay = FocusDisplay.RIGHT;
                rightDisplay.getSelectionModel().select(0);
                leftDisplay.getSelectionModel().clearSelection();
                currentActiveDisplayPath.textProperty().setValue(selectedRightDisplayDir);
            }
        });
    }

    @FXML
    private void keyPressed(KeyEvent e) throws IOException {
        if (e.getCode() == KeyCode.ENTER) {
            updateAndRefreshListOfFilesForView(getActiveView());
        }

        if (e.getCode() == KeyCode.F4) {
            openEditor();
        }

        if (e.getCode() == KeyCode.F5) {
            copyFile();
        }
    }

    private void updateAndRefreshListOfFilesForView(TableView<FileModel> view) {
        String path = view.getSelectionModel().getSelectedItem().getFile().toString();
        ObservableList<FileModel> items;
        if (!path.equals("...")) {
            items = FXCollections.observableArrayList(converter.convert(fileAndFolderGatherer
                    .getStructureForPath(path)));
            selectedLeftDisplayDir = path;
            currentActiveDisplayPath.textProperty().setValue(path);
        } else {
            items = FXCollections.observableArrayList(converter.convert(fileAndFolderGatherer
                    .getStructureForPath(converter.getParentPath(selectedLeftDisplayDir))));
            selectedLeftDisplayDir = converter.getParentPath(selectedLeftDisplayDir);
            currentActiveDisplayPath.textProperty().setValue(selectedLeftDisplayDir);
        }
        view.setItems(items);
        view.refresh();
        view.getSelectionModel().select(0);
    }

    private void refresh(TableView<FileModel> view) {
        ObservableList<FileModel> items;
        items = FXCollections.observableArrayList(converter.convert(fileAndFolderGatherer
                .getStructureForPath(selectedLeftDisplayDir)));
        view.setItems(items);
        view.refresh();
        view.getSelectionModel().select(0);
    }

    private void fillDataForDriveSelector() {
        File[] drives = File.listRoots();
        if (drives != null && drives.length > 0) {
            for (File aDrive : drives) {
                driveSelectLeft.getItems().add(aDrive.toString());
                driveSelectRight.getItems().add(aDrive.toString());
            }
        }
        driveSelectLeft.getSelectionModel().select(preferencesManager.getPreferencesForKey(Constants.Prefs.LAST_SELECTED_DRIVE_FOR_LEFT_DISPLAY));
        driveSelectRight.getSelectionModel().select(preferencesManager.getPreferencesForKey(Constants.Prefs.LAST_SELECTED_DRIVE_FOR_RIGHT_DISPLAY));
        selectedLeftDisplayDir = driveSelectLeft.getSelectionModel().selectedItemProperty().getValue();
        selectedRightDisplayDir = driveSelectRight.getSelectionModel().selectedItemProperty().getValue();
        currentActiveDisplayPath.textProperty().setValue(selectedLeftDisplayDir);
    }

    public void openEditor() throws IOException {
        if (getActiveView().getSelectionModel().getSelectedItem().getName() != null && getActiveView().getSelectionModel().getSelectedItem().getType() == PositionType.FILE) {
            ProcessBuilder processBuilder = new ProcessBuilder("Notepad.exe",
                    getActiveView().getSelectionModel().getSelectedItem().getFile().getPath());
            processBuilder.start();
        } else {
            stageManager.openDialog(Paths.VIEWS.FILE_SELECT_DIALOG, 300, 300);
        }
    }

    private TableView<FileModel> getActiveView() {
        TableView<FileModel> view;
        if (focusedDisplay == FocusDisplay.LEFT) {
            view = leftDisplay;
        } else {
            view = rightDisplay;
        }
        return view;
    }

    @FXML
    private void copyFile() throws IOException {
        File file = new File(getActiveView().getSelectionModel().getSelectedItem().getFile().getPath());
        File destinationDir = new File("C:\\test-directory");

        FileUtils.copyFileToDirectory(file, destinationDir);
        refresh(leftDisplay);
        refresh(rightDisplay);
    }

    public void test() {

    }
}
