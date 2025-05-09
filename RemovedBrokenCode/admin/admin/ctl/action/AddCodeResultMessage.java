package ru.imagebook.client.admin.ctl.action;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class AddCodeResultMessage extends BaseMessage {
	private static final long serialVersionUID = -6602666830546887352L;
	
	public static final String HAVE_ADDED = "haveAdded";

	public AddCodeResultMessage() {
	}

	public AddCodeResultMessage(boolean haveAdded) {
		super(ActionMessages.ADD_CODE_RESULT);

		addAspects(RemotingAspect.CLIENT);
		set(HAVE_ADDED, haveAdded);
	}

	public Boolean getResult() {
		return get(HAVE_ADDED);
	}
}
