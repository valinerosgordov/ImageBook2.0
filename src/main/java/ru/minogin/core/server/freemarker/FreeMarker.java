package ru.minogin.core.server.freemarker;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ru.minogin.core.client.exception.Exceptions;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class FreeMarker {
	private final Configuration config;
	private final Map<String, Object> root = new HashMap<String, Object>();

	public FreeMarker(Class<?> xClass) {
		config = new Configuration();
		config.setObjectWrapper(new DefaultObjectWrapper());
		// config.setEncoding(new Locale(locale), "UTF-8");
		config.setDefaultEncoding("UTF-8");
		config.setOutputEncoding("UTF-8");
		config.setClassForTemplateLoading(xClass, "");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
	}

	public void set(String var, Object value) {
		root.put(var, value);
	}

	public void process(String name, String locale, Writer writer) {
		try {
			Template template = config.getTemplate(name, new Locale(locale));
			template.process(root, writer);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	public String process(String name, String locale) {
		StringWriter writer = new StringWriter();
		process(name, locale, writer);
		return writer.toString();
	}
}
