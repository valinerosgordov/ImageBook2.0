package ru.imagebook.client.admin.ctl.delivery;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddOrderMessage extends BaseMessage {
	private static final long serialVersionUID = 318766570851628314L;

	public static final String NUMBER = "number";

	AddOrderMessage() {}

	public AddOrderMessage(String number) {
		super(DeliveryMessages.ADD_ORDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(NUMBER, number);
	}

	public String getNumber() {
		return get(NUMBER);
	}
}
