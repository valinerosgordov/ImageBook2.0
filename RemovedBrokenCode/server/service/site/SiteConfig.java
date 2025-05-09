package ru.imagebook.server.service.site;

public class SiteConfig {
	private String url;
	private String staticUrl;
	private String innerPrefix;
	private String filesPath;
	private String filesUrl;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStaticUrl() {
		return staticUrl;
	}

	public void setStaticUrl(String staticUrl) {
		this.staticUrl = staticUrl;
	}

	public String getInnerPrefix() {
		return innerPrefix;
	}

	public void setInnerPrefix(String innerPrefix) {
		this.innerPrefix = innerPrefix;
	}

	public String getFilesPath() {
		return filesPath;
	}

	public void setFilesPath(String filesPath) {
		this.filesPath = filesPath;
	}

	public String getFilesUrl() {
		return filesUrl;
	}

	public void setFilesUrl(String filesUrl) {
		this.filesUrl = filesUrl;
	}
}
