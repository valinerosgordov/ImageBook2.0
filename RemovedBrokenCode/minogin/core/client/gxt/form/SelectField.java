package ru.minogin.core.client.gxt.form;


import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class SelectField<T> extends ComboBox<SelectValue<T>> {
	public SelectField() {
		setStore(new ListStore<SelectValue<T>>());
		setTriggerAction(TriggerAction.ALL);
		setEditable(false);
		setTemplate("<tpl for=\".\"><div class=x-combo-list-item>{encoded}</div></tpl>");
	}

	public SelectField(String label, boolean allowBlank, LayoutContainer container) {
		this();

		setFieldLabel(label);
		setAllowBlank(allowBlank);
		container.add(this);
	}

	public void add(T value, String text) {
		store.add(new SelectValue<T>(value, text));
	}

	public void add(T value) {
		add(value, value.toString());
	}

	public T getXValue() {
		SelectValue<T> value = getValue();
		if (value != null)
			return value.getValue();
		else
			return null;
	}

	public void setXValue(T value) {
		SelectValue<T> model = find(value);
		setValue(model);
	}

	public void selectIfOne() {
		if (store.getCount() == 1)
			setValue(store.getAt(0));
	}

	public void removeAll() {
		store.removeAll();
	}

	public void selectFirst() {
		setValue(store.getAt(0));
	}

	public SelectValue<T> find(T value) {
		return store.findModel(SelectValue.VALUE, value);
	}
}
