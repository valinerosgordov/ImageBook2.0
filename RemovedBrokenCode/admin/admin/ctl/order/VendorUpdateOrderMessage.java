package ru.imagebook.client.admin.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class VendorUpdateOrderMessage extends BaseMessage {
	private static final long serialVersionUID = -7988913066587764952L;

	public static final String ORDER_ID = "orderId";
	public static final String STATE = "state";

	VendorUpdateOrderMessage() {}

	public VendorUpdateOrderMessage(Integer orderId, Integer state) {
		super(OrderMessages.VENDOR_UPDATE_ORDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ORDER_ID, orderId);
		set(STATE, state);
	}

	public Integer getOrderId() {
		return get(ORDER_ID);
	}

	public Integer getState() {
		return get(STATE);
	}
}
