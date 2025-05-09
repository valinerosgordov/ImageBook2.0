package ru.minogin.util.server.spring;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringUtil {
	public static <T> T getBean(Class<T> clazz) {
		ApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
		return (T) context.getBean(clazz);
	}

	public static ServletContext getServletContext() {
		return getSession().getServletContext();
	}

	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	public static HttpServletRequest getRequest() {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		return requestAttributes.getRequest();
	}

	public static <T> T getBean(ServletContext servletContext, Class<T> clazz) {
		WebApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);
		return (T) context.getBean(clazz);
	}
}
