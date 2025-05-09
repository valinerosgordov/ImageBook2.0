package ru.imagebook.client.admin.ctl.order;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;

public class CopyOrderMessage extends BaseMessage {
	private static final long serialVersionUID = -483738914791251880L;

	public static final String ORDER = "order";

	public CopyOrderMessage(Order<?> order) {
		super(OrderMessages.COPY_ORDER);

		set(ORDER, order);
	}

	public Order<?> getOrder() {
		return get(ORDER);
	}
}
