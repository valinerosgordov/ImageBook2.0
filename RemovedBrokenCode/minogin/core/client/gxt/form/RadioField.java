package ru.minogin.core.client.gxt.form;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;

public class RadioField<T> extends RadioGroup {
	private static final String VALUE = "value";

	public void add(T value, String text) {
		Radio radio = new Radio();
		radio.setBoxLabel(text);
		radio.setData(VALUE, value);
		add(radio);
	}

	public void add(T value) {
		add(value, value.toString());
	}

	@SuppressWarnings("unchecked")
	public T getXValue() {
		Radio radio = getValue();
		if (radio == null)
			return null;
		return (T) radio.getData(VALUE);
	}

	@SuppressWarnings("unchecked")
	public void setXValue(T value) {
		for (Field<?> field : getAll()) {
			Radio radio = (Radio) field;
			T radioValue = (T) radio.getData(VALUE);
			if (radioValue.equals(value))
				setValue(radio);
		}
	}
}
