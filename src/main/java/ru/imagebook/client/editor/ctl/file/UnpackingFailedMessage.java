package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class UnpackingFailedMessage extends BaseMessage {
	private static final long serialVersionUID = 1429254628584221486L;

	public UnpackingFailedMessage() {
		super(FileMessages.UNPACKING_FAILED);

		addAspects(RemotingAspect.CLIENT);
	}
}
