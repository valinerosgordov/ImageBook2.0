package ru.minogin.core.client.util;

public class SiteUtil {
	public static String url(String url) {
		url = url.trim();
		if (url.startsWith("http://"))
			return url;
		else
			return "http://" + url;
	}

	public static String baseDomain(String url) {
		int i = url.lastIndexOf(".");
		if (i == -1)
			return url;
		int i2 = url.lastIndexOf(".", i - 1);
		if (i2 == -1)
			return url;
		return url.substring(i2 + 1);
	}
}
