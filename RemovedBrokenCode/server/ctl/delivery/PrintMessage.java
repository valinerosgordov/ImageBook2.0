package ru.imagebook.server.ctl.delivery;

import java.io.Writer;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class PrintMessage extends BaseMessage {
	private static final long serialVersionUID = 1073050601524751076L;

	public static final String DELIVERY_TYPE = "deliveryType";
	public static final String WRITER = "writer";

	public PrintMessage(Integer deliveryType, Writer writer) {
		super(DeliveryMessages.PRINT);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(DELIVERY_TYPE, deliveryType);
		set(WRITER, writer);
	}

	public Integer getDeliveryType() {
		return (Integer) get(DELIVERY_TYPE);
	}

	public Writer getWriter() {
		return get(WRITER);
	}
}
