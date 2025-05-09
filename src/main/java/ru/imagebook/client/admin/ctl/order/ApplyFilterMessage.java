package ru.imagebook.client.admin.ctl.order;

import ru.imagebook.shared.model.OrderFilter;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ApplyFilterMessage extends BaseMessage {
	private static final long serialVersionUID = -5329187769872021540L;

	public static final String FILTER = "filter";

	ApplyFilterMessage() {}

	public ApplyFilterMessage(OrderFilter filter) {
		super(OrderMessages.APPLY_FILTER);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(FILTER, filter);
	}

	public OrderFilter getFilter() {
		return get(FILTER);
	}
}
