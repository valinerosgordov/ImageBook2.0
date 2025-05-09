package ru.minogin.core.client.i18n;

public class NounType {
	private String name;
	private String description;

	public NounType(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
