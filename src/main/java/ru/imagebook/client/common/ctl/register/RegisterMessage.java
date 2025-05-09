package ru.imagebook.client.common.ctl.register;

import ru.imagebook.shared.model.Module;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class RegisterMessage extends BaseMessage {
	private static final long serialVersionUID = -7633719440433755754L;

	public static final String NAME = "name";
	public static final String EMAIL = "email";
	public static final String CAPTCHA = "captcha";
	public static final String MODULE = "module";

	RegisterMessage() {}
	
	public RegisterMessage(String name, String email, String captcha, Module module) {
		super(RegisterMessages.REGISTER);

		addAspects(RemotingAspect.REMOTE);

		set(NAME, name);
		set(EMAIL, email);
		set(CAPTCHA, captcha);
		set(MODULE, module.name());
	}

	public String getName() {
		return get(NAME);
	}

	public String getEmail() {
		return get(EMAIL);
	}

	public String getCaptcha() {
		return get(CAPTCHA);
	}
	
	public String getModule() {
		return get(MODULE);
	}
}
