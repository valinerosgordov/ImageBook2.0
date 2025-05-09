package ru.minogin.core.client.gxt;

import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.google.gwt.user.client.ui.Widget;

public class Block<T, F extends Widget> extends HorizontalPanel {
	private final T object;
	private final F field;
	private final CheckBox checkBox;

	public Block(T object, F field) {
		this.object = object;
		this.field = field;

		TableData data = new TableData();
		data.setVerticalAlign(VerticalAlignment.TOP);
		checkBox = new CheckBox();
		checkBox.setStyleAttribute("margin", "10px 5px 0px 0px");
		add(checkBox, data);

		add(field);
	}

	public T getObject() {
		return object;
	}
	
	public F getField() {
		return field;
	}

	public boolean isSelected() {
		return checkBox.getValue() == true;
	}
}
