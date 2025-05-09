package ru.imagebook.client.admin.ctl.action;

import ru.imagebook.client.admin.ctl.order.OrderMessages;
import ru.minogin.core.client.flow.BaseMessage;

public class SearchActionCodeMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String QUERY = "query";

	public SearchActionCodeMessage(String query) {
		super(ActionMessages.SEARCH);

		set(QUERY, query);
	}

	public String getQuery() {
		return get(QUERY);
	}
}
