package ru.imagebook.client.calc.ctl;

import ru.imagebook.shared.model.Color;
import ru.minogin.core.client.flow.BaseMessage;

public class ColorSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = -6669499103464476699L;
	
	public static final String COLOR = "color";

	public ColorSelectedMessage(Color color) {
		super(CalcMessages.COLOR_SELECTED);

		set(COLOR, color);
	}

	public Color getColor() {
		return get(COLOR);
	}
}
