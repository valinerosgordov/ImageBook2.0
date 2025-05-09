package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class UploadFailedMessage extends BaseMessage {
	private static final long serialVersionUID = 1753608515294767518L;

	public UploadFailedMessage() {
		super(FileMessages.UPLOAD_FAILED);

		addAspects(RemotingAspect.CLIENT);
	}
}
