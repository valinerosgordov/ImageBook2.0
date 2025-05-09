package ru.imagebook.client.admin.ctl.product;

import java.util.List;

import ru.imagebook.shared.model.Album;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadAlbumsResultMessage extends BaseMessage {
	private static final long serialVersionUID = 3883975900914085919L;

	public static final String ALBUMS = "albums";

	LoadAlbumsResultMessage() {}

	public LoadAlbumsResultMessage(List<Album> albums) {
		super(ProductMessages.LOAD_ALBUMS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ALBUMS, albums);
	}

	public List<Album> getAlbums() {
		return get(ALBUMS);
	}
}
