package ru.minogin.core.server.file;

import java.io.File;

import ru.minogin.core.client.bean.BaseIdBean;

public class TempFile extends BaseIdBean {
	private static final long serialVersionUID = -7402331620851479468L;
	
	public static final String NAME = "name";
	public static final String FILE = "file";

	public TempFile(String id, String name, File file) {
		super(id);
		
		set(NAME, name);
		set(FILE, file);
	}

	public String getName() {
		return get(NAME);
	}
	
	public File getFile() {
		return get(FILE);
	}
}
