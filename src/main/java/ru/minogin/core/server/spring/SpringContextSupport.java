package ru.minogin.core.server.spring;

import javax.servlet.ServletContext;

import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringContextSupport {
	@SuppressWarnings("unchecked")
	public static <T> T getBean(ServletContext servletContext, String name) {
		WebApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);
		return (T) context.getBean(name);
	}

	public static <T> T getBean(ServletContext servletContext, Class<T> xClass) {
		WebApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);
		return (T) context.getBean(xClass);
	}

	public static ResourceLoader getResourceLoader(ServletContext servletContext) {
		WebApplicationContext context = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);
		return context;
	}
}
