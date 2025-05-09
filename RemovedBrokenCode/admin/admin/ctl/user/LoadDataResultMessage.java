package ru.imagebook.client.admin.ctl.user;

import java.util.List;

import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadDataResultMessage extends BaseMessage {
	private static final long serialVersionUID = -8100237044006749415L;

	public static final String VENDORS = "vendors";

	LoadDataResultMessage() {}

	public LoadDataResultMessage(List<Vendor> vendors) {
		super(UserMessages.LOAD_DATA_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(VENDORS, vendors);
	}

	public List<Vendor> getVendors() {
		return get(VENDORS);
	}
}
