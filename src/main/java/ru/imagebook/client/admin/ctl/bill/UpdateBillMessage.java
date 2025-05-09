package ru.imagebook.client.admin.ctl.bill;

import ru.imagebook.shared.model.Bill;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class UpdateBillMessage extends BaseMessage {
	private static final long serialVersionUID = 8897314430984975899L;

	public static final String BILL = "bill";

	UpdateBillMessage() {}

	public UpdateBillMessage(Bill bill) {
		super(BillMessages.UPDATE_BILL);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(BILL, bill);
	}

	public Bill getBill() {
		return get(BILL);
	}
}
