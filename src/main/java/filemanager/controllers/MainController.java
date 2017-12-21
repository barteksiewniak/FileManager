package filemanager.controllers;

import filemanager.core.FileAndFolderGatherer;
import filemanager.core.HDDSpaceTracker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.File;
import java.io.IOException;

public class MainController {
    @FXML
    private Label freeSpaceLabel;
    @FXML
    private Label totalSpaceLabel;
    @FXML
    private ListView<File> leftDisplay;
    @FXML
    private ListView<File> rightDisplay;
    private FileAndFolderGatherer fileAndFolderGatherer;

    @FXML
    public void initialize() throws IOException {
        createNecessaryObjects();
        prepareDataForHDDSpace();
        fillDisplayWindowsWithData();
    }

    private void prepareDataForHDDSpace() throws IOException {
        HDDSpaceTracker hddSpaceTracker = new HDDSpaceTracker();
        totalSpaceLabel.textProperty().setValue(hddSpaceTracker.getAmountOfSpaceFromSelectedDrive(
                HDDSpaceTracker.TypeForHDDSpaceAmountSelector.TOTAL_SPACE_AMOUNT));
        freeSpaceLabel.textProperty().setValue(hddSpaceTracker.getAmountOfSpaceFromSelectedDrive(
                HDDSpaceTracker.TypeForHDDSpaceAmountSelector.FREE_SPACE_AMOUNT));
    }

    private void createNecessaryObjects() {
        fileAndFolderGatherer = new FileAndFolderGatherer();
    }

    private void fillDisplayWindowsWithData() {
        fillLeftDisplayWithData("C:/");
        fillRightDisplayWithData("C:/");
    }

    private void fillLeftDisplayWithData(String path) {
        ObservableList<File> itemsForLeftDisplay =
                FXCollections.observableArrayList(fileAndFolderGatherer.getStructureForRootPath(path));
        leftDisplay.setItems(itemsForLeftDisplay);
    }

    private void fillRightDisplayWithData(String path) {
        ObservableList<File> itemsForRightDisplay =
                FXCollections.observableArrayList(fileAndFolderGatherer.getStructureForRootPath(path));
        rightDisplay.setItems(itemsForRightDisplay);
    }
}
