package ru.minogin.ui.client.list;

public class Memento {
	private String position;
	private String zIndex;
	private String opacity;
	private int absoluteLeft;
	private int absoluteTop;
	private String cursor;

	public void setPosition(String position) {
		this.position = position;
	}

	public void setZIndex(String zIndex) {
		this.zIndex = zIndex;
	}

	public void setOpacity(String opacity) {
		this.opacity = opacity;
	}

	public void setAbsoluteLeft(int absoluteLeft) {
		this.absoluteLeft = absoluteLeft;
	}

	public void setAbsoluteTop(int absoluteTop) {
		this.absoluteTop = absoluteTop;
	}

	public String getzIndex() {
		return zIndex;
	}

	public void setzIndex(String zIndex) {
		this.zIndex = zIndex;
	}

	public String getPosition() {
		return position;
	}

	public String getOpacity() {
		return opacity;
	}

	public int getAbsoluteLeft() {
		return absoluteLeft;
	}

	public int getAbsoluteTop() {
		return absoluteTop;
	}

	public String getCursor() {
		return cursor;
	}

	public void setCursor(String cursor) {
		this.cursor = cursor;
	}
}
