package ru.imagebook.client.admin.ctl.product;

import java.util.List;

import ru.imagebook.shared.model.Color;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadColorsForAlbumResultMessage extends BaseMessage {
	private static final long serialVersionUID = -3606250590078132575L;

	public static final String COLORS = "colors";

	LoadColorsForAlbumResultMessage() {}

	public LoadColorsForAlbumResultMessage(List<Color> colors) {
		super(ProductMessages.LOAD_COLORS_FOR_ALBUM_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(COLORS, colors);
	}

	public List<Color> getColors() {
		return get(COLORS);
	}
}
