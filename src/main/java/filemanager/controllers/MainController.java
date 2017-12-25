package filemanager.controllers;

import filemanager.core.FileAndFolderGatherer;
import filemanager.core.FileToModelListConverter;
import filemanager.core.HDDSpaceTracker;
import filemanager.model.FileModel;
import filemanager.model.FocusDisplay;
import filemanager.model.PositionType;
import filemanager.utils.ApplicationProperties;
import filemanager.utils.Paths;
import filemanager.utils.StageManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.IOException;


public class MainController {
    @FXML
    private Label hddSpaceInfo;
    @FXML
    private Label currentPathLeftDisplayLabel;
    @FXML
    private TableView<FileModel> leftDisplay;
    @FXML
    private TableView<FileModel> rightDisplay;
    @FXML
    private ComboBox<String> driveSelect;

    private FileAndFolderGatherer fileAndFolderGatherer;
    private ApplicationProperties properties;
    private FileToModelListConverter converter;
    private FocusDisplay focusedDisplay;
    private String currentFolder;
    private StageManager stageManager;

    @FXML
    public void initialize() throws IOException {
        createNecessaryObjects();
        prepareDataForHDDSpaceLabel();
        fillDisplayWindowsWithData();
        fillDataForDriveSelector();
        displayFocusListener();
        selectDriveListener();
    }

    private void selectDriveListener() {
        driveSelect.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String oldValue, String newValue) {
                if (newValue.equals("C:\\")) {
                    currentFolder = "C:\\";
                    currentPathLeftDisplayLabel.textProperty().setValue(currentFolder);
                    fillLeftDisplayWithData("C:\\");
                } else if (newValue.equals("D:\\")) {
                    currentFolder = "D:\\";
                    currentPathLeftDisplayLabel.textProperty().setValue(currentFolder);
                    fillLeftDisplayWithData("D:\\");
                }
            }
        });
    }

    private void displayFocusListener() {
        leftDisplay.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    focusedDisplay = FocusDisplay.LEFT;
                    leftDisplay.getSelectionModel().select(0);
                    rightDisplay.getSelectionModel().clearSelection();
                } else {
                    focusedDisplay = FocusDisplay.RIGHT;
                    rightDisplay.getSelectionModel().select(0);
                    leftDisplay.getSelectionModel().clearSelection();
                }
            }
        });
    }

    private void prepareDataForHDDSpaceLabel() throws IOException {
        HDDSpaceTracker hddSpaceTracker = new HDDSpaceTracker();
        hddSpaceInfo.textProperty().setValue
                (hddSpaceTracker.getAmountOfSpaceFromSelectedDrive(HDDSpaceTracker.TypeForHDDSpaceAmountSelector.FREE_SPACE_AMOUNT) + " k from " +
                        (hddSpaceTracker.getAmountOfSpaceFromSelectedDrive(HDDSpaceTracker.TypeForHDDSpaceAmountSelector.TOTAL_SPACE_AMOUNT) + " free"));
    }

    private void createNecessaryObjects() {
        fileAndFolderGatherer = new FileAndFolderGatherer();
        properties = new ApplicationProperties();
        converter = new FileToModelListConverter();
        stageManager = new StageManager();
    }

    private void fillDisplayWindowsWithData() throws IOException {
        String rootPath = properties.getStringValueFromPropertiesForKey("root_path");
        fillLeftDisplayWithData(rootPath);
        fillRightDisplayWithData(rootPath);
        createHeadersForTables(leftDisplay);
        createHeadersForTables(rightDisplay);
    }

    private void fillLeftDisplayWithData(String path) {
        ObservableList<FileModel> itemsForLeftDisplay =
                FXCollections.observableArrayList(converter.convert(fileAndFolderGatherer.getStructureForRootPath(path)));
        leftDisplay.setItems(itemsForLeftDisplay);
        leftDisplay.getSelectionModel().select(0);
    }

    private void fillRightDisplayWithData(String path) {
        ObservableList<FileModel> itemsForRightDisplay =
                FXCollections.observableArrayList(converter.convert(fileAndFolderGatherer.getStructureForRootPath(path)));
        rightDisplay.setItems(itemsForRightDisplay);
    }

    @FXML
    private void keyPressed(KeyEvent e) throws IOException {
        if (focusedDisplay == FocusDisplay.LEFT && e.getCode() == KeyCode.ENTER) {
            updateAndRefreshListOfFilesForView(leftDisplay);
        }
        if (focusedDisplay == FocusDisplay.RIGHT && e.getCode() == KeyCode.ENTER) {
            updateAndRefreshListOfFilesForView(rightDisplay);
        }

        if (focusedDisplay == FocusDisplay.LEFT && e.getCode() == KeyCode.F4) {
            openEditor();
        }
        if (focusedDisplay == FocusDisplay.RIGHT && e.getCode() == KeyCode.F4) {
            openEditor();
        }
    }

    private void updateAndRefreshListOfFilesForView(TableView<FileModel> view) {
        String path = view.getSelectionModel().getSelectedItem().getFile().toString();
        ObservableList<FileModel> items;
        if (!path.equals("...")) {
            items = FXCollections.observableArrayList(converter.convert(fileAndFolderGatherer
                    .getStructureForRootPath(path)));
            currentFolder = path;
            currentPathLeftDisplayLabel.textProperty().setValue(path);
        } else {
            items = FXCollections.observableArrayList(converter.convert(fileAndFolderGatherer
                    .getStructureForRootPath(converter.getParentPath(currentFolder))));
            currentFolder = converter.getParentPath(currentFolder);
            currentPathLeftDisplayLabel.textProperty().setValue(currentFolder);
        }
        view.setItems(items);
        view.refresh();
        view.getSelectionModel().select(0);
    }

    private void createHeadersForTables(TableView<FileModel> table) {
        TableColumn file = new TableColumn("name");
        file.setCellValueFactory(new PropertyValueFactory<FileModel, String>("name"));
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
        table.getColumns().addAll(file, size, extension, date);
    }

    private void fillDataForDriveSelector() {
        File[] drives = File.listRoots();
        if (drives != null && drives.length > 0) {
            for (File aDrive : drives) {
                driveSelect.getItems().add(aDrive.toString());
            }
        }
        driveSelect.getSelectionModel().selectFirst();
        currentFolder = driveSelect.getSelectionModel().selectedItemProperty().getValue();
        currentPathLeftDisplayLabel.textProperty().setValue(currentFolder);
    }

    public void openEditor() throws IOException {
        if (leftDisplay.getSelectionModel().getSelectedItem().getName() != null && leftDisplay.getSelectionModel().getSelectedItem().getType() == PositionType.FILE) {
            ProcessBuilder processBuilder = new ProcessBuilder("Notepad.exe",
                    leftDisplay.getSelectionModel().getSelectedItem().getFile().getPath());
            processBuilder.start();
        } else {
            stageManager.openDialog(Paths.VIEWS.FILE_SELECT_DIALOG, 300, 300);
        }
    }
}
