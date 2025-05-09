package ru.imagebook.client.admin.ctl.bill;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteBillsMessage extends BaseMessage {
	private static final long serialVersionUID = -1553565202801325286L;

	public static final String IDS = "ids";

	DeleteBillsMessage() {}

	public DeleteBillsMessage(List<Integer> ids) {
		super(BillMessages.DELETE_BILLS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(IDS, ids);
	}

	public List<Integer> getIds() {
		return get(IDS);
	}
}
