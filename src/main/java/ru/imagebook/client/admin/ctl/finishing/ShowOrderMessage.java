package ru.imagebook.client.admin.ctl.finishing;

import ru.minogin.core.client.flow.BaseMessage;

public class ShowOrderMessage extends BaseMessage {
	private static final long serialVersionUID = -8887614300079935589L;

	public static final String NUMBER = "number";
	public static final String SCANNED = "scanned";

	public ShowOrderMessage(String number, boolean scanned) {
		super(FinishingMessages.SHOW_ORDER);

		set(NUMBER, number);
		set(SCANNED, scanned);
	}

	public String getNumber() {
		return get(NUMBER);
	}

	public boolean isScanned() {
		return (Boolean) get(SCANNED);
	}
}
