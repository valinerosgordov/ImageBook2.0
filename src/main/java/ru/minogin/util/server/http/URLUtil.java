package ru.minogin.util.server.http;

public class URLUtil {
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
