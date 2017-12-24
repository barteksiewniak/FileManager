package filemanager.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileAndFolderGatherer {
    public List<File> getStructureForRootPath(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        ArrayList<File> fileArrayList = new ArrayList<>(Arrays.asList(files));
        if (file.getParent() == null) {
            return fileArrayList;
        } else {
            fileArrayList.add(new File("..."));
            return fileArrayList;
        }
    }
}
