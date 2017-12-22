package filemanager.core;

import filemanager.model.FileModel;
import filemanager.model.PositionType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileToModelListConverter {
    public List<FileModel> convert(List<File> source) {
        ArrayList<FileModel> destination = new ArrayList<>();

        source.forEach(x -> {
            FileModel model = new FileModel();
            model.setFile(x);
            if (x.isDirectory()) {
                model.setType(PositionType.FOLDER);
            } else {
                model.setType(PositionType.FILE);
            }
            destination.add(model);
        });
        return destination;
    }
}
