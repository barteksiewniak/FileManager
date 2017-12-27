package filemanager.model;

import javafx.scene.image.Image;

import java.io.File;

public class FileModel {
    private PositionType type;
    private File file;
    private String name;
    private String extension;
    private String size;
    private String lastModifiedTime;
    private String attributes;
    public final Image dirImage = new Image("images/folder.png");
    public final Image fileImage = new Image("images/file.png");

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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @Override
    public String toString() {
        return "FileModel{" +
                "type=" + type +
                ", file=" + file +
                ", name='" + name + '\'' +
                ", extension='" + extension + '\'' +
                ", size='" + size + '\'' +
                ", lastModifiedTime='" + lastModifiedTime + '\'' +
                ", attributes='" + attributes + '\'' +
                '}';
    }
}
