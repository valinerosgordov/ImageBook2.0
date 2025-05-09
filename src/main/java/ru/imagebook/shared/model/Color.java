package ru.imagebook.shared.model;

import ru.minogin.core.client.bean.BaseEntityBean;
import ru.minogin.core.client.i18n.MultiString;

public class Color extends BaseEntityBean {
	private static final long serialVersionUID = -824565679563112335L;

	public static final String NUMBER = "number";
	public static final String NAME = "name";

	public Color() {}

	public Color(Integer number, MultiString name) {
		setNumber(number);
		setName(name);
	}

	public Integer getNumber() {
		return get(NUMBER);
	}

	public void setNumber(Integer number) {
		set(NUMBER, number);
	}

	public MultiString getName() {
		return get(NAME);
	}

	public void setName(MultiString name) {
		set(NAME, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		if (!(obj instanceof Color))
			return false;

		Color color = (Color) obj;
		if (color.getId() == null || getId() == null)
			return false;
		
		return color.getId().equals(getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}
}
