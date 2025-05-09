package ru.imagebook.shared.model.editor;

public class SafeAreaImpl extends RectangleImpl implements SafeArea {
	private static final long serialVersionUID = -4598699514957156464L;

	public SafeAreaImpl() {
		setBlocked(true);
	}

	public SafeAreaImpl(SafeArea prototype) {
		super(prototype);
	}

	@Override
	public SafeArea copy() {
		return new SafeAreaImpl(this);
	}

	@Override
	public void accept(ComponentVisitor visitor) {
		visitor.visit(this);
	}
}
