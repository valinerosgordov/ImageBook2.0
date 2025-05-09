package ru.saasengine.client.ctl.desktop;

import ru.minogin.core.client.flow.BaseMessage;

public class CloseWindowMessage extends BaseMessage {
	private static final long serialVersionUID = 4358616334982919051L;

	public CloseWindowMessage() {
		super(WindowMessages.CLOSE_WINDOW);

		addAspects(WindowAspect.WINDOW);
	}
}
