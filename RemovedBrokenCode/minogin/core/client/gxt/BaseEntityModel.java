package ru.minogin.core.client.gxt;

import ru.minogin.core.shared.model.BaseEntity;

import com.extjs.gxt.ui.client.data.BaseModel;

public class BaseEntityModel<T extends BaseEntity> extends BaseModel {
	private static final long serialVersionUID = -9167847078985922680L;

	private final T entity;

	public BaseEntityModel(T entity) {
		this.entity = entity;
	}

	public T getEntity() {
		return entity;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof BaseEntityModel))
			return false;

		BaseEntityModel model = (BaseEntityModel) obj;
		return entity.equals(model.getEntity());
	}

	@Override
	public int hashCode() {
		return entity.hashCode();
	}
}
