package ru.saasengine.client.model.filter;

public interface ItemVisitor<T> {
	public T visit(Block block);

	public T visit(FilterCondition condition);
}
