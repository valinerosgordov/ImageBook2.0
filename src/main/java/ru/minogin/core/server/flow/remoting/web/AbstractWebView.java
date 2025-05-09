package ru.minogin.core.server.flow.remoting.web;

import java.io.Writer;

import ru.minogin.core.server.freemarker.FreeMarker;

public interface AbstractWebView {
	FreeMarker getFreeMarker();

	void process(String template, String locale, Writer writer);
}
