package filemanager.core;

import filemanager.model.Method;

import java.io.File;

public class HDDSpaceTracker {
    public String calcFreeAndTotalSpace(Method mode, String path) {
        File file = new File(path);
        String result = "";

        if (mode == Method.FREE_SPACE_AMOUNT) {
            result = String.valueOf((file.getFreeSpace() / 1024));
        } else if (mode == Method.TOTAL_SPACE_AMOUNT) {
            result = String.valueOf((file.getTotalSpace() / 1024));
        }

        return displayFormatted(result);
    }

    public String displayFormatted(String source) {
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
}
