package ru.imagebook.client.admin.ctl.bill;

import java.util.List;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.BillFilter;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadBillsResultMessage extends BaseMessage {
	private static final long serialVersionUID = 5893129867209716613L;

	public static final String BILLS = "bills";
	public static final String OFFSET = "offset";
	public static final String TOTAL = "total";
	public static final String FILTER = "filter";

	LoadBillsResultMessage() {}

	public LoadBillsResultMessage(List<Bill> bills, int offset, long total, BillFilter billFilter) {
		super(BillMessages.LOAD_BILLS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(BILLS, bills);
		set(OFFSET, offset);
		set(TOTAL, total);
		set(FILTER, billFilter);
	}

	public List<Bill> getBills() {
		return get(BILLS);
	}

	public int getOffset() {
		return (Integer) get(OFFSET);
	}

	public long getTotal() {
		return (Long) get(TOTAL);
	}

	public BillFilter getBillFilter() { return  (BillFilter) get(FILTER); }
}
