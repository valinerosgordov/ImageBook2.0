package ru.saasengine.client.model.filter;

import ru.minogin.core.client.bean.BasePersistentIdBean;

public class Element extends BasePersistentIdBean {
	private static final long serialVersionUID = 7849254334014324774L;

	public static final String TYPE_NAME = "filter.Element";

	private static final String TYPE = "block";

	Element() {}

	Element(Element prototype) {
		super(prototype);
	}

	public Element(ElementType type, String id) {
		super(id);
		
		set(TYPE, type);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public ElementType getType() {
		return get(TYPE);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Element))
			return false;

		Element element = (Element) obj;
		return element.getType() == getType() && element.getId().equals(getId());
	}

	@Override
	public int hashCode() {
		return getType().hashCode() ^ getId().hashCode();
	}

	@Override
	public Element copy() {
		return new Element(this);
	}
}
