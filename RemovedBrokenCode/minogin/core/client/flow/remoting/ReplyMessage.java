package ru.minogin.core.client.flow.remoting;

import ru.minogin.core.client.flow.BaseMessage;

public class ReplyMessage extends BaseMessage {
	private static final long serialVersionUID = 5375916795939610603L;

	ReplyMessage() {}

	public ReplyMessage(String type) {
		super(type);

		addAspects(RemotingAspect.CLIENT);
	}
}
