package ru.imagebook.server.service.flash;

public class FlashConfig {
	private String host;
	private String user;
	private String password;
	private String jpegPath;
	private String flashPath;
	private String webFlashPath;
	private String publishPath;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getJpegPath() {
		return jpegPath;
	}

	public void setJpegPath(String jpegPath) {
		this.jpegPath = jpegPath;
	}

	public String getFlashPath() {
		return flashPath;
	}

	public void setFlashPath(String flashPath) {
		this.flashPath = flashPath;
	}

	public String getWebFlashPath() {
		return webFlashPath;
	}

	public void setWebFlashPath(String webFlashPath) {
		this.webFlashPath = webFlashPath;
	}

	public String getPublishPath() {
		return publishPath;
	}

	public void setPublishPath(String publishPath) {
		this.publishPath = publishPath;
	}
}
