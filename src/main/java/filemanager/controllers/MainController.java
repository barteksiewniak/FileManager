package filemanager.controllers;

import filemanager.core.FileAndFolderGatherer;
import filemanager.core.FileToModelListConverter;
import filemanager.core.HDDSpaceTracker;
import filemanager.model.FileModel;
import filemanager.utils.ApplicationProperties;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;


public class MainController {
    @FXML
    private Label freeSpaceLabel;
    @FXML
    private Label totalSpaceLabel;
    @FXML
    private TableView<FileModel> leftDisplay;
    @FXML
    private TableView<FileModel> rightDisplay;

    private FileAndFolderGatherer fileAndFolderGatherer;
    private ObservableList<FileModel> itemsForLeftDisplay;
    private ObservableList<FileModel> itemsForRightDisplay;
    private ApplicationProperties properties;
    private FileToModelListConverter converter;

    @FXML
    public void initialize() throws IOException {
        createNecessaryObjects();
        prepareDataForHDDSpaceLabels();
        fillDisplayWindowsWithData();
    }

    private void prepareDataForHDDSpaceLabels() throws IOException {
        HDDSpaceTracker hddSpaceTracker = new HDDSpaceTracker();
        totalSpaceLabel.textProperty().setValue(hddSpaceTracker.getAmountOfSpaceFromSelectedDrive(
                HDDSpaceTracker.TypeForHDDSpaceAmountSelector.TOTAL_SPACE_AMOUNT));
        freeSpaceLabel.textProperty().setValue(hddSpaceTracker.getAmountOfSpaceFromSelectedDrive(
                HDDSpaceTracker.TypeForHDDSpaceAmountSelector.FREE_SPACE_AMOUNT));
    }

    private void createNecessaryObjects() {
        fileAndFolderGatherer = new FileAndFolderGatherer();
        properties = new ApplicationProperties();
        converter = new FileToModelListConverter();
    }

    private void fillDisplayWindowsWithData() throws IOException {
        String rootPath = properties.getStringValueFromPropertiesForKey("root_path");
        fillLeftDisplayWithData(rootPath);
        fillRightDisplayWithData(rootPath);
        createHeadersForTables(leftDisplay);
        createHeadersForTables(rightDisplay);
    }

    private void fillLeftDisplayWithData(String path) {
        itemsForLeftDisplay = FXCollections.observableArrayList(converter.convert(fileAndFolderGatherer.getStructureForRootPath(path)));
        leftDisplay.setItems(itemsForLeftDisplay);
    }

    private void fillRightDisplayWithData(String path) {
        itemsForRightDisplay = FXCollections.observableArrayList(converter.convert(fileAndFolderGatherer.getStructureForRootPath(path)));
        rightDisplay.setItems(itemsForRightDisplay);
    }

    @FXML
    private void keyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            itemsForLeftDisplay = FXCollections.observableArrayList(converter.convert(fileAndFolderGatherer
                    .getStructureForRootPath(leftDisplay.getSelectionModel().getSelectedItem().getFile().toString())));
            leftDisplay.setItems(itemsForLeftDisplay);
            leftDisplay.refresh();
        }
    }

    public void createHeadersForTables(TableView<FileModel> table) {
        TableColumn file = new TableColumn("name");
        file.setCellValueFactory(new PropertyValueFactory<FileModel, String>("name"));
        TableColumn extension = new TableColumn("extension");
        extension.setCellValueFactory(new PropertyValueFactory<FileModel, String>("extension"));
        TableColumn size = new TableColumn("size");
        size.setCellValueFactory(new PropertyValueFactory<FileModel, String>("size"));
        TableColumn date = new TableColumn("date");
        date.setCellValueFactory(new PropertyValueFactory<FileModel, String>("lastModifiedTime"));
        table.getColumns().addAll(file, extension, size, date);
    }
}
