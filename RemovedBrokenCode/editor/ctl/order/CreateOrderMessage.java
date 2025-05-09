package ru.imagebook.client.editor.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class CreateOrderMessage extends BaseMessage {
	private static final long serialVersionUID = 320779991891051888L;

	public static final String PRODUCT_ID = "productId";
	public static final String PAGE_COUNT = "pageCount";

	CreateOrderMessage() {}

	public CreateOrderMessage(int productId, int pageCount) {
		super(OrderMessages.CREATE_ORDER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(PRODUCT_ID, productId);
		set(PAGE_COUNT, pageCount);
	}

	public int getProductId() {
		return (Integer) get(PRODUCT_ID);
	}

	public int getPageCount() {
		return (Integer) get(PAGE_COUNT);
	}
}
