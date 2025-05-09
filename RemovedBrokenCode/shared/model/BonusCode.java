package ru.imagebook.shared.model;

import ru.minogin.core.client.bean.BaseEntityBean;

public class BonusCode extends BaseEntityBean implements Comparable<BonusCode> {
	private static final long serialVersionUID = -8864215419143322429L;

	public static final String ACTION = "action";
	public static final String NUMBER = "number";
	public static final String CODE = "code";

	public BonusCode() {
		setNumber(0);
	}

	public BonusAction getAction() {
		return get(ACTION);
	}

	public void setAction(BonusAction action) {
		set(ACTION, action);
	}

	public Integer getNumber() {
		return get(NUMBER);
	}

	public void setNumber(Integer number) {
		set(NUMBER, number);
	}

	public String getCode() {
		return get(CODE);
	}

	public void setCode(String code) {
		set(CODE, code);
	}

	public String getName() {
		return getAction().getName() + " - " + getCode();
	}

	@Override
	public int compareTo(BonusCode code) {
		return getNumber().compareTo(code.getNumber());
	}
}
