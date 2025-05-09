package ru.imagebook.server.service.flash;

public class Page {
	private int type;
	private int number;
	private String name;

	public Page(int type, int number) {
		this.type = type;
		this.number = number;
	}

	public int getType() {
		return type;
	}

	public int getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
