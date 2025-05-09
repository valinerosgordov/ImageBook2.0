package ru.minogin.ui.client.activetree;

import com.google.gwt.user.client.ui.Widget;

public interface TreeRenderer<T> {
	Widget render(T value);
}
