package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.imagebook.shared.model.Album;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadDirSectionDataResultMessage extends BaseMessage {
	private static final long serialVersionUID = 6785815280628283055L;

	public static final String ALBUMS = "albums";

	LoadDirSectionDataResultMessage() {}

	public LoadDirSectionDataResultMessage(List<Album> albums) {
		super(SiteMessages.LOAD_DIR_SECTION_DATA_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ALBUMS, albums);
	}

	public List<Album> getAlbums() {
		return get(ALBUMS);
	}
}
