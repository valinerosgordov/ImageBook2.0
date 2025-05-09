package ru.minogin.oo.server.format;

import ru.minogin.oo.server.text.TextAlign;

public class Format {
	private String fontName;
	private Float height;
	private boolean bold;
	private Integer backgroundColor;
	private TextAlign align;

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public Float getHeight() {
		return height;
	}

	public void setHeight(Float height) {
		this.height = height;
	}
	
	public boolean isBold() {
		return bold;
	}
	
	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public Integer getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Integer backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void setAlign(TextAlign align) {
		this.align = align;
	}

	public TextAlign getAlign() {
		return align;
	}
}
