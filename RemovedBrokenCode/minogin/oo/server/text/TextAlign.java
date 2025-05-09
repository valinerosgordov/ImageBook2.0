package ru.minogin.oo.server.text;

import com.sun.star.style.ParagraphAdjust;

public enum TextAlign {
	LEFT(ParagraphAdjust.LEFT_value),
	RIGHT(ParagraphAdjust.RIGHT_value),
	BLOCK(ParagraphAdjust.BLOCK_value),
	CENTER(ParagraphAdjust.CENTER_value),
	STRETCH(ParagraphAdjust.STRETCH_value);
	
	private final int style;
	
	TextAlign(int style) {
		this.style = style;
	}
	
	int getStyle() {
		return style;
	}
	
	static TextAlign byStyle(int style) {
		for (TextAlign align : TextAlign.values()) {
			if (align.getStyle() == style) {
				return align;
			}
		}
		return null;
	}
}
