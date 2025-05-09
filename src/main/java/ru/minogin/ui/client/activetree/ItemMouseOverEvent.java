package ru.minogin.ui.client.activetree;

import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.shared.GwtEvent;

public class ItemMouseOverEvent<T> extends GwtEvent<ItemMouseOverHandler<T>> {
	public static final Type<ItemMouseOverHandler<?>> TYPE = new Type<ItemMouseOverHandler<?>>();

	private final T value;
	private final TreeItem<T> treeItem;
	private final MouseOverEvent sourceEvent;

	public ItemMouseOverEvent(T value, TreeItem<T> item,
			MouseOverEvent sourceEvent) {
		this.value = value;
		this.treeItem = item;
		this.sourceEvent = sourceEvent;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Type<ItemMouseOverHandler<T>> getAssociatedType() {
		return (Type) TYPE;
	}

	@Override
	protected void dispatch(ItemMouseOverHandler<T> handler) {
		handler.onItemMouseOver(this);
	}

	public T getValue() {
		return value;
	}

	public TreeItem<T> getItem() {
		return treeItem;
	}

	public MouseOverEvent getSourceEvent() {
		return sourceEvent;
	}
}
