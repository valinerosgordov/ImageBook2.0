package ru.minogin.ui.client.list;

import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public interface ActiveListHeaderRenderer {
	List<? extends Widget> render();
}
