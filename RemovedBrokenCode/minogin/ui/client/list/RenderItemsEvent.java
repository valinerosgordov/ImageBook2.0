package ru.minogin.ui.client.list;

import com.google.gwt.event.shared.GwtEvent;

public class RenderItemsEvent extends GwtEvent<RenderItemsHandler> {
	public static final Type<RenderItemsHandler> TYPE = new Type<RenderItemsHandler>();

	@Override
	public Type<RenderItemsHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(RenderItemsHandler handler) {
		handler.onRenderItems(this);
	}
}
