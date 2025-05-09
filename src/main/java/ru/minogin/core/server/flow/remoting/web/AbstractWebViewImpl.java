package ru.minogin.core.server.flow.remoting.web;

import java.io.Writer;

import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.server.freemarker.FreeMarker;

public class AbstractWebViewImpl extends View implements AbstractWebView {
	private final ThreadLocal<FreeMarker> threadLocalFreeMarker = new ThreadLocal<FreeMarker>() {
		@Override
		protected FreeMarker initialValue() {
			return new FreeMarker(AbstractWebViewImpl.this.getClass());
		}
	};

	public AbstractWebViewImpl(Dispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public FreeMarker getFreeMarker() {
		return threadLocalFreeMarker.get();
	}

	@Override
	public void process(String template, String locale, Writer writer) {
		FreeMarker freeMarker = getFreeMarker();
		freeMarker.process(template, locale, writer);
		threadLocalFreeMarker.remove();
	}
}
