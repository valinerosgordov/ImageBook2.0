package ru.imagebook.client.admin.ctl.vendor;

import java.util.List;

import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadVendorsResultMessage extends BaseMessage {
	private static final long serialVersionUID = -1423229152307167567L;

	public static final String AGENTS = "agents";

	LoadVendorsResultMessage() {}

	public LoadVendorsResultMessage(List<Vendor> agents) {
		super(VendorMessages.LOAD_AGENTS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(AGENTS, agents);
	}

	public List<Vendor> getAgents() {
		return get(AGENTS);
	}
}
