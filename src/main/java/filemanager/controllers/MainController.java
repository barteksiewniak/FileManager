package filemanager.controllers;

import filemanager.core.FileAndFolderGatherer;
import filemanager.core.FileToModelListConverter;
import filemanager.core.HDDSpaceTracker;
import filemanager.model.FileModel;
import filemanager.model.FocusDisplay;
import filemanager.model.Method;
import filemanager.model.PositionType;
import filemanager.utils.ApplicationProperties;
import filemanager.utils.Paths;
import filemanager.utils.StageManager;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
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

    private FileAndFolderGatherer fileAndFolderGatherer;
    private ApplicationProperties properties;
    private FileToModelListConverter converter;
    private FocusDisplay focusedDisplay;
    private String selectedLeftDisplayDir;
    private String selectedRightDisplayDir;
    private StageManager stageManager;
    private List<TableView<FileModel>> displays;
    private List<Object> leftDisplayProperties;
    private List<Object> rightDisplayProperties;

    @FXML
    public void initialize() throws IOException {
        createNecessaryObjects();
        prepareDataForHDDSpaceLabel();
        fillDisplayWindowsWithData();
        fillDataForDriveSelector();
        displayFocusListener();
        selectDriveListener();
    }

    private void createNecessaryObjects() {
        createAndFillDisplaysList();
        fileAndFolderGatherer = new FileAndFolderGatherer();
        properties = new ApplicationProperties();
        converter = new FileToModelListConverter();
        stageManager = new StageManager();
    }

    private void prepareDataForHDDSpaceLabel() {
        HDDSpaceTracker hddSpaceTracker = new HDDSpaceTracker();
        hddSpaceInfoLeft.textProperty().setValue
                (hddSpaceTracker.calcFreeAndTotalSpace(Method.FREE_SPACE_AMOUNT, "C:/") + " k from " +
                        (hddSpaceTracker.calcFreeAndTotalSpace(Method.TOTAL_SPACE_AMOUNT, "C:/") + " free"));
        leftDisplayProperties.add(hddSpaceInfoLeft);
        hddSpaceInfoRight.textProperty().setValue
                (hddSpaceTracker.calcFreeAndTotalSpace(Method.FREE_SPACE_AMOUNT, "D:/") + " k from " +
                        (hddSpaceTracker.calcFreeAndTotalSpace(Method.TOTAL_SPACE_AMOUNT, "D:/") + " free"));
        rightDisplayProperties.add(hddSpaceInfoRight);
    }

    private void createAndFillDisplaysList() {
        leftDisplayProperties = new ArrayList<>();
        rightDisplayProperties = new ArrayList<>();
        displays = new ArrayList<>();
        displays.add(leftDisplay);
        displays.add(rightDisplay);
    }

    private void fillDisplaysWithData(String path) {
        displays.forEach(display -> {
            ObservableList<FileModel> itemsForLeftDisplay =
                    FXCollections.observableArrayList(converter.convert(fileAndFolderGatherer.getStructureForPath(path)));
            display.setItems(itemsForLeftDisplay);
            display.getSelectionModel().select(0);
            leftDisplayProperties.add(display);
        });
    }

    private void fillDisplaysWithDataForSelector(TableView<FileModel> display, String path) {
        ObservableList<FileModel> items =
                FXCollections.observableArrayList(converter.convert(fileAndFolderGatherer.getStructureForPath(path)));
        display.setItems(items);
        display.getSelectionModel().select(0);
    }

    private void fillDisplayWindowsWithData() throws IOException {
        final String ROOT_PATH = properties.getStringValueFromPropertiesForKey("root_path");
        fillDisplaysWithData(ROOT_PATH);
        createHeadersForTables(displays);
    }

    private void createHeadersForTables(List<TableView<FileModel>> tables) {
        tables.forEach(table -> {
            TableColumn<FileModel, FileModel> file = new TableColumn<>("name");
            file.setMinWidth(150);
            file.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<FileModel, FileModel>, ObservableValue<FileModel>>() {
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
            if (newValue.equals("C:\\")) {
                selectedLeftDisplayDir = "C:\\";
                currentActiveDisplayPath.textProperty().setValue(selectedLeftDisplayDir);
                fillDisplaysWithDataForSelector(leftDisplay, "C:\\");
                leftDisplayProperties.add(selectedLeftDisplayDir);
                leftDisplay.refresh();
            } else if (newValue.equals("D:\\")) {
                selectedLeftDisplayDir = "D:\\";
                currentActiveDisplayPath.textProperty().setValue(selectedLeftDisplayDir);
                fillDisplaysWithDataForSelector(leftDisplay, "D:\\");
                leftDisplayProperties.add(selectedLeftDisplayDir);
                leftDisplay.refresh();
            }
        });

        driveSelectRight.valueProperty().addListener((ov, oldValue, newValue) -> {
            if (newValue.equals("C:\\")) {
                selectedRightDisplayDir = "C:\\";
                currentActiveDisplayPath.textProperty().setValue(selectedRightDisplayDir);
                fillDisplaysWithDataForSelector(rightDisplay, "C:\\");
                rightDisplayProperties.add(selectedRightDisplayDir);
                rightDisplay.refresh();

            } else if (newValue.equals("D:\\")) {
                selectedRightDisplayDir = "D:\\";
                currentActiveDisplayPath.textProperty().setValue(selectedRightDisplayDir);
                fillDisplaysWithDataForSelector(rightDisplay, "D:\\");
                rightDisplayProperties.add(selectedRightDisplayDir);
                rightDisplay.refresh();
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
                    currentActiveDisplayPath.textProperty().setValue(selectedLeftDisplayDir);
                    leftDisplayProperties.add(focusedDisplay);
                }
            }
        });

        rightDisplay.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    focusedDisplay = FocusDisplay.RIGHT;
                    rightDisplay.getSelectionModel().select(0);
                    leftDisplay.getSelectionModel().clearSelection();
                    currentActiveDisplayPath.textProperty().setValue(selectedRightDisplayDir);
                    rightDisplayProperties.add(focusedDisplay);
                }
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
        driveSelectLeft.getSelectionModel().selectFirst();
        driveSelectRight.getSelectionModel().selectFirst();
        selectedLeftDisplayDir = driveSelectLeft.getSelectionModel().selectedItemProperty().getValue();
        selectedRightDisplayDir = driveSelectRight.getSelectionModel().selectedItemProperty().getValue();
        currentActiveDisplayPath.textProperty().setValue(selectedLeftDisplayDir);
        leftDisplayProperties.add(driveSelectLeft);
        rightDisplayProperties.add(driveSelectRight);
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
}
