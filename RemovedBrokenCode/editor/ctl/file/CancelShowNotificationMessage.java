package ru.imagebook.client.editor.ctl.file;

import ru.imagebook.client.editor.ctl.file.FileMessages;
import ru.imagebook.shared.model.editor.NotificationType;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class CancelShowNotificationMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String TYPE = "type";

	CancelShowNotificationMessage() {}

	public CancelShowNotificationMessage(int type) {
		super(FileMessages.CANCEL_SHOW_NOTIFICATION);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(TYPE, type);
	}

	public Integer getType() {
		return (Integer)get(TYPE);
	}

	public void setPath(Integer type) {
		set(TYPE, type);
	}
}
