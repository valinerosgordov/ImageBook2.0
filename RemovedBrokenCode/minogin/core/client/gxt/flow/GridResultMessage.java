package ru.minogin.core.client.gxt.flow;

import ru.minogin.core.client.flow.BaseMessage;

public abstract class GridResultMessage extends BaseMessage {
	private static final long serialVersionUID = 5310402138021365596L;

	public static final String OFFSET = "offset";
	public static final String TOTAL = "total";

	protected GridResultMessage() {}

	public GridResultMessage(String type, int offset, long total) {
		super(type);

		set(OFFSET, offset);
		set(TOTAL, total);
	}

	public int getOffset() {
		return (Integer) get(OFFSET);
	}

	public long getTotal() {
		return (Long) get(TOTAL);
	}
}
