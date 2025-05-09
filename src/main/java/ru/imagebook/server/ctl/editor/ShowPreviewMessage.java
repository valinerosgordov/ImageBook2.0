package ru.imagebook.server.ctl.editor;

import java.io.OutputStream;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ShowPreviewMessage extends BaseMessage {
	private static final long serialVersionUID = -4193394927201616350L;

	public static final String PATH = "path";
	public static final String OUTPUT_STREAM = "outputStream";

	public ShowPreviewMessage(String path, OutputStream outputStream) {
		super(RemoteEditorMessages.SHOW_PREVIEW);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(PATH, path);
		set(OUTPUT_STREAM, outputStream);
	}

	public String getPath() {
		return get(PATH);
	}

	public OutputStream getOutputStream() {
		return get(OUTPUT_STREAM);
	}
}