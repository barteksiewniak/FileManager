package filemanager.core;

import filemanager.utils.ApplicationProperties;

import java.io.File;
import java.io.IOException;

public class HDDSpaceTracker {
    private ApplicationProperties properties = new ApplicationProperties();
    public String getAmountOfSpaceFromSelectedDrive(TypeForHDDSpaceAmountSelector selector) throws IOException {
        final String PATH_NAME = properties.getStringValueFromPropertiesForKey("hdd_space_tracker_path");
        File file = new File(PATH_NAME);
        String result = "";

        if (selector == TypeForHDDSpaceAmountSelector.FREE_SPACE_AMOUNT) {
            result = String.valueOf((file.getFreeSpace() / 1024));
        } else if (selector == TypeForHDDSpaceAmountSelector.TOTAL_SPACE_AMOUNT) {
            result = String.valueOf((file.getTotalSpace() / 1024));
        }

        return separateSpaceByThreeCharsWithSpace(result);
    }

    private String separateSpaceByThreeCharsWithSpace(String source) {
        // reverse string because we need to have grouping from right side
        String temporaryStringBuilderWithReversedString = new StringBuilder(source).reverse().toString();
        String[] temporaryArrayWithSplittedChars = temporaryStringBuilderWithReversedString.split("(?<=\\G...)");
        // here we save the result
        StringBuilder result = new StringBuilder();

        for (String row : temporaryArrayWithSplittedChars) {
            result.append(row + " ");
        }

        // again reversing the string for proper order
        return new StringBuilder(result).reverse().toString().trim();
    }

    public enum TypeForHDDSpaceAmountSelector {
        FREE_SPACE_AMOUNT, TOTAL_SPACE_AMOUNT
    }
}
