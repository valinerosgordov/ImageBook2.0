package ru.imagebook.server.service.editor;

public class UnsupportedImageTypeException extends RuntimeException {
    private String filename;

    public UnsupportedImageTypeException(String filename) {
        super();
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
