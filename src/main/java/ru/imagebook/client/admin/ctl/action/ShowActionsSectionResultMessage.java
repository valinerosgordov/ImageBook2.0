package ru.imagebook.client.admin.ctl.action;

import java.util.List;

import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ShowActionsSectionResultMessage extends BaseMessage {
	private static final long serialVersionUID = 6192888185508908621L;
	public static final String VENDORS = "vendors";

	ShowActionsSectionResultMessage() {}

	public ShowActionsSectionResultMessage(List<Vendor> vendors) {
		super(ActionMessages.SHOW_ACTIONS_SECTION_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(VENDORS, vendors);
	}

	public List<Vendor> getVendors() {
		return get(VENDORS);
	}
}
