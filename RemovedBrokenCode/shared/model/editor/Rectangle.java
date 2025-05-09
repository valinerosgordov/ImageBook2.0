package ru.imagebook.shared.model.editor;

public interface Rectangle extends Component {
	String LEFT = "left";
	String TOP = "top";
	String WIDTH = "width";
	String HEIGHT = "height";

	float getLeft();

	void setLeft(float left);

	float getTop();

	void setTop(float top);

	float getWidth();

	void setWidth(float width);

	float getHeight();

	void setHeight(float height);

	void setPosition(Rectangle rectangle);
}
