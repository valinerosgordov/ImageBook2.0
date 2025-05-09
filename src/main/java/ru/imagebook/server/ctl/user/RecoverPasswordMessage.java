package ru.imagebook.server.ctl.user;

import java.io.Writer;

import ru.imagebook.shared.model.Module;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class RecoverPasswordMessage extends BaseMessage {
	private static final long serialVersionUID = -3670969879908478424L;

	public static final String USER_ID = "userId";
	public static final String CODE = "code";
	public static final String WRITER = "writer";
	public static final String MODULE = "module";

	public RecoverPasswordMessage(int userId, String code, Writer writer) {
		super(UserMessages.RECOVER_PASSWORD);

		addAspects(RemotingAspect.REMOTE);

		set(USER_ID, userId);
		set(CODE, code);
		set(WRITER, writer);
	}
	
	public RecoverPasswordMessage(int userId, String code, Writer writer, Module module) {
		this(userId, code, writer);
		
		set(MODULE, module.name());
	}

	public int getUserId() {
		return (Integer) get(USER_ID);
	}

	public String getCode() {
		return get(CODE);
	}

	public Writer getWriter() {
		return get(WRITER);
	}
	
	public String getModule() {
		return get(MODULE);
	}
}
