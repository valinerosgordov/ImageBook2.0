package ru.imagebook.client.admin.ctl.bill;

import ru.imagebook.shared.model.Bill;
import ru.minogin.core.client.flow.BaseMessage;

public class BillSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = -943247565131445188L;

	public static final String BILL = "bill";

	public BillSelectedMessage(Bill bill) {
		super(BillMessages.BILL_SELECTED);

		set(BILL, bill);
	}

	public Bill getBill() {
		return get(BILL);
	}
}
