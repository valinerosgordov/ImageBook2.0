package ru.minogin.oo.server.text;

public enum BorderSide {
	LEFT("LeftBorder"),
	RIGHT("RightBorder"),
	TOP("TopBorder"),
	BOTTOM("BottomBorder");
	
	private final String propertyName;
	
	BorderSide(String propertyName) {
		this.propertyName = propertyName;
	}
	
	String getPropertyName() {
		return propertyName;
	}
}
