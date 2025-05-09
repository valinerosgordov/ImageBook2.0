package ru.imagebook.server.ctl.editor;

import java.io.OutputStream;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ShowComponentMessage extends BaseMessage {
	private static final long serialVersionUID = -4193394927201616350L;

	public static final String COMPONENT_ID = "componentId";
	public static final String OUTPUT_STREAM = "outputStream";

	public ShowComponentMessage(int componentId, OutputStream outputStream) {
		super(RemoteEditorMessages.SHOW_COMPONENT);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(COMPONENT_ID, componentId);
		set(OUTPUT_STREAM, outputStream);
	}

	public int getComponentId() {
		return (Integer) get(COMPONENT_ID);
	}

	public OutputStream getOutputStream() {
		return get(OUTPUT_STREAM);
	}
}