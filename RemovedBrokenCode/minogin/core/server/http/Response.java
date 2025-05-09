package ru.minogin.core.server.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import ru.minogin.core.client.exception.Exceptions;

public class Response {
	private final HttpResponse httpResponse;

	public Response(HttpResponse httpResponse) {
		this.httpResponse = httpResponse;
	}

	public HttpResponse getHttpResponse() {
		return httpResponse;
	}

	public String getContent() {
		try {
			HttpEntity entity = httpResponse.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		}
		catch (Exception e) {
			return Exceptions.rethrow(e);
		}
	}

	public int getStatusCode() {
		return httpResponse.getStatusLine().getStatusCode();
	}
}
