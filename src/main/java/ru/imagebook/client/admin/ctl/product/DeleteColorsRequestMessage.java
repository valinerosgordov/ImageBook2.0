package ru.imagebook.client.admin.ctl.product;

import java.util.List;

import ru.imagebook.shared.model.Color;
import ru.minogin.core.client.flow.BaseMessage;

public class DeleteColorsRequestMessage extends BaseMessage {
	private static final long serialVersionUID = 4689619793925936536L;
	
	public static final String COLORS = "colors";

	public DeleteColorsRequestMessage(List<Color> colors) {
		super(ProductMessages.DELETE_COLORS_REQUEST);

		set(COLORS, colors);
	}

	public List<Color> getColors() {
		return get(COLORS);
	}
}
