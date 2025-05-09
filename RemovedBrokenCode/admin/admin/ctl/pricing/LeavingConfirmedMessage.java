package ru.imagebook.client.admin.ctl.pricing;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Message;

public class LeavingConfirmedMessage extends BaseMessage {
	private static final long serialVersionUID = 5367301158320382644L;
	
	public static final String MESSAGE = "message";
	
	public LeavingConfirmedMessage(Message message) {
		super(PricingMessages.LEAVING_CONFIRMED);
		
		set(MESSAGE, message);
	}
	
	public Message getMessage() {
		return get(MESSAGE);
	}
}
