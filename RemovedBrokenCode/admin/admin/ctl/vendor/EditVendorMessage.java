package ru.imagebook.client.admin.ctl.vendor;

import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.flow.BaseMessage;

public class EditVendorMessage extends BaseMessage {
	private static final long serialVersionUID = -4844992130897543873L;

	public static final String AGENT = "agent";

	public EditVendorMessage(Vendor agent) {
		super(VendorMessages.EDIT_AGENT);
		
		set(AGENT, agent);
	}

	public Vendor getAgent() {
		return get(AGENT);
	}
}
