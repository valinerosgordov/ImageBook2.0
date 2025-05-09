package ru.saasengine.client.model.filter;

import java.util.ArrayList;
import java.util.List;

import ru.minogin.core.client.bean.BasePersistentBean;

public class Path extends BasePersistentBean {
	private static final long serialVersionUID = 5611508892910301047L;

	public static final String TYPE_NAME = "filter.Path";

	private static final String ELEMENTS = "elements";

	public Path() {
		set(ELEMENTS, new ArrayList<Element>());
	}

	public Path(Path prototype) {
		super(prototype);
	}

	@Override
	public String getTypeName() {
		return TYPE_NAME;
	}

	public List<Element> getElements() {
		return get(ELEMENTS);
	}

	public boolean add(Element e) {
		return getElements().add(e);
	}

	public Element getLastElement() {
		if (getElements().isEmpty())
			return null;

		return getElements().get(getElements().size() - 1);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Path))
			return false;

		return ((Path) obj).getElements().equals(getElements());
	}

	@Override
	public int hashCode() {
		return getElements().hashCode();
	}

	@Override
	public Path copy() {
		return new Path(this);
	}
}
