package ru.imagebook.client.admin.ctl.product;

import ru.imagebook.shared.model.Album;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class UpdateAlbumMessage extends BaseMessage {
	private static final long serialVersionUID = 8555811257413333983L;
	
	public static final String ALBUM = "album";

	UpdateAlbumMessage() {}

	public UpdateAlbumMessage(Album album) {
		super(ProductMessages.UPDATE_ALBUM);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ALBUM, album);
	}

	public Album getAlbum() {
		return get(ALBUM);
	}
}
