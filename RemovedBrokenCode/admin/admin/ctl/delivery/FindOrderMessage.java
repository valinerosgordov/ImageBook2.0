package ru.imagebook.client.admin.ctl.delivery;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class FindOrderMessage extends BaseMessage {
	private static final long serialVersionUID = -3351863624675315357L;

	public static final String NUMBER = "number";

	FindOrderMessage() {}

	public FindOrderMessage(String number) {
		super(DeliveryMessages.FIND_ORDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(NUMBER, number);
	}

	public String getNumber() {
		return get(NUMBER);
	}
}
