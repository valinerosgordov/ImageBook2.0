package ru.imagebook.server.model.web;

public class Breadcrumb {
	private String name;
	private String url;

	public Breadcrumb(String name) {
		this.name = name;
	}

	public Breadcrumb(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}
}
