package ru.imagebook.shared.model.editor;

import ru.minogin.core.client.bean.BaseEntityBean;

public class EditorCounter extends BaseEntityBean {
	private static final long serialVersionUID = 2224570280544469408L;

	public static final String NUMBER = "number";

	public int getNumber() {
		return (Integer) get(NUMBER);
	}

	public void setNumber(int number) {
		set(NUMBER, number);
	}
}
