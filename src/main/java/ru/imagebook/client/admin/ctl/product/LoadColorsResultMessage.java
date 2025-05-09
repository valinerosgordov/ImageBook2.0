package ru.imagebook.client.admin.ctl.product;

import java.util.List;

import ru.imagebook.shared.model.Color;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadColorsResultMessage extends BaseMessage {
	private static final long serialVersionUID = -2364949772439152318L;
	
	public static final String COLORS = "colors";

	LoadColorsResultMessage() {}

	public LoadColorsResultMessage(List<Color> colors) {
		super(ProductMessages.LOAD_COLORS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(COLORS, colors);
	}

	public List<Color> getColors() {
		return get(COLORS);
	}
}
