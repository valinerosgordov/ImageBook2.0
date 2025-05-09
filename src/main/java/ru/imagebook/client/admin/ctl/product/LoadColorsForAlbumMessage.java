package ru.imagebook.client.admin.ctl.product;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class LoadColorsForAlbumMessage extends BaseMessage {
	private static final long serialVersionUID = 3901614160190963247L;

	public LoadColorsForAlbumMessage() {
		super(ProductMessages.LOAD_COLORS_FOR_ALBUM);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);
	}
}
