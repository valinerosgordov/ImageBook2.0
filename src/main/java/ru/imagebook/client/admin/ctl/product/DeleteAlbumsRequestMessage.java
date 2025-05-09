package ru.imagebook.client.admin.ctl.product;

import java.util.List;

import ru.imagebook.shared.model.Album;
import ru.minogin.core.client.flow.BaseMessage;

public class DeleteAlbumsRequestMessage extends BaseMessage {
	private static final long serialVersionUID = 6082809984606097668L;

	public static final String ALBUMS = "albums";

	public DeleteAlbumsRequestMessage(List<Album> albums) {
		super(ProductMessages.DELETE_ALBUMS_REQUEST);

		set(ALBUMS, albums);
	}

	public List<Album> getAlbums() {
		return get(ALBUMS);
	}
}
