package ru.minogin.core.server;

import java.io.File;

public class FileManager {
	public void delete(String path) {
		new File(path).delete();
	}

	public void mkdirs(String path) {
		new File(path).mkdirs();
	}
}
