package ru.imagebook.client.admin.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;

public class SearchOrderMessage extends BaseMessage {
	private static final long serialVersionUID = 5926323389497336767L;
	
	public static final String QUERY = "query";

	public SearchOrderMessage(String query) {
		super(OrderMessages.SEARCH);

		set(QUERY, query);
	}

	public String getQuery() {
		return get(QUERY);
	}
}
