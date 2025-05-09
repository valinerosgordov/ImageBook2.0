package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddImageMessage extends BaseMessage {
	private static final long serialVersionUID = -4336939775292503651L;

	public static final String PATH = "path";
	public static final String TYPE = "type";
	public static final String PAGE_NUMBER = "pageNumber";
	public static final String VARIANT = "variant";

	AddImageMessage() {}

	public AddImageMessage(int type) {
		super(FileMessages.ADD_IMAGE);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(TYPE, type);
	}

	public String getPath() {
		return get(PATH);
	}

	public void setPath(String path) {
		set(PATH, path);
	}

	public int getType() {
		return (Integer) get(TYPE);
	}

	public int getPageNumber() {
		return (Integer) get(PAGE_NUMBER);
	}

	public void setPageNumber(int pageNumber) {
		set(PAGE_NUMBER, pageNumber);
	}

	public int getVariant() {
		return (Integer) get(VARIANT);
	}

	public void setVariant(int variant) {
		set(VARIANT, variant);
	}
}
