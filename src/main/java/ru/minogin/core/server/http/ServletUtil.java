package ru.minogin.core.server.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class ServletUtil {
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(name)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	public static Cookie findCookie(HttpServletRequest request, String name,
			String domain, String value) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName()) && domain.equals(cookie.getDomain())
					&& value.equals(cookie.getValue())) {
				return cookie;
			}
		}
		return null;
	}
}
