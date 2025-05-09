package ru.imagebook.client.admin.ctl.product;

import ru.imagebook.shared.model.Album;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class AddAlbumMessage extends BaseMessage {
	private static final long serialVersionUID = 5003409332866457500L;

	public static final String ALBUM = "album";

	AddAlbumMessage() {}

	public AddAlbumMessage(Album album) {
		super(ProductMessages.ADD_ALBUM);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(ALBUM, album);
	}

	public Album getAlbum() {
		return get(ALBUM);
	}
}
