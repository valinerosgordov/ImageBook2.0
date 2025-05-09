package ru.minogin.gwt.client.url;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;

public class URL {
	public static String getLocalUrl(String relativeUrl) {
		int pos = relativeUrl.indexOf("#");
		if (pos == -1)
			pos = relativeUrl.length();

		String gwt = "";
		if (!GWT.isProdMode()) {
			if (!relativeUrl.contains("?"))
				gwt = "?";
			else
				gwt = "&";
			gwt += "gwt.codesvr=127.0.0.1:9997";
		}

		relativeUrl = relativeUrl.substring(0, pos) + gwt
				+ relativeUrl.substring(pos);
		return GWT.getHostPageBaseURL() + relativeUrl;
	}

	public static String getCurrentPageKey() {
		String path = Window.Location.getPath();
		String[] parts = path.split("/");
		if (parts.length == 0)
			return null;
		return parts[parts.length - 1];
	}
}
