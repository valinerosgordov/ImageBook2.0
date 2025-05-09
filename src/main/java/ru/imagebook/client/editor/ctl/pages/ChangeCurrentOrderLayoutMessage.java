package ru.imagebook.client.editor.ctl.pages;

import ru.imagebook.shared.model.editor.Layout;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class ChangeCurrentOrderLayoutMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String LAYOUT = "layout";

	ChangeCurrentOrderLayoutMessage() {}

	public ChangeCurrentOrderLayoutMessage(Layout layout) {
		super(PagesMessages.CHANGE_CURRENT_ORDER_LAYOUT);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(LAYOUT, layout);
	}

	public Layout getLayout() { return (Layout) get(LAYOUT); }
}
