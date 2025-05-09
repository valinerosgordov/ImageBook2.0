package ru.imagebook.model;

import ru.minogin.core.client.bean.BaseEntityBean;

public class OrderBean extends BaseEntityBean implements Comparable<OrderBean> {
	private static final long serialVersionUID = 626751506406680302L;

	@Override
	public int compareTo(OrderBean bean) {
		return getId().compareTo(bean.getId());
	}
}
