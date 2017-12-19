package filemanager.core;

import java.io.File;

public class HDDSpaceTracker {
    public String getAmountOfSpaceFromSelectedDrive(TypeForHDDSpaceAmountSelector selector) {
        final String PATH_NAME = "C:/";
        File file = new File(PATH_NAME);
        String result = "";
        if (selector == TypeForHDDSpaceAmountSelector.FREE_SPACE_AMOUNT) {
            result = String.valueOf(file.getFreeSpace());

        } else if (selector == TypeForHDDSpaceAmountSelector.TOTAL_SPACE_AMOUNT) {
            result = String.valueOf(file.getTotalSpace());

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
