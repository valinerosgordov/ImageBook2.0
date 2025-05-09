package ru.imagebook.client.common.ctl.user;

import ru.imagebook.shared.model.Module;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class RecoverPasswordMessage extends BaseMessage {
	private static final long serialVersionUID = -8799943046685046192L;

	public static final String EMAIL = "email";
	public static final String MODULE = "module";

	RecoverPasswordMessage() {}

	public RecoverPasswordMessage(String email) {
		super(UserMessages.RECOVER_PASSWORD);

		addAspects(RemotingAspect.REMOTE);

		set(EMAIL, email);
	}
	
	public RecoverPasswordMessage(String email, Module module) {
		this(email);
		
		set(MODULE, module.name());		
	}

	public String getEmail() {
		return get(EMAIL);
	}
	
	public String getModule() {
		return get(MODULE);
	}
}
