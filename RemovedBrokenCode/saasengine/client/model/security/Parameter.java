package ru.saasengine.client.model.security;

import ru.minogin.core.client.bean.BasePersistentIdBean;
import ru.minogin.core.client.i18n.MultiString;

public class Parameter extends BasePersistentIdBean {
	private static final long serialVersionUID = 2950525427949141878L;

	private static final String TYPE_NAME = "Parameter";

	private static final String TITLE = "title";
	private static final String CLASS_ID = "classId";

	Parameter() {}

	Parameter(Parameter prototype) {
		super(prototype);
	}

	public Parameter(String id, MultiString title, String classId) {
		setId(id);
		setTitle(title);
		setClassId(classId);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public MultiString getTitle() {
		return get(TITLE);
	}

	public String getClassId() {
		return get(CLASS_ID);
	}

	public void setTitle(MultiString title) {
		set(TITLE, title);
	}

	public void setClassId(String classId) {
		set(CLASS_ID, classId);
	}

	@Override
	public Parameter copy() {
		return new Parameter(this);
	}
}
