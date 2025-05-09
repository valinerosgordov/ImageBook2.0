package ru.minogin.core.client.gxt.form;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ModelProcessor;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class ObjectField<T> extends ComboBox<ObjectModel<T>> {
	private T value;

	public ObjectField() {
		DataProxy<PagingLoadResult<ObjectModel<T>>> proxy = new DataProxy<PagingLoadResult<ObjectModel<T>>>() {
			@Override
			public void load(DataReader<PagingLoadResult<ObjectModel<T>>> reader,
					Object loadConfig,
					final AsyncCallback<PagingLoadResult<ObjectModel<T>>> callback) {
				PagingLoadConfig config = (PagingLoadConfig) loadConfig;
				String query = config.get("query");
				ObjectField.this.load(config.getOffset(), config.getLimit(), query,
						new ObjectFieldCallback<T>() {
							@Override
							public void onLoaded(List<T> objects, int offset, long total) {
								List<ObjectModel<T>> rows = new ArrayList<ObjectModel<T>>();
								if (offset == 0 && getAllowBlank()) {
									rows.add(new ObjectModel<T>(null));
								}
								for (T object : objects) {
									rows.add(new ObjectModel<T>(object));
								}
								PagingLoadResult<ObjectModel<T>> result = new BasePagingLoadResult<ObjectModel<T>>(
										rows, offset, (int) total);
								callback.onSuccess(result);
							}
						});
			}
		};

		PagingLoader<PagingLoadResult<ObjectModel<T>>> pagingLoader = new BasePagingLoader<PagingLoadResult<ObjectModel<T>>>(
				proxy);
		ListStore<ObjectModel<T>> store = new ListStore<ObjectModel<T>>(
				pagingLoader);
		setStore(store);
		setPageSize(10);
		setMinChars(0);
		setTriggerAction(TriggerAction.ALL);

		getView().setModelProcessor(new ModelProcessor<ObjectModel<T>>() {
			@Override
			public ObjectModel<T> prepareData(ObjectModel<T> model) {
				T object = model.getObject();
				String text;
				if (object != null) {
					text = render(object);
				} else {					
					text = "-";
				}
				model.setText(text);
				return model;
			}
		});
	}

	public ObjectField(String label, boolean allowBlank, LayoutContainer container) {
		this();

		setFieldLabel(label + (allowBlank ? "" : " *"));
		setAllowBlank(allowBlank);
		container.add(this);
	}

	public T getXValue() {
		ObjectModel<T> selectedValue = getValue();
		if (selectedValue != null)
			return selectedValue.getObject();
		else
			return value;
	}

	public void setXValue(T value) {
		this.value = value;
		if (value != null)
			setValue(new ObjectModel<T>(value, render(value)));
		else
			setValue(null);
	}

	protected abstract void load(int offset, int limit, String query,
			ObjectFieldCallback<T> callback);

	protected abstract String render(T value);
}
