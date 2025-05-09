package ru.minogin.core.server.spring;

import javax.servlet.http.HttpServlet;

import org.springframework.core.io.ResourceLoader;

public class SpringContextAwareServlet extends HttpServlet {
	private static final long serialVersionUID = 628196114624254967L;

	@SuppressWarnings("unchecked")
	protected <T> T getBean(String name) {
		return (T) SpringContextSupport.getBean(getServletContext(), name);
	}

	protected ResourceLoader getResourceLoader() {
		return SpringContextSupport.getResourceLoader(getServletContext());
	}
}
