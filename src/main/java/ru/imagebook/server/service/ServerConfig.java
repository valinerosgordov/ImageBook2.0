package ru.imagebook.server.service;

public class ServerConfig {
	private String appContextUrl;
	private String flashContextUrl;
	private String calcPrefix;
	private String editorPrefix;
	private String webPrefix;

	public String getAppContextUrl() {
		return appContextUrl;
	}

	public void setAppContextUrl(String appContextUrl) {
		this.appContextUrl = appContextUrl;
	}

	public String getCalcPrefix() {
		return calcPrefix;
	}

	public void setCalcPrefix(String calcPrefix) {
		this.calcPrefix = calcPrefix;
	}

	public String getEditorPrefix() {
		return editorPrefix;
	}

	public void setEditorPrefix(String editorPrefix) {
		this.editorPrefix = editorPrefix;
	}

	public String getWebPrefix() {
		return webPrefix;
	}

	public void setWebPrefix(String webPrefix) {
		this.webPrefix = webPrefix;
	}

	public String getFlashContextUrl() {
		return flashContextUrl;
	}

	public void setFlashContextUrl(String flashContextUrl) {
		this.flashContextUrl = flashContextUrl;
	}
}
