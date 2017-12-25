package filemanager.utils;

import com.google.common.collect.ComparisonChain;
import filemanager.model.FileModel;

import java.util.Comparator;

public class FileComparator implements Comparator<FileModel> {
    public int compare(FileModel r1, FileModel r2) {
        return ComparisonChain.start()
                .compare(r1.getType(), r2.getType())
                .compare(r1.getName(), r2.getName())
                .result();
    }
}
