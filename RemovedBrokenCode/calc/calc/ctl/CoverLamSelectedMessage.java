package ru.imagebook.client.calc.ctl;

import ru.minogin.core.client.flow.BaseMessage;

public class CoverLamSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = 4970582926934182060L;

	public static final String COVER_LAM = "coverLam";

	public CoverLamSelectedMessage(int coverLam) {
		super(CalcMessages.COVER_LAM_SELECTED);

		set(COVER_LAM, coverLam);
	}

	public int getCoverLam() {
		return (Integer) get(COVER_LAM);
	}
}
