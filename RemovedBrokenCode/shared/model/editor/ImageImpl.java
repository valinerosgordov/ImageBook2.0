package ru.imagebook.shared.model.editor;

public class ImageImpl extends RectangleImpl implements Image {
	private static final long serialVersionUID = -4278060581685034342L;

	public ImageImpl() {}

	public ImageImpl(Image prototype) {
		super(prototype);

		setLayoutType(prototype.getLayoutType());
		setClipLeft(prototype.getClipLeft());
		setClipTop(prototype.getClipTop());
		setClipWidth(prototype.getClipWidth());
		setClipHeight(prototype.getClipHeight());
		setPrototypeId(prototype.getId());
	}

	@Override
	public int getLayoutType() {
		return (Integer) get(LAYOUT_TYPE);
	}

	@Override
	public void setLayoutType(int layoutType) {
		set(LAYOUT_TYPE, layoutType);
	}

	@Override
	public float getClipLeft() {
		return (Float) get(CLIP_LEFT);
	}

	@Override
	public void setClipLeft(float clipLeft) {
		set(CLIP_LEFT, clipLeft);
	}

	@Override
	public float getClipTop() {
		return (Float) get(CLIP_TOP);
	}

	@Override
	public void setClipTop(float clipTop) {
		set(CLIP_TOP, clipTop);
	}

	@Override
	public float getClipWidth() {
		return (Float) get(CLIP_WIDTH);
	}

	@Override
	public void setClipWidth(float clipWidth) {
		set(CLIP_WIDTH, clipWidth);
	}

	@Override
	public float getClipHeight() {
		return (Float) get(CLIP_HEIGHT);
	}

	@Override
	public void setClipHeight(float clipHeight) {
		set(CLIP_HEIGHT, clipHeight);
	}

	@Override
	public int getPrototypeId() {
		return (Integer) get(PROTOTYPE_ID);
	}

	@Override
	public void setPrototypeId(int prototypeId) {
		set(PROTOTYPE_ID, prototypeId);
	}

	@Override
	public Image copy() {
		return new ImageImpl(this);
	}

	@Override
	public void accept(ComponentVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof Image))
			return false;

		Image image = (Image) obj;
		if (image.getId() == null || getId() == null)
			return false;

		return image.getId().equals(getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}
}
