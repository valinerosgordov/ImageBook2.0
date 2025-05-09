package ru.saasengine.client.model.ui;

import ru.minogin.core.client.common.Builder;

public class ShowObjectsActionBuilder implements Builder<ShowObjectsAction> {
	@Override
	public ShowObjectsAction newInstance() {
		return new ShowObjectsAction();
	}
}
