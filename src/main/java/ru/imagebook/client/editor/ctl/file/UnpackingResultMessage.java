package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class UnpackingResultMessage extends BaseMessage {
	private static final long serialVersionUID = -1847333832087804197L;

	public UnpackingResultMessage() {
		super(FileMessages.UNPACKING_RESULT);

		addAspects(RemotingAspect.CLIENT);
	}
}
