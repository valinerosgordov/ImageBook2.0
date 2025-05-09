package ru.minogin.core.server.gxt;

import net.fckeditor.handlers.PropertiesLoader;

public class HtmlFieldConfigurer {
	private final String filesUrl;
	private final String filesPath;
	private int height = 500;

	public HtmlFieldConfigurer(String filesUrl, String filesPath) {
		this.filesUrl = filesUrl;
		this.filesPath = filesPath;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void configure() {
		PropertiesLoader.setProperty("fckeditor.height", height + "");
		PropertiesLoader.setProperty("fckeditor.basePath", "/static/fckeditor");
		PropertiesLoader.setProperty("connector.userActionImpl",
				"net.fckeditor.requestcycle.impl.EnabledUserAction");
		PropertiesLoader.setProperty("connector.impl", "net.fckeditor.connector.impl.LocalConnector");
		PropertiesLoader.setProperty("connector.userPathBuilderImpl",
				"net.fckeditor.requestcycle.impl.ServerRootPathBuilder");
		PropertiesLoader.setProperty("connector.userFilesPath", filesUrl);
		PropertiesLoader.setProperty("connector.userFilesAbsolutePath", filesPath);
	}
}
