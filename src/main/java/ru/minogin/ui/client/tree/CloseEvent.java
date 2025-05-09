package ru.minogin.ui.client.tree;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

public class CloseEvent extends GwtEvent<CloseHandler> {
	public static final Type<CloseHandler> TYPE = new Type<CloseHandler>();

	private final Widget widget;

	public CloseEvent(Widget widget) {
		this.widget = widget;
	}

	@Override
	public Type<CloseHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(CloseHandler handler) {
		handler.onClose(this);
	}

	public Widget getWidget() {
		return widget;
	}
}
