package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class WrongFileTypeMessage extends BaseMessage {
    private static final long serialVersionUID = 1753608515294767518L;

    public WrongFileTypeMessage() {
        super(FileMessages.WRONG_FILE_TYPE);

        addAspects(RemotingAspect.CLIENT);
    }
}
