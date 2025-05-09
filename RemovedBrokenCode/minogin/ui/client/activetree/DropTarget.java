package ru.minogin.ui.client.activetree;

public class DropTarget<T> {
	public enum Type {
		NEIGHBOUR, CHILD;
	}
	
	private Type type;
	private TreeItem<T> target;
	private int index;

	public DropTarget(Type type, TreeItem<T> target, int index) {
		this.type = type;
		this.target = target;
		this.index = index;
	}
	
	public Type getType() {
		return type;
	}

	public TreeItem<T> getTarget() {
		return target;
	}

	public int getIndex() {
		return index;
	}
}
