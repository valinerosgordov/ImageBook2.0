package ru.imagebook.client.admin.ctl.product;

import java.util.List;

import ru.imagebook.shared.model.Color;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class DeleteColorsMessage extends BaseMessage {
	private static final long serialVersionUID = -3756228001467808035L;
	
	public static final String COLORS = "colors";

	DeleteColorsMessage() {}

	public DeleteColorsMessage(List<Color> colors) {
		super(ProductMessages.DELETE_COLORS);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(COLORS, colors);
	}

	public List<Color> getColors() {
		return get(COLORS);
	}
}
