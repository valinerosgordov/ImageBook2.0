package ru.minogin.core.client.mvp;

public class XPlace extends com.google.gwt.place.shared.Place {
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj == this)
			return true;

		return getClass().equals(obj.getClass());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
