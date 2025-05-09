package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ResizingFailedMessage extends BaseMessage {
	private static final long serialVersionUID = -7893469933963542093L;

	public ResizingFailedMessage() {
		super(FileMessages.RESIZING_FAILED);

		addAspects(RemotingAspect.CLIENT);
	}
}
