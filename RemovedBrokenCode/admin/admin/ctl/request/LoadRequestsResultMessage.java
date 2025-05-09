package ru.imagebook.client.admin.ctl.request;

import java.util.List;

import ru.imagebook.shared.model.Request;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.minogin.core.client.gxt.flow.GridResultMessage;

public class LoadRequestsResultMessage extends GridResultMessage {
	private static final long serialVersionUID = -4948076519092565914L;

	public static final String REQUESTS = "requests";

	LoadRequestsResultMessage() {}

	public LoadRequestsResultMessage(List<Request> requests, int offset, long total) {
		super(RequestMessages.LOAD_REQUESTS_RESULT, offset, total);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(REQUESTS, requests);
	}

	public List<Request> getRequests() {
		return get(REQUESTS);
	}
}
