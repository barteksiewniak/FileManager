package filemanager.core;

import filemanager.model.FileModel;
import filemanager.model.PositionType;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FileToModelListConverter {
    private static final Logger LOGGER = LogManager.getLogger(MainApp.class);
    public List<FileModel> convert(List<File> source) {
        ArrayList<FileModel> destination = new ArrayList<>();

        source.forEach(x -> {
            FileModel model = new FileModel();
            model.setFile(x);
            if (x.isDirectory()) {
                model.setType(PositionType.FOLDER);
                model.setName("[" + x.getName() + "]");
                model.setSize("<DIR>");
            } else {
                model.setType(PositionType.FILE);
                String baseName = FilenameUtils.getBaseName(x.getName());
                String extension = FilenameUtils.getExtension(x.getName());
                model.setName(baseName);
                model.setExtension(extension);
            }
            try {
                BasicFileAttributes attr = Files.readAttributes(x.toPath(), BasicFileAttributes.class);
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                String date = df.format(attr.lastModifiedTime().toMillis());
                model.setLastModifiedTime(date);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            destination.add(model);
        });
        return destination;
    }
}
