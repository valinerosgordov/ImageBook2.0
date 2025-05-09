package ru.imagebook.client.admin.ctl.action;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.BonusAction;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

import java.util.List;

public class LoadAlbumsResultForBonusActionMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String ALBUMS = "albums";
	public static final String BONUS_ACTION = "action";

	LoadAlbumsResultForBonusActionMessage() {}

	public LoadAlbumsResultForBonusActionMessage(List<Album> albums, BonusAction bonusAction) {
		super(ActionMessages.LOAD_ALBUMS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ALBUMS, albums);
		set(BONUS_ACTION, bonusAction);
	}

	public BonusAction getBonusAction() {
		return get(BONUS_ACTION);
	}

	public List<Album> getAlbums() {
		return get(ALBUMS);
	}
}
