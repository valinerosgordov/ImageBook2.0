package ru.imagebook.client.admin.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadUsersMessage extends BaseMessage {
	private static final long serialVersionUID = -6293923480624991308L;

	public static final String OFFSET = "offset";
	public static final String LIMIT = "limit";
	public static final String QUERY = "query";

	LoadUsersMessage() {}

	public LoadUsersMessage(int offset, int limit, String query) {
		super(OrderMessages.LOAD_USERS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(OFFSET, offset);
		set(LIMIT, limit);
		set(QUERY, query);
	}

	public int getOffset() {
		return (Integer) get(OFFSET);
	}

	public int getLimit() {
		return (Integer) get(LIMIT);
	}

	public String getQuery() {
		return get(QUERY);
	}
}
