package filemanager.core;

import java.io.File;
import java.util.*;

public class FileAndFolderGatherer {
    public List<File> getStructureForRootPath(String path) {
        File file = new File(path);
        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(file.listFiles())));
    }
}
