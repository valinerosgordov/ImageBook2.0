package ru.saasengine.client.model.ui;

public class ShowObjectsAction extends Action {
	private static final long serialVersionUID = 6273037318796433377L;

	public static final String TYPE_NAME = "ShowObjectsAction";

	private static final String CLASS_ID = "classId";

	ShowObjectsAction() {}

	public ShowObjectsAction(ShowObjectsAction prototype) {
		super(prototype);
	}

	public ShowObjectsAction(String classId) {
		super(TYPE_NAME);

		set(CLASS_ID, classId);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public String getClassId() {
		return get(CLASS_ID);
	}
	
	@Override
	public ShowObjectsAction copy() {
		return new ShowObjectsAction(this);
	}
}
