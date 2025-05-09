package ru.imagebook.client.admin.ctl.vendor;

import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class UpdateVendorMessage extends BaseMessage {
	private static final long serialVersionUID = -2630284121910193824L;
	
	public static final String AGENT = "agent";

	UpdateVendorMessage() {}

	public UpdateVendorMessage(Vendor agent) {
		super(VendorMessages.UPDATE_AGENT);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(AGENT, agent);
	}

	public Vendor getAgent() {
		return get(AGENT);
	}
}
