package ru.minogin.core.client.gxt.form;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.minogin.core.client.text.StringUtil;

import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.ListViewEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.user.client.Element;

public class MultiSelectField<T> extends ComboBox<SelectValue<T>> {
	private ListStore<SelectValue<T>> store;
	private CheckBoxListView<SelectValue<T>> view;
	private SelectValue<T> allModel;

	public MultiSelectField() {
		super();

		view = new CheckBoxListView<SelectValue<T>>();
		setView(view);
		store = new ListStore<SelectValue<T>>();
		setStore(store);
		setTriggerAction(TriggerAction.ALL);
		setEditable(false);

		String spacing = GXT.isIE ? "0" : "3";
		setTemplate(XTemplate
				.create("<tpl for=\".\"><div class='x-view-item x-view-item-check'><table cellspacing='"
						+ spacing
						+ "' cellpadding=0><tr><td><input class=\"x-view-item-checkbox\" type=\"checkbox\" /></td><td><td>{encoded}</td></tr></table></div></tpl>"));
		setItemSelector(".x-view-item");

		allModel = new SelectValue<T>(null, null);
		allModel.setEncoded("<i>Отметить/сбросить все</i>");
		store.add(allModel);

		setLazyRender(false);
	}

	public MultiSelectField(String label, boolean allowBlank, LayoutContainer container) {
		this();

		setFieldLabel(label);
		setAllowBlank(allowBlank);
		container.add(this);
	}

	@Override
	protected void initList() {
		super.initList();

		getView().removeAllListeners();
		getView().addListener(Events.OnClick, new Listener<ListViewEvent<SelectValue<T>>>() {
			@Override
			public void handleEvent(ListViewEvent<SelectValue<T>> e) {
				if (e.getTarget().getTagName().equals("INPUT")) {
					SelectValue<T> model = e.getModel();
					if (model == allModel) {
						if (view.getChecked().contains(allModel)) {
							for (SelectValue<T> item : store.getModels()) {
								if (item != allModel)
									view.setChecked(item, true);
							}
						}
						else {
							for (SelectValue<T> item : store.getModels()) {
								if (item != allModel)
									view.setChecked(item, false);
							}
						}
					}

					setText();
				}
				else {
					SelectValue<T> model = e.getModel();
					if (model != null) {
						boolean checked = view.getChecked().contains(model);
						view.setChecked(model, !checked);

						if (model == allModel) {
							if (view.getChecked().contains(allModel)) {
								for (SelectValue<T> item : store.getModels()) {
									if (item != allModel)
										view.setChecked(item, true);
								}
							}
							else {
								for (SelectValue<T> item : store.getModels()) {
									if (item != allModel)
										view.setChecked(item, false);
								}
							}
						}

						setText();
					}
				}
			}
		});
	}

	public void add(T value, String text) {
		SelectValue<T> model = new SelectValue<T>(value, text);
		store.add(model);
	}

	public void add(T value) {
		add(value, value.toString());
	}

	public List<T> getXValue() {
		List<T> value = new ArrayList<T>();
		for (SelectValue<T> item : view.getChecked()) {
			if (item != allModel)
				value.add(item.getValue());
		}
		return value;
	}

	public Set<T> getXValueSet() {
		Set<T> value = new HashSet<T>();
		for (SelectValue<T> item : view.getChecked()) {
			if (item != allModel)
				value.add(item.getValue());
		}
		return value;
	}

	public void setXValue(Collection<T> value) {
		if (value == null)
			throw new NullPointerException();

		for (SelectValue<T> item : view.getChecked()) {
			view.setChecked(item, false);
		}

		for (T t : value) {
			SelectValue<T> model = store.findModel(SelectValue.VALUE, t);
			view.setChecked(model, true);
		}

		if (!value.isEmpty()) {
			view.setChecked(allModel, true);
		}

		setText();
	}

	private void setText() {
		List<String> texts = new ArrayList<String>();
		for (SelectValue<T> item : view.getChecked()) {
			texts.add(item.getText());
		}
		setRawValue(StringUtil.implode(", ", texts));
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

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);

		setText();
	}

	@Override
	protected boolean validateValue(String value) {
		if (!getAllowBlank()) {
			if (getXValue().isEmpty()) {
				markInvalid(getMessages().getBlankText());
				return false;
			}
		}
		return true;
	}
}
