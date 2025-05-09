package ru.imagebook.client.admin.ctl.vendor;

import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddVendorMessage extends BaseMessage {
	private static final long serialVersionUID = 4034002464632426385L;

	public static final String VENDOR = "vendor";

	AddVendorMessage() {}

	public AddVendorMessage(Vendor agent) {
		super(VendorMessages.ADD_VENDOR);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(VENDOR, agent);
	}

	public Vendor getVendor() {
		return get(VENDOR);
	}
}
