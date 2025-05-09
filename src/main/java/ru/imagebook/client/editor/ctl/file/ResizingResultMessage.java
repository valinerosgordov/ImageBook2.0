package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ResizingResultMessage extends BaseMessage {
	private static final long serialVersionUID = -8016195340483676979L;

	public ResizingResultMessage() {
		super(FileMessages.RESIZING_RESULT);

		addAspects(RemotingAspect.CLIENT);
	}
}
