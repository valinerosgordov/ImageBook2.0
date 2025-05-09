package ru.imagebook.shared.model.flash;

import java.io.Serializable;

public class Flash implements Serializable {
	private static final long serialVersionUID = 4250885137545412982L;

	private int width;
	private int height;
	private String code;

	Flash() {}

	public Flash(int width, int height, String code) {
		this.width = width;
		this.height = height;
		this.code = code;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getCode() {
		return code;
	}
}
