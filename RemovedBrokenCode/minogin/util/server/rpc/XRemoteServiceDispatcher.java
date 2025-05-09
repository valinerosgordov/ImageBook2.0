package ru.minogin.util.server.rpc;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.gwtrpcspring.RemoteServiceDispatcher;

import ru.minogin.util.shared.exceptions.Exceptions;

import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.gwt.user.server.rpc.SerializationPolicyLoader;

public class XRemoteServiceDispatcher extends RemoteServiceDispatcher {
	private static final long serialVersionUID = 690261073617452071L;

	@Override
	protected SerializationPolicy doGetSerializationPolicy(HttpServletRequest request, String moduleBaseURL,
			String strongName) {
		try {
			// TODO [sec]
			String requestUrl = request.getRequestURL().toString();
			if (requestUrl.contains("8888"))
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
