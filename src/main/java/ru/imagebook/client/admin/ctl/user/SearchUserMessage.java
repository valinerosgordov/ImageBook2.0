package ru.imagebook.client.admin.ctl.user;

import ru.minogin.core.client.flow.BaseMessage;

public class SearchUserMessage extends BaseMessage {
	private static final long serialVersionUID = -4129891408599346715L;
	
	public static final String QUERY = "query";

	public SearchUserMessage(String query) {
		super(UserMessages.SEARCH);

		set(QUERY, query);
	}

	public String getQuery() {
		return get(QUERY);
	}
}
