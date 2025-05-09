package ru.minogin.ui.client.tree;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

public class OpenEvent extends GwtEvent<OpenHandler> {
	public static final Type<OpenHandler> TYPE = new Type<OpenHandler>();

	private final Widget widget;

	public OpenEvent(Widget widget) {
		this.widget = widget;
	}

	@Override
	public Type<OpenHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(OpenHandler handler) {
		handler.onOpen(this);
	}

	public Widget getWidget() {
		return widget;
	}
}
