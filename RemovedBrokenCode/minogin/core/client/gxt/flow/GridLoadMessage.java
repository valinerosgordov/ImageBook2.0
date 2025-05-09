package ru.minogin.core.client.gxt.flow;

import ru.minogin.core.client.flow.BaseMessage;

public abstract class GridLoadMessage extends BaseMessage {
	private static final long serialVersionUID = 1635004936529324637L;

	public static final String OFFSET = "offset";
	public static final String LIMIT = "limit";

	protected GridLoadMessage() {}

	public GridLoadMessage(String type, int offset, int limit) {
		super(type);

		set(OFFSET, offset);
		set(LIMIT, limit);
	}

	public int getOffset() {
		return (Integer) get(OFFSET);
	}

	public int getLimit() {
		return (Integer) get(LIMIT);
	}
}
