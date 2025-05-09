package ru.imagebook.client.admin.ctl.product;

import java.util.List;

import ru.imagebook.shared.model.Album;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteAlbumsMessage extends BaseMessage {
	private static final long serialVersionUID = 4070881606275455798L;

	public static final String ALBUMS = "albums";
	public static final String ALBUM_IDS = "albumIds";

	DeleteAlbumsMessage() {}

	public DeleteAlbumsMessage(List<Album> albums) {
		super(ProductMessages.DELETE_ALBUMS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		setTransient(ALBUMS, albums);
	}

	public List<Album> getAlbums() {
		return get(ALBUMS);
	}

	public void setAlbumIds(List<Integer> albumIds) {
		set(ALBUM_IDS, albumIds);
	}

	public List<Integer> getAlbumIds() {
		return get(ALBUM_IDS);
	}
}
