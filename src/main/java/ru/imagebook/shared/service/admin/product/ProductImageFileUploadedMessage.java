package ru.imagebook.shared.service.admin.product;

import ru.minogin.core.client.push.PushMessage;

public class ProductImageFileUploadedMessage implements PushMessage {
    private String sourceFile;
    private String photoPath;

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
