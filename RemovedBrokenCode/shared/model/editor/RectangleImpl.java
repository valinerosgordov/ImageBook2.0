package ru.imagebook.shared.model.editor;

public abstract class RectangleImpl extends ComponentImpl implements Rectangle {
	private static final long serialVersionUID = -9036462615559761703L;

	public RectangleImpl() {}

	public RectangleImpl(Rectangle prototype) {
		super(prototype);

		setLeft(prototype.getLeft());
		setTop(prototype.getTop());
		setWidth(prototype.getWidth());
		setHeight(prototype.getHeight());
	}

	@Override
	public float getLeft() {
		return (Float) get(LEFT);
	}

	@Override
	public void setLeft(float left) {
		set(LEFT, left);
	}

	@Override
	public float getTop() {
		return (Float) get(TOP);
	}

	@Override
	public void setTop(float top) {
		set(TOP, top);
	}

	@Override
	public float getWidth() {
		return (Float) get(WIDTH);
	}

	@Override
	public void setWidth(float width) {
		set(WIDTH, width);
	}

	@Override
	public float getHeight() {
		return (Float) get(HEIGHT);
	}

	@Override
	public void setHeight(float height) {
		set(HEIGHT, height);
	}

	@Override
	public void setPosition(Rectangle rectangle) {
		setLeft(rectangle.getLeft());
		setTop(rectangle.getTop());
		setWidth(rectangle.getWidth());
		setHeight(rectangle.getHeight());
	}
}
