package ru.imagebook.client.admin.ctl.order;

import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.client.gxt.flow.GridLoadMessage;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadOrdersMessage extends GridLoadMessage {
	private static final long serialVersionUID = -3460414220532227033L;

	public static final String QUERY = "query";

	LoadOrdersMessage() {}

	public LoadOrdersMessage(int offset, int limit) {
		super(OrderMessages.LOAD_ORDERS, offset, limit);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}

	public String getQuery() {
		return get(QUERY);
	}

	public void setQuery(String query) {
		set(QUERY, query);
	}
}
