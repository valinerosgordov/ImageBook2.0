package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class UploadResultMessage extends BaseMessage {
	private static final long serialVersionUID = 588035126176305509L;

	public UploadResultMessage() {
		super(FileMessages.UPLOAD_RESULT);

		addAspects(RemotingAspect.CLIENT);
	}
}
