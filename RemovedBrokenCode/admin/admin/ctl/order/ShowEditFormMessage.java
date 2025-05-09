package ru.imagebook.client.admin.ctl.order;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;

public class ShowEditFormMessage extends BaseMessage {
	private static final long serialVersionUID = 1976553133856833258L;
	
	public static final String ORDER = "order";

	public ShowEditFormMessage(Order<?> order) {
		super(OrderMessages.SHOW_EDIT_FORM);

		set(ORDER, order);
	}

	public Order<?> getOrder() {
		return get(ORDER);
	}
}
