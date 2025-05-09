package ru.imagebook.server.service.editor;

public class EditorConfig {
	private String storagePath;
	private String layoutPath;
	private String templatePath;

	public String getStoragePath() {
		return storagePath;
	}

	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}

	public String getLayoutPath() {
		return layoutPath;
	}

	public void setLayoutPath(String layoutPath) {
		this.layoutPath = layoutPath;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}
}
