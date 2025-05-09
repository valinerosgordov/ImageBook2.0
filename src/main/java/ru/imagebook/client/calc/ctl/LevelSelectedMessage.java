package ru.imagebook.client.calc.ctl;

import ru.minogin.core.client.flow.BaseMessage;

public class LevelSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = -5703868567253178451L;

	public static final String LEVEL = "level";

	public LevelSelectedMessage(int level) {
		super(CalcMessages.LEVEL_SELECTED);

		set(LEVEL, level);
	}

	public int getLevel() {
		return (Integer) get(LEVEL);
	}
}
