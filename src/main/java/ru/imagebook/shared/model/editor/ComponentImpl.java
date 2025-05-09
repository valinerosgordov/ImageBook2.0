package ru.imagebook.shared.model.editor;

import ru.minogin.core.client.bean.BaseEntityBean;

public abstract class ComponentImpl extends BaseEntityBean implements Component {
	private static final long serialVersionUID = 4689887259909567917L;

	public ComponentImpl() {
		setIndex(0);
		setBlocked(false);
	}

	public ComponentImpl(Component prototype) {
		setIndex(prototype.getIndex());
		setBlocked(prototype.isBlocked());
		setComponentId(prototype.getComponentId());
	}

	@Override
	public Integer getIndex() {
		return get(INDEX);
	}

	@Override
	public void setIndex(Integer index) {
		set(INDEX, index);
	}

	public boolean isBlocked() {
		return (Boolean) get(BLOCKED);
	}

	public void setBlocked(boolean blocked) {
		set(BLOCKED, blocked);
	}

	@Override
	public Integer getComponentId() {
		return get(COMPONENT_ID);
	}

	@Override
	public void setComponentId(Integer componentId) {
		set(COMPONENT_ID, componentId);
	}

	@Override
	public int compareTo(Component component) {
		return getIndex().compareTo(component.getIndex());
	}
}