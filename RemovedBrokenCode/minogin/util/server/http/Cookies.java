package ru.minogin.util.server.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Cookies {
	public static final int DAY = 60 * 60 * 24;
	public static final int WEEK = DAY * 7;
	public static final int YEAR = DAY * 365;

	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(name))
				return cookie.getValue();
		}
		return null;
	}

	public static void setCookie(HttpServletResponse response, String name,
			String value, int ageSec) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(ageSec);
		response.addCookie(cookie);
	}

	public static void setSessionCookie(HttpServletResponse response,
			String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
