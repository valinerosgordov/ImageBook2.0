package ru.imagebook.client.admin.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class PublishWebFlashMessage extends BaseMessage {
	private static final long serialVersionUID = -5348411324679842626L;

	public static final String ORDER_ID = "orderId";

	PublishWebFlashMessage() {}

	public PublishWebFlashMessage(int orderId) {
		super(OrderMessages.PUBLISH_WEB_FLASH);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_ID, orderId);
	}

	public int getOrderId() {
		return (Integer) get(ORDER_ID);
	}
}
