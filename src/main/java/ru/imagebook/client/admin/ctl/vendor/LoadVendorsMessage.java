package ru.imagebook.client.admin.ctl.vendor;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadVendorsMessage extends BaseMessage {
	private static final long serialVersionUID = 4347380037707894434L;

	public LoadVendorsMessage() {
		super(VendorMessages.LOAD_VENDORS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
