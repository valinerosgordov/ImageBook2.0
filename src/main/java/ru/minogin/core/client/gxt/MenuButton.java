package ru.minogin.core.client.gxt;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class MenuButton extends Button {
	public MenuButton(String text, AbstractImagePrototype icon) {
		super(text, icon);
	}

	public MenuButton(String text) {
		super(text);
	}

	@Override
	protected String getMenuClass() {
		return "";
	}
}
