package ru.imagebook.client.admin.ctl.action;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class SendStatusCodeResultMessage extends BaseMessage {
	private static final long serialVersionUID = -5855915460352173524L;

	public SendStatusCodeResultMessage() {
		super(ActionMessages.SEND_STATUS_CODE_RESULT);

		addAspects(RemotingAspect.CLIENT);
	}
}
