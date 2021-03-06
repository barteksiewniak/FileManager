package filemanager.core;

import filemanager.model.Method;
import filemanager.utils.Constants;
import filemanager.utils.PreferencesManager;
import javafx.scene.control.Label;

import java.io.File;

public class HDDSpaceTracker {

    private PreferencesManager preferencesManager;

    public HDDSpaceTracker(PreferencesManager preferencesManager) {
        this.preferencesManager = preferencesManager;
    }


    public void setSpaceInfoForRightDisplay(Label rightDisplayLabel) {
        rightDisplayLabel.textProperty().setValue
                (calcFreeAndTotalSpace(Method.FREE_SPACE_AMOUNT,
                        preferencesManager.getPreferencesForKey(Constants.Prefs.LAST_SELECTED_DRIVE_FOR_RIGHT_DISPLAY)) + " k from " +
                        (calcFreeAndTotalSpace(Method.TOTAL_SPACE_AMOUNT,
                                preferencesManager.getPreferencesForKey(Constants.Prefs.LAST_SELECTED_DRIVE_FOR_RIGHT_DISPLAY)) + " free"));
    }

    public void setSpaceInfoForLeftDisplay(Label leftDisplayLabel) {
        leftDisplayLabel.textProperty().setValue
                (calcFreeAndTotalSpace(Method.FREE_SPACE_AMOUNT,
                        preferencesManager.getPreferencesForKey(Constants.Prefs.LAST_SELECTED_DRIVE_FOR_LEFT_DISPLAY)) + " k from " +
                        (calcFreeAndTotalSpace(Method.TOTAL_SPACE_AMOUNT,
                                preferencesManager.getPreferencesForKey(Constants.Prefs.LAST_SELECTED_DRIVE_FOR_LEFT_DISPLAY)) + " free"));
    }

    public String displayFormatted(String source) {
        // reverse string because we need to have grouping from right side
        String temporaryStringBuilderWithReversedString = new StringBuilder(source).reverse().toString();
        String[] temporaryArrayWithSplittedChars = temporaryStringBuilderWithReversedString.split("(?<=\\G...)");
        // here we save the result
        StringBuilder result = new StringBuilder();

        for (String row : temporaryArrayWithSplittedChars) {
            result.append(row).append(" ");
        }

        // again reversing the string for proper order
        return new StringBuilder(result).reverse().toString().trim();
    }

    private String calcFreeAndTotalSpace(Method mode, String path) {
        File file = new File(path);
        String result = "";

        if (mode == Method.FREE_SPACE_AMOUNT) {
            result = String.valueOf((file.getFreeSpace() / 1024));
        } else if (mode == Method.TOTAL_SPACE_AMOUNT) {
            result = String.valueOf((file.getTotalSpace() / 1024));
        }

        return displayFormatted(result);
    }
}
