package ru.imagebook.client.editor.ctl.order;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadProductsMessage extends BaseMessage {
	private static final long serialVersionUID = 3834518031692710793L;

	public LoadProductsMessage() {
		super(OrderMessages.LOAD_PRODUCTS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
