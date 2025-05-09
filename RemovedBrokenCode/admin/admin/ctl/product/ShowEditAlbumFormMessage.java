package ru.imagebook.client.admin.ctl.product;

import ru.imagebook.shared.model.Album;
import ru.minogin.core.client.flow.BaseMessage;

public class ShowEditAlbumFormMessage extends BaseMessage {
	private static final long serialVersionUID = 5136061139883625522L;

	public static final String ALBUM = "album";

	public ShowEditAlbumFormMessage(Album album) {
		super(ProductMessages.SHOW_EDIT_ALBUM_FORM);

		set(ALBUM, album);
	}

	public Album getAlbum() {
		return get(ALBUM);
	}
}
