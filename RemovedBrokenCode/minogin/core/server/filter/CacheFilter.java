package ru.minogin.core.server.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CacheFilter implements Filter {
	public static final long HOUR_SEC = 60 * 60;
	public static final long DAY_SEC = HOUR_SEC * 24;
	public static final long WEEK_SEC = DAY_SEC * 7;
	public static final long MONTH_SEC = DAY_SEC * 30;
	public static final long YEAR_SEC = DAY_SEC * 365;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String uri = httpRequest.getRequestURI();

		HttpServletResponse httpResponse = (HttpServletResponse) response;

		Long maxAge = null;
		if (uri.contains(".cache."))
			maxAge = YEAR_SEC;
		else if (uri.startsWith("/static/"))
			maxAge = MONTH_SEC;
		else if (uri.startsWith("/web"))
			maxAge = DAY_SEC;

		if (maxAge != null)
			httpResponse.setHeader("Cache-Control", "public, max-age=" + maxAge
					+ ", must-revalidate");
		else
			httpResponse.setHeader("Cache-Control", "no-cache, must-revalidate");

		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void destroy() {}
}
