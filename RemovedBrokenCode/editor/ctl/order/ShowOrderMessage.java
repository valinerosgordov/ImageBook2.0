package ru.imagebook.client.editor.ctl.order;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;

public class ShowOrderMessage extends BaseMessage {
	private static final long serialVersionUID = -5869970250601439665L;
	
	public static final String ORDER = "order";

	public ShowOrderMessage(Order<?> order) {
		super(OrderMessages.SHOW_ORDER);

		set(ORDER, order);
	}

	public Order<?> getOrder() {
		return get(ORDER);
	}
}
