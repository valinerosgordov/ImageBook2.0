package ru.minogin.core.client.gxt.grid;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;

public class IntegerColumnConfig extends XColumnConfig {
	public IntegerColumnConfig(String id, String name, int width) {
		super(id, name, width);

		setAlignment(HorizontalAlignment.RIGHT);
	}
}
