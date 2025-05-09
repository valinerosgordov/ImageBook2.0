package ru.imagebook.client.admin.ctl.action;

import ru.minogin.core.client.flow.BaseMessage;

public class LoadVendorActionsMessage extends BaseMessage {
	private static final long serialVersionUID = -2137402001142042734L;
	private static final String VENDOR = "vendor";
	
	LoadVendorActionsMessage() {}
	
	public LoadVendorActionsMessage(Integer vendorId) {
		super(ActionMessages.LOAD_VENDOR_ACTIONS);

		set(VENDOR, vendorId);
	}

	public Integer getVendorId() {
		return get(VENDOR);
	}
}
