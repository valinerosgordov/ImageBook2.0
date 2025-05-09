package ru.minogin.core.client.lang.template;

public class Part {
	public enum Type {
		TEXT,
		CODE;
	}

	private Type type;
	private StringBuffer text = new StringBuffer();

	public Part(Type type) {
		this.type = type;
	}

	public Type getType() {
		return type;
	}

	public StringBuffer getText() {
		return text;
	}
}
