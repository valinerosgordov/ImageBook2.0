package ru.imagebook.shared.service.app;

import java.util.Date;

public class IncorrectPeriodCodeError extends RuntimeException {
	private Date bonusActionDateStart;
    private Date bonusActionDateEnd;

	public IncorrectPeriodCodeError() {
		super();
	}

    public IncorrectPeriodCodeError(Date bonusActionDateStart, Date bonusActionDateEnd) {
        this.bonusActionDateStart = bonusActionDateStart;
        this.bonusActionDateEnd = bonusActionDateEnd;
    }

    public Date getBonusActionDateStart() {
        return bonusActionDateStart;
    }

    public Date getBonusActionDateEnd() {
        return bonusActionDateEnd;
    }
}
