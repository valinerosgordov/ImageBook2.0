package ru.minogin.core.client.bean;

import java.util.Map;

public abstract class BasePersistentIdBean extends BaseIdBean implements PersistentIdBean {
	private static final long serialVersionUID = -2383771150442226377L;

	public BasePersistentIdBean() {}

	public BasePersistentIdBean(String id) {
		super(id);
	}

	public BasePersistentIdBean(BasePersistentIdBean prototype) {
		PersistenceSupport.copy(this, prototype);
	}

	@Override
	public void loadFrom(Map<String, Object> image) {
		PersistenceSupport.loadFrom(this, image);
	}

	@Override
	public void saveTo(Map<String, Object> image) {
		PersistenceSupport.saveTo(this, image);
	}
}
