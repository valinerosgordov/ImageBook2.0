package ru.minogin.core.server.flow.remoting;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import ru.minogin.core.client.exception.Exceptions;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;

public class XRemoteServiceServlet extends RemoteServiceServlet {
	private static final long serialVersionUID = 4251307249629725213L;

	@Override
	protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL,
			String strongName) {
		try {
			// TODO
			String requestUrl = request.getRequestURL().toString();
			if (requestUrl.contains("127.0.0.1:8888") || requestUrl.contains(".mt:8888"))
				return super.doGetSerializationPolicy(request, moduleBaseURL, strongName);

			String requestURI = request.getRequestURI();
			requestURI = "http://localhost:8080" + requestURI.substring(0, requestURI.lastIndexOf("/") + 1);
			String url = SerializationPolicyLoader.getSerializationPolicyFileName(requestURI + strongName);
			InputStream is = new URL(url).openStream();
			List<ClassNotFoundException> list = new ArrayList<ClassNotFoundException>();
			return SerializationPolicyLoader.loadFromStream(is, list);
		}
		catch (Exception e) {
			return Exceptions.rethrow(e);
		}
	}
}
