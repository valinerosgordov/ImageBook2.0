package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class NonZipArchiveFormatMessage extends BaseMessage {
    private static final long serialVersionUID = 1429254628584221486L;

    public NonZipArchiveFormatMessage() {
        super(FileMessages.NON_ZIP_ARCHIVE_FORMAT);

        addAspects(RemotingAspect.CLIENT);
    }
}
