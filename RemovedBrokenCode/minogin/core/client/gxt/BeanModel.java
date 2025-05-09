package ru.minogin.core.client.gxt;

import java.util.Collection;
import java.util.Map;

import ru.minogin.core.client.bean.Bean;

import com.extjs.gxt.ui.client.data.ModelData;

public class BeanModel<B extends Bean> implements ModelData {
	public static final String BEAN = "_bean";

	private B bean;

	public BeanModel(B bean) {
		this.bean = bean;

		set(BEAN, bean);
	}

	public B getBean() {
		return bean;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X get(String property) {
		return (X) bean.get(property);
	}

	@Override
	public Map<String, Object> getProperties() {
		return bean.getProperties();
	}

	@Override
	public Collection<String> getPropertyNames() {
		return bean.getPropertyNames();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X remove(String property) {
		return (X) bean.remove(property);
	}

	@Override
	public <X> X set(String property, X value) {
		return (X) bean.set(property, value);
	}

	public <X> X setTransient(String property, X value) {
		return (X) bean.setTransient(property, value);
	}
}
