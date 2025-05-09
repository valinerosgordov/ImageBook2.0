package ru.minogin.core.server.mail;

import java.io.File;

public class Attachment {
	private String id;
	private String name;
	private File file;

	public Attachment(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public File getFile() {
		return file;
	}
}
