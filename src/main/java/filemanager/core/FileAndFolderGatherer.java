package filemanager.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileAndFolderGatherer {
    public List<File> getStructureForRootPath(String path) {
        File file = new File(path);
        return new ArrayList<>(Arrays.asList(Objects.requireNonNull(file.listFiles())));
    }
}
