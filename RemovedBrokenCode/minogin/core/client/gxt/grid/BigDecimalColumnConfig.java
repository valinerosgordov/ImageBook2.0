package ru.minogin.core.client.gxt.grid;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.google.gwt.i18n.client.NumberFormat;

public class BigDecimalColumnConfig extends XColumnConfig {
	public BigDecimalColumnConfig(String id, String name, int width) {
		super(id, name, width);

		setAlignment(HorizontalAlignment.RIGHT);
		setNumberFormat(NumberFormat.getFormat("#,##0.00"));
	}
}
