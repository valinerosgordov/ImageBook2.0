package ru.imagebook.client.admin.ctl.user;

import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.client.gxt.flow.GridLoadMessage;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadUsersMessage extends GridLoadMessage {
	private static final long serialVersionUID = -8901181134331663492L;

	public static final String QUERY = "query";

	LoadUsersMessage() {}

	public LoadUsersMessage(int offset, int limit) {
		super(UserMessages.LOAD_USERS, offset, limit);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}

	public String getQuery() {
		return get(QUERY);
	}

	public void setQuery(String query) {
		set(QUERY, query);
	}
}
