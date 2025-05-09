package ru.minogin.ui.client.list;

import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public interface ActiveListRenderer<T> {
	List<? extends Widget> render(T value);
}
