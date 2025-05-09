package ru.minogin.core.server.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;

import ru.minogin.core.client.exception.Exceptions;

public class XHttpClient {
	private HttpClient client;

	public XHttpClient() {
		client = new DefaultHttpClient();
	}

	public Response postXml(String url, String xml, String contentType) {
		try {
			HttpPost post = new HttpPost(url);
			post.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);

			post.setHeader("Content-Type", contentType);

			StringEntity entity = new StringEntity(xml, HTTP.UTF_8);
			entity.setContentType("text/xml");
			post.setEntity(entity);

			HttpResponse httpResponse = client.execute(post);
			return new Response(httpResponse);
		}
		catch (Exception e) {
			return Exceptions.rethrow(e);
		}
	}

	public Response postXml(String url, String xml) {
		return postXml(url, xml, "text/xml; charset=UTF-8");
	}
}
