package ru.minogin.core.client.gxt.grid;

import ru.minogin.core.client.gxt.form.SelectField;
import ru.minogin.core.client.gxt.form.SelectValue;

import com.extjs.gxt.ui.client.widget.grid.CellEditor;

public class SelectFieldCellEditor<T> extends CellEditor {
	private final SelectField<T> field;

	public SelectFieldCellEditor(SelectField<T> field) {
		super(field);

		this.field = field;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object preProcessValue(Object object) {
		return field.find((T) object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object postProcessValue(Object object) {
		return ((SelectValue<T>) object).getValue();
	}
}
