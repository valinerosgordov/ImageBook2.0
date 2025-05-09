package ru.imagebook.client.admin.ctl.product;

import java.util.List;

import ru.imagebook.shared.model.Color;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class UpdateColorsMessage extends BaseMessage {
	private static final long serialVersionUID = 5637610644203362737L;

	public static final String COLORS = "colors";

	UpdateColorsMessage() {}

	public UpdateColorsMessage(List<Color> colors) {
		super(ProductMessages.UPDATE_COLORS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(COLORS, colors);
	}

	public List<Color> getColors() {
		return get(COLORS);
	}
}
