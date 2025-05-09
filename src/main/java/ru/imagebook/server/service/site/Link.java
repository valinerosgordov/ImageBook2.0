package ru.imagebook.server.service.site;

public class Link {
	private String name;
	private String url;

	public Link(String name) {
		this.name = name;
	}

	public Link(String name, String url) {
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
