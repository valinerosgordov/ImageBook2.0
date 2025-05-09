package ru.imagebook.client.editor.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ProcessOrderMessage extends BaseMessage {
	private static final long serialVersionUID = -2328091314965708517L;

	public ProcessOrderMessage() {
		super(OrderMessages.PROCESS_ORDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
