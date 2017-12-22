package filemanager.model;

import java.io.File;
import java.time.LocalDate;

public class FileModel {
    private PositionType type;
    private File file;
    private String extension;
    private String size;
    private LocalDate date;
    private String attributes;

    public PositionType getType() {
        return type;
    }

    public void setType(PositionType type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "FileModel{" +
                "type=" + type +
                ", file=" + file +
                ", extension='" + extension + '\'' +
                ", size='" + size + '\'' +
                ", date=" + date +
                ", attributes='" + attributes + '\'' +
                '}';
    }
}
