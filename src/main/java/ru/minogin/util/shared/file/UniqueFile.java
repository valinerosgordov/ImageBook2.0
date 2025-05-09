package ru.minogin.util.shared.file;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UniqueFile implements IsSerializable {
	private String id;
	private String name;

	UniqueFile() {
	}

	public UniqueFile(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
