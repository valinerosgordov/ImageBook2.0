package ru.imagebook.client.admin.ctl.bill;

import ru.imagebook.shared.model.BillFilter;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadBillsMessage extends BaseMessage {
	private static final long serialVersionUID = 5893129867209716613L;

	public static final String OFFSET = "offset";
	public static final String LIMIT = "limit";
	public static final String FILTER = "filter";

	LoadBillsMessage() {}

	public LoadBillsMessage(int offset, int limit, BillFilter billFilter) {
		super(BillMessages.LOAD_BILLS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(OFFSET, offset);
		set(LIMIT, limit);
		set(FILTER, billFilter);
	}

	public int getOffset() {
		return (Integer) get(OFFSET);
	}

	public int getLimit() {
		return (Integer) get(LIMIT);
	}

	public BillFilter getBillFilter() { return  (BillFilter) get(FILTER); }
}
