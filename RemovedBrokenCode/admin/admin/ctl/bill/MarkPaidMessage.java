package ru.imagebook.client.admin.ctl.bill;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class MarkPaidMessage extends BaseMessage {
	private static final long serialVersionUID = 6539306286759998776L;

	public static final String IDS = "ids";

	MarkPaidMessage() {}

	public MarkPaidMessage(List<Integer> ids) {
		super(BillMessages.MARK_PAID);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(IDS, ids);
	}

	public List<Integer> getIds() {
		return get(IDS);
	}
}
