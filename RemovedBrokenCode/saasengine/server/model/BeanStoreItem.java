package ru.saasengine.server.model;

import ru.minogin.core.client.bean.BaseIdBean;

public class BeanStoreItem extends BaseIdBean {
	private static final long serialVersionUID = 1677366149811444218L;

	public static final String JSON = "json";

	BeanStoreItem() {}

	public BeanStoreItem(String id, String json) {
		setId(id);
		setJson(json);
	}

	public String getJson() {
		return get(JSON);
	}

	public void setJson(String json) {
		set(JSON, json);
	}
}
