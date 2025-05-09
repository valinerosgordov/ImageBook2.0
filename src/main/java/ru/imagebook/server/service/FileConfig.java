package ru.imagebook.server.service;

public class FileConfig {
	private String path;
	private String tempPath;
	private String downloadPath;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTempPath() {
		return tempPath;
	}

	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public String getDownloadPath() {
		return downloadPath;
	}
}
