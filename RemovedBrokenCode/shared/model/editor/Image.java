package ru.imagebook.shared.model.editor;

public interface Image extends Rectangle {
	String LAYOUT_TYPE = "layoutType";
	String CLIP_LEFT = "clipLeft";
	String CLIP_TOP = "clipTop";
	String CLIP_WIDTH = "clipWidth";
	String CLIP_HEIGHT = "clipHeight";
	
	String PROTOTYPE_ID = "prototypeId";

	int getLayoutType();

	void setLayoutType(int layoutType);

	float getClipLeft();

	void setClipLeft(float clipLeft);

	float getClipTop();

	void setClipTop(float clipTop);

	float getClipWidth();

	void setClipWidth(float clipWidth);

	float getClipHeight();

	void setClipHeight(float clipHeight);
	
	int getPrototypeId();

	void setPrototypeId(int prototypeId);
	
	@Override
	Image copy();
}
