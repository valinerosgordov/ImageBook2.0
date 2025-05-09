package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class UnsupportedImageTypeMessage extends BaseMessage {

    private static final String FILENAME = "filename";

    public UnsupportedImageTypeMessage() {
    }

    public UnsupportedImageTypeMessage(String filename) {
        super(FileMessages.UNSUPPORTED_IMAGE_TYPE);

        addAspects(RemotingAspect.CLIENT);

        set(FILENAME, filename);
    }

    public String getFilename() {
        return get(FILENAME);
    }
}
