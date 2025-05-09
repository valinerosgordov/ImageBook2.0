package ru.imagebook.server.ctl.site;

import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ShowPageMessage extends BaseMessage {
	private static final long serialVersionUID = -2855286652140957030L;

	public static final String URI = "uri";
	public static final String WRITER = "writer";
	private HttpServletResponse response;

	public ShowPageMessage(String uri, Writer writer, HttpServletResponse response) {
		super(SiteMessages.SHOW_PAGE);

		this.response = response;

		addAspects(RemotingAspect.REMOTE);

		set(URI, uri);
		set(WRITER, writer);
	}

	public String getUri() {
		return get(URI);
	}

	public Writer getWriter() {
		return get(WRITER);
	}

	public HttpServletResponse getResponse() {
		return response;
	}
}
