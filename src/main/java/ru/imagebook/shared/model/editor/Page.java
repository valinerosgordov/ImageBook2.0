package ru.imagebook.shared.model.editor;

import java.util.Set;
import java.util.TreeSet;

import ru.minogin.core.client.bean.BaseEntityBean;

public class Page extends BaseEntityBean {
	private static final long serialVersionUID = -1495911031529857143L;

	public static final String TYPE = "type";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String X_MARGIN = "xMargin";
	public static final String Y_MARGIN = "yMargin";
	public static final String COMPONENTS = "components";
	public static final String BLOCKED = "blocked";
	public static final String COMMON = "common";

	public Page() {
		setComponents(new TreeSet<Component>());
		setBlocked(false);
		setCommon(true);
	}

	public Page(Page prototype) {
		this();

		setType(prototype.getType());
		setWidth(prototype.getWidth());
		setHeight(prototype.getHeight());
		setXMargin(prototype.getXMargin());
		setYMargin(prototype.getYMargin());
		for (Component component : prototype.getComponents()) {
			if (component instanceof Barcode)
				continue;

			getComponents().add(component.copy());
		}
		setBlocked(prototype.isBlocked());
		setCommon(prototype.isCommon());
	}

	public int getType() {
		return (Integer) get(TYPE);
	}

	public void setType(int type) {
		set(TYPE, type);
	}

	public float getWidth() {
		return (Float) get(WIDTH);
	}

	public void setWidth(float width) {
		set(WIDTH, width);
	}

	public float getHeight() {
		return (Float) get(HEIGHT);
	}

	public void setHeight(float height) {
		set(HEIGHT, height);
	}

	public float getXMargin() {
		return (Float) get(X_MARGIN);
	}

	public void setXMargin(float xMargin) {
		set(X_MARGIN, xMargin);
	}

	public float getYMargin() {
		return (Float) get(Y_MARGIN);
	}

	public void setYMargin(float yMargin) {
		set(Y_MARGIN, yMargin);
	}

	public Set<Component> getComponents() {
		return get(COMPONENTS);
	}

	public void setComponents(Set<Component> components) {
		set(COMPONENTS, components);
	}

	public void setMargins(float xMargin, float yMargin) {
		setXMargin(xMargin);
		setYMargin(yMargin);
	}

	public void setSize(float width, float height) {
		setWidth(width);
		setHeight(height);
	}

	public boolean isBlocked() {
		return (Boolean) get(BLOCKED);
	}

	public void setBlocked(boolean blocked) {
		set(BLOCKED, blocked);
	}

	public boolean isCommon() {
		return (Boolean) get(COMMON);
	}

	public void setCommon(boolean common) {
		set(COMMON, common);
	}

	public Page copy() {
		return new Page(this);
	}

	@SuppressWarnings("unchecked")
	public <C extends Component> C getComponent(Integer componentId) {
		if (componentId == null)
			throw new NullPointerException();

		for (Component component : getComponents()) {
			if (componentId.equals(component.getComponentId()))
				return (C) component;
		}
		return null;
	}

	public boolean isSpreadOrFlyLeafPage() {
		return getType() == PageType.SPREAD || isFlyLeaf();
	}

	public boolean isFlyLeaf() {
		return getType() == PageType.FLYLEAF;
	}
}
