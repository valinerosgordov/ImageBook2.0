package ru.imagebook.shared.model.editor;

public class PositionImpl extends RectangleImpl implements Position {
	private static final long serialVersionUID = -7089677529643678591L;

	public PositionImpl() {}

	public PositionImpl(Position prototype) {
		super(prototype);
	}

	@Override
	public Position copy() {
		return new PositionImpl(this);
	}

	@Override
	public void accept(ComponentVisitor visitor) {
		visitor.visit(this);
	}
}
