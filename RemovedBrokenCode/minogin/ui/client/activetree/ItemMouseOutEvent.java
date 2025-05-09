package ru.minogin.ui.client.activetree;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

public class ItemMouseOutEvent<T> extends GwtEvent<ItemMouseOutHandler<T>> {
	public static final Type<ItemMouseOutHandler<?>> TYPE = new Type<ItemMouseOutHandler<?>>();

	private final T value;
	private final Widget widget;
	private final TreeItem<T> treeItem;
	private final MouseOutEvent sourceEvent;

	public ItemMouseOutEvent(T value, Widget widget, TreeItem<T> item,
			MouseOutEvent sourceEvent) {
		this.value = value;
		this.widget = widget;
		this.treeItem = item;
		this.sourceEvent = sourceEvent;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Type<ItemMouseOutHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(ItemMouseOutHandler<T> handler) {
		handler.onItemMouseOut(this);
	}

	public T getValue() {
		return value;
	}

	public Widget getWidget() {
		return widget;
	}

	public TreeItem<T> getItem() {
		return treeItem;
	}

	public MouseOutEvent getSourceEvent() {
		return sourceEvent;
	}
}
