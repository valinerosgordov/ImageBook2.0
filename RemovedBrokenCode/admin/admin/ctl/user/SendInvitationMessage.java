package ru.imagebook.client.admin.ctl.user;

import java.util.List;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SendInvitationMessage extends BaseMessage {
	private static final long serialVersionUID = -109390488422251542L;
	
	public static final String USER_IDS = "userIds";

	SendInvitationMessage() {}

	public SendInvitationMessage(List<Integer> userIds) {
		super(UserMessages.SEND_INVITATION);
		
		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(USER_IDS, userIds);
	}

	public List<Integer> getUserIds() {
		return get(USER_IDS);
	}
}
