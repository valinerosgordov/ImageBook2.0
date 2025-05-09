package ru.imagebook.client.admin.ctl.action;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadActionsMessage extends BaseMessage {
	private static final long serialVersionUID = 2362856891892309584L;
	private static final String VENDOR = "vendor";
	private static final String QUERY = "query";
	
	LoadActionsMessage() {}
	
	public LoadActionsMessage(Integer vendorId, final String query) {
		super(ActionMessages.LOAD_ACTIONS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
		
		set(VENDOR, vendorId);
		set(QUERY, query);
	}
	
	public Integer getVendorId() {
		return get(VENDOR);
	}

	public String getQuery() { return get(QUERY); }
}
